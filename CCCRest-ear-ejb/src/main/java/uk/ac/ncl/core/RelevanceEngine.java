package uk.ac.ncl.core;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.DroolsParserException;
import org.drools.conf.MBeansOption;
import org.drools.io.ResourceChangeScannerConfiguration;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import uk.ac.ncl.checker.DataChecker;
import uk.ac.ncl.checker.DataCheckerImpl;
import uk.ac.ncl.dao.UserManager;
import uk.ac.ncl.dao.UserManagerImpl;
import uk.ac.ncl.event.Event;
import uk.ac.ncl.logging.CCCLogger;
import uk.ac.ncl.response.CCCResponse;
import uk.ac.ncl.rop.Obligation;
import uk.ac.ncl.rop.Prohibition;
import uk.ac.ncl.rop.Right;
import uk.ac.ncl.user.User;
import uk.ac.ncl.util.DateParser;
import uk.ac.ncl.util.EventMarshaller;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;

import static uk.ac.ncl.user.User.setTimeKeeper;
import static uk.ac.ncl.util.EventMarshaller.*;

/**
 * The Class RelevanceEngine. Instances of this class are wrappers around the
 * Drools Rule Engine
 */
public class RelevanceEngine {

    private final static Logger log = Logger.getLogger(RelevanceEngine.class
            .toString());

    // /* Private data
    static KnowledgeBase ruleBase;

    static KnowledgeSessionConfiguration sconfig;

    static StatefulKnowledgeSession workingMem;

    static LinkedList<Event> eventQueue = new LinkedList<Event>();
    static EventLogger eventLogger = null;
    // TimeKeeper timeKeeper = null;

    // For performance testing only
    static boolean performanceTestingOn = false;

    private static Responder responder;

    // default response is non contract compliant otherwise contract compliant
    private static CCCResponse cccResponse = new CCCResponse(false);
    private static CCCLogger ccclog;

    /**
     * Handle compilation errors.
     *
     * @param builder  the builder
     * @param fileName the file name
     */
    private static void handleCompilationErrors(KnowledgeBuilder builder,
                                                String fileName) {
        KnowledgeBuilderErrors builderErrors = builder.getErrors();
        String[] errorMsg = new String[builderErrors.size() + 1];
        errorMsg[0] = new String("Compilation errors for file " + fileName);
        for (int i = 0; i < builderErrors.size(); i++) {
            errorMsg[i + 1] = builderErrors.iterator().next().getMessage();
        }
        ErrorMessageManager.fatalErrorMsg(errorMsg);
    }

    /**
     * Instantiates a new relevance engine.
     *
     * @param fileName the file name of the changeset
     * @param el       the event logger
     * @throws IOException           Signals that an I/O exception has occurred.
     * @throws DroolsParserException the drools parser exception
     */
    public RelevanceEngine(String fileName, EventLogger el) throws IOException,
            DroolsParserException {
        // Verify that the EventLogger is not null
        if (el == null)
            throw new IllegalArgumentException("EventLogger ref null");

        // Create KnowledgeBuilder
        KnowledgeBuilder builder = KnowledgeBuilderFactory
                .newKnowledgeBuilder();

        try {
            builder.add(ResourceFactory.newFileResource(fileName),
                    ResourceType.CHANGE_SET);
        } catch (Exception e) {
            ErrorMessageManager.fatalErrorMsg(
                    "Exception opening file resource " + fileName, e);
        }

        // Check if the compilation was successful
        if (builder.hasErrors()) {
            handleCompilationErrors(builder, fileName);
        }
        KnowledgeBaseConfiguration config = KnowledgeBaseFactory
                .newKnowledgeBaseConfiguration();

        // enable knowledge session monitoring using JMX
        config.setOption(MBeansOption.ENABLED);

        ruleBase = KnowledgeBaseFactory.newKnowledgeBase("CCCbase", config);
        ruleBase.addKnowledgePackages(builder.getKnowledgePackages());
        KnowledgeAgentConfiguration aconf = KnowledgeAgentFactory
                .newKnowledgeAgentConfiguration();

		/*
         *
		 * false: make the agent keep and incrementally update the existing
		 * knowledge base, automatically updating all existing sessions true:
		 * make the agent to create a brand new KnowledgeBase every time there
		 * is a change to the source assets.
		 */
        aconf.setProperty("drools.agent.newInstance", "false");

        KnowledgeAgent kagent = KnowledgeAgentFactory.newKnowledgeAgent(
                "CCCagent", ruleBase, aconf);
        kagent.applyChangeSet(ResourceFactory.newFileResource(fileName));
        ruleBase = kagent.getKnowledgeBase();
        ResourceChangeScannerConfiguration sconf = ResourceFactory
                .getResourceChangeScannerService()
                .newResourceChangeScannerConfiguration();

        // set disc scanning interval in seconds
        sconf.setProperty("drools.resource.scanner.interval", "10");

        ResourceFactory.getResourceChangeScannerService().configure(sconf);

        sconfig = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();

		/*
         *
		 * realtime: uses the system clock to determine the current time for
		 * timestamps. pseudo: for testing temporal rules since it can be
		 * controlled by the application.
		 */
        sconfig.setOption(ClockTypeOption.get("realtime"));

        workingMem = ruleBase.newStatefulKnowledgeSession(sconfig, null);
        // SessionPseudoClock clock = workingMem.getSessionClock();

        // workingMem.addEventListener(new CustomAgendaEventListener());
        // workingMem.addEventListener(new CustomWorkingMemoryEventListener());

        // KnowledgeRuntimeLogger rulesLogger =
        // KnowledgeRuntimeLoggerFactory.newConsoleLogger(workingMem);

        ResourceFactory.getResourceChangeNotifierService().start();
        ResourceFactory.getResourceChangeScannerService().start();

        eventLogger = el;

		/*
         *
		 * Creates a file logger that executes in a different thread, where
		 * information is written on given intervals (in milliseconds). Logs the
		 * execution of the session for later inspection
		 */
        final KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory
                .newThreadedFileLogger(workingMem, "ruleLog", 1000);

        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {

                if (logger != null) {

                    logger.close();

                }

            }

        });
    }

    /**
     * Initialize contract.
     */
    public static void initializeContract(TimeKeeper timeKeeper) {
        if (workingMem != null) {
            workingMem.dispose();
        }
        workingMem = ruleBase.newStatefulKnowledgeSession(sconfig, null);
        log.info("Initializing contract...");
        // Pass global objects to the working memory
        // workingMem.setGlobal("engine", this);
        workingMem.setGlobal("logger", eventLogger);
        DateParser dateParser = new DateParser();

        if (performanceTestingOn) {
            TimingMonitor tm = new TimingMonitor();
            workingMem.setGlobal("timingMonitor", tm);
        }
        // Pass the TimeKeeper instance to the ROPSet class
        setTimeKeeper(timeKeeper);
        DataChecker dataChecker = new DataCheckerImpl();
        workingMem.setGlobal("dataChecker", dataChecker);
        workingMem.setGlobal("dateParser", dateParser);


        responder = new Responder(false);
        workingMem.setGlobal("responder", responder);
        ccclog = new CCCLogger();

        workingMem.setGlobal("cccloger", ccclog);

        log.info("Initialization complete");
    }

    // * Event Management Methods

    /**
     * Adds the event.
     *
     * @param e the e
     */
    public void addEvent(Event e) {
        log.info("- Adding new event to queue");
        try {
            EventMarshaller.marshalEvent(e);
        } catch (JAXBException e1) {
            e1.printStackTrace();
        }
        eventQueue.offer(e);
        // TODO: Change this, the queue should be watched by an
        // Observer that calls the method below, so as to
        // decouple adding events from processing them
        processEventQueue();

    }


    /**
     * Checks if is queue empty.
     *
     * @return true, if is queue empty
     */
    public boolean isQueueEmpty() {
        return eventQueue.isEmpty();
    }

    /**
     * Process event queue.
     */
    public static void processEventQueue() {

        setCCCResponse(new CCCResponse(false));
        responder.setContractCompliant(false);
        // Check if EventLogger is in place, if not refuse to process event
        // queue
        if (eventLogger == null)
            throw new RuntimeException(
                    "EventLogger ref in RelevanceEngine is still null, cannot process events");
        // It is ok, continue with processing
        Event ev = eventQueue.poll();
        // Check if the queue is empty
        if (ev == null)
            return;
        // It is not empty, process event.

        try {
            // Insert new event in working memory
            UserManager userManager = new UserManagerImpl();
            User user = userManager.query(ev.getUsername());
            Right right = userManager.getRight(user, ev.getOperation());
            Obligation obligation = userManager.getObligation(user,
                    ev.getOperation());
            Prohibition prohibition = userManager.getProhibition(user,
                    ev.getOperation());

            log.info("user object: " + user.toString());

            if (right != null) {
                log.info("right object: " + right.toString());
            }

            if (obligation != null) {
                log.info("obligation object: " + obligation.toString());
            }

            if (prohibition != null) {
                log.info("prohibition object: " + prohibition.toString());
            }

            log.info("event object: " + ev.toString());

            if (ev.getOperation() != null && ev.getOperation().getObject() != null) {
                log.info("data: " + ev.getOperation().getObject());
            }

            workingMem.insert(user);
            workingMem.insert(right);
            workingMem.insert(obligation);
            workingMem.insert(prohibition);
            workingMem.insert(ev);

        } catch (Exception e) {
            ErrorMessageManager.errorMsg(
                    "Insertion of new event in working memory failed", e);
            setCCCResponse(new CCCResponse(false));
        }

        log.info("+ Processing event: " + ev.toString());

        // Fire all rules!
        try {

            workingMem.fireAllRules();
            updateCCCResult();

        } catch (Exception e) {
            ErrorMessageManager.errorMsg("Exception when firing rules", e);
            setCCCResponse(new CCCResponse(false));
            cccResponse.setMessage("Exception when firing rules");
        }

    }

    private static void updateCCCResult() {
        log.info("updateCCCResult -> cccresponse: " + responder.getContractCompliant());
        // update response of CCC
        if (responder != null) {
            if (responder.getContractCompliant() == null) {
                setCCCResponse(new CCCResponse(false));
            } else {
                setCCCResponse(new CCCResponse(responder.getContractCompliant()));
                cccResponse.setMessage(responder.getMessage());
            }

            try {
                marshalCCCResponse(getCCCResponse());
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return the cccResponse
     */
    public static CCCResponse getCCCResponse() {
        return cccResponse;
    }

    /**
     * @param cccResponse the cccResponse to set
     */
    public static void setCCCResponse(CCCResponse cccResponse) {
        RelevanceEngine.cccResponse = cccResponse;
    }

}
