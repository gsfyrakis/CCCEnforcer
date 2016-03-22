package uk.ac.ncl.core;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import uk.ac.ncl.conf.ConfigurationFilesEnum;
import uk.ac.ncl.event.Event;
import uk.ac.ncl.resource.Resources;
import uk.ac.ncl.response.CCCResponse;

/**
 * The Class EventsMDB. Message Driven Bean for Business Events
 */
public class CCCEngine {

    private final static Logger log = Logger.getLogger(CCCEngine.class
            .toString());

    @PersistenceContext(unitName = "RopePU")
    private EntityManager em;

    private static ContractComplianceChecker ccc;

    // private List<Event> events = new ArrayList<Event>();

    /**
     * @see MessageListener#onMessage(Message)
     */
    public CCCResponse run(Event event) {

        try {
            Resources.setEntityManager(em);
            ccc = ContractComplianceChecker
                    .createContractComplianceChecker(ConfigurationFilesEnum.CHANGESET_XML
                            .getConfigurationFilePath());

            CCCResponse cccResponse = new CCCResponse();
            cccResponse.setContractCompliant(false);

            log.info("CCC started? " + ccc.cccStarted());
//
//			if (!ccc.cccStarted()) {
				ccc.initializeCCC();
//			}


            cccResponse = processCCCEvent(event);

            log.info("cccResponse: " + cccResponse);

            return cccResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Process ccc event.
     *
     * @param event the event
     * @return the cCC response
     */
    private CCCResponse processCCCEvent(Event event) {
        CCCResponse cccResponse;
        if (!ccc.cccStarted()) {
            ccc.initializeCCC();
            cccResponse = ccc.processEvent(event);
        } else {
            cccResponse = ccc.processEvent(event);
        }
        return cccResponse;
    }


}
