/*
 * 
 */
package uk.ac.ncl.core;

import org.jboss.logging.Logger;
import uk.ac.ncl.event.Event;
import uk.ac.ncl.event.EventStatus;
import uk.ac.ncl.event.Operation;
import uk.ac.ncl.rop.Obligation;
import uk.ac.ncl.rop.RopEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;

/**
 * The Class TimeKeeper.
 */
public class TimeKeeper {

    // /* Private data
    // References to other relevant components of the CCC
    private RelevanceEngine relevanceEngine = null;
    private EventLogger logger = null;
    // HashMap of scheduled Deadlines, kept to track them if
    // they have to be cancelled.
    private HashMap<String, Timer> timerMap = new HashMap<String, Timer>();
    private final static Logger log = Logger.getLogger(TimeKeeper.class.toString());

    /**
     * Instantiates a new time keeper.
     *
     * @param re the re
     * @param el the el
     */
    public TimeKeeper(RelevanceEngine re, EventLogger el) {
        // Verify that arguments are acceptable
        if (re == null)
            throw new IllegalArgumentException("Relevance Engine ref cannot be null");
        if (el == null)
            throw new IllegalArgumentException("Event Logger ref cannot be null");
        relevanceEngine = re;
        logger = el;
        log.info("TimeKeeper successfully instantiated");
    }

    /**
     * Adds the deadline.
     *
     * @param rop  the rop
     * @param user the user
     * @param dl   the dl
     */
    public void addDeadline(RopEntity rop, Operation operation, String user, Date dl) {
        // Verify that arguments are acceptable
        if (rop == null)
            throw new IllegalArgumentException("RopEntity cannot be null in addDeadline()");
        if ((user == null) || (user.length() == 0))
            throw new IllegalArgumentException("user name cannot be null or empty in addDeadline()");
//		if ((responder == null) || (responder.length() == 0))
//			throw new IllegalArgumentException("Responder name cannot be null or empty in addDeadline()");
        if (dl == null)
            throw new IllegalArgumentException("Date for deadline cannot be null in addDeadline()");
        // Create new Deadline object
        Deadline deadline = null;
        if (rop instanceof Obligation)
            // Create a deadline for an expiry, not a timeout
            deadline = new Deadline(this, rop, user, dl);
        else
            // Create a deadline for a timeout
            deadline = new Deadline(this, rop, user);
        // Create new Timer object
        Timer timer = new Timer();
        // And now schedule the deadline with it
        timer.schedule(deadline, dl);
        // And now build a key for it, and store it for retrieval!
        String key = new String(rop.getName() + "-" + operation.getType() + "-" + operation.getName());//dl.toString() + "-");
        // +"-"+DateParser.format(dl)); Not possible to store the deadline
        // easily outside of tk
        timerMap.put(key, timer);
        log.info("  Deadline added: " + key);
    }

    /**
     * Removes the deadline.
     *
     * @param rop        the rop
     * @param originator the originator
     * @param responder  the responder
     * @return true, if successful
     */
    public boolean removeDeadline(RopEntity rop, String originator, String responder) {
        // And now build a key for it, and store it for retrieval!
        String key = new String(rop.getName() + "-" + rop.getState() + "-" + rop.getDeadline() + "-");
        // +"-"+DateParser.format(dl));
        // Remove timer from HashMap
        Timer t = timerMap.remove(key);
        if (t == null)
            return false;
        // Timer ref is present, remove pending Deadlines (only one anyway)
        t.cancel();
        t.purge();
        log.info("  Deadline removed: " + key);
        return true;
    }

    private CCCEngine engine;

    /**
     * Deadline callback.
     * Callback method, called when a deadline expires.
     *
     * @param rop       the rop
     * @param user      the originator
     * @param responder the responder
     * @param isExpiry  the is expiry
     */
    public void deadlineCallback(RopEntity rop, String user, String responder, boolean isExpiry) {
        // Create deadline event, differentiating between timeouts and expires
        String name = new String(rop.getName());

        if (isExpiry) {
            name = name.concat(" Expiry");
        } else {
            name = name.concat(" Timeout");
        }

        log.info("----Deadline callback begin--- ");
        log.info("callback(rop): " + rop);
        log.info("callback(user): " + user);
        log.info("callback(responder): " + responder);
        log.info("callback(isExpiry): " + isExpiry);
        log.info("-----Deadline callback end----");

        Event ev = new Event(user, rop.getOperation(), EventStatus.succeed, rop.getDeadline());

        log.info("Deadline callback event: " + ev);

//        engine.run(ev);
        // Pump the event in the queue and log it
        logger.logEvent(ev);
        relevanceEngine.addEvent(ev);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String s = new String("TimeKeeper instance - Current deadlines:\n");
        Set<String> keys = timerMap.keySet();
        for (String k : keys) {
            s = s.concat(k + " => ");
            Timer tm = timerMap.get(k);
            s = s.concat(tm.toString() + "\n");
        }
        return s;
    }


}
