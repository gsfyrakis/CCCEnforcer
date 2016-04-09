package uk.ac.ncl.core;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ncl.event.Operation;
import uk.ac.ncl.resource.Resources;
import uk.ac.ncl.rop.RopEntity;
import uk.ac.ncl.state.RopState;
import uk.ac.ncl.user.User;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.Date;
import java.util.TimerTask;

@Stateful
@Transactional
public class Deadline extends TimerTask {
    private User user;
    private String expiryDate = null;
    private Operation operation;
    private Date deadline;
    private RopEntity<? extends RopState> rop;

    private TimeKeeper timeKeeper = null;
    //   	private RopEntity rop = null;
    private String userName = null;
    private String responder = null;
    // Flag to identify if this deadline is for an expiry:
    // if true it is, otherwise it's a deadline for a timeout
    private boolean expiryFlag = true;


//    @Resource(mappedName = "java:jboss/TransactionManager")
//    private TransactionManager transactionManager;

    private UserTransaction userTransaction;

    @PersistenceContext(unitName = "RopePU")
    private EntityManager em;


    public Deadline(Date deadline) {
        this.deadline = deadline;
    }

    public Deadline() {
        em = Resources.getEntityManager();
    }

    public Deadline(RopEntity<? extends RopState> rop) {
        this.rop = rop;
    }

    public Deadline(TimeKeeper timeKeeper, RopEntity rop, String originator, String responder, boolean b) {
        this.timeKeeper = timeKeeper;
        this.rop = rop;
        this.expiryFlag = expiryFlag;

    }

    public Deadline(TimeKeeper timeKeeper, RopEntity rop, String user, String responder) {
        this.timeKeeper = timeKeeper;
        this.rop = rop;
        this.expiryFlag = true;
        this.userName = user;
    }

    public Deadline(TimeKeeper timeKeeper, RopEntity rop, User user, String expiryDate) {
        this.timeKeeper = timeKeeper;
        this.rop = rop;
        this.expiryDate = expiryDate;
        this.user = user;
    }

    public Deadline(TimeKeeper timeKeeper, RopEntity rop, String user, Date dl) {
        this.timeKeeper = timeKeeper;
        this.rop = rop;
        this.deadline = dl;
        this.userName = user;
    }

    public Deadline(TimeKeeper timeKeeper, RopEntity rop, String user) {
        this.timeKeeper = timeKeeper;
        this.rop = rop;
        this.userName = user;
        this.expiryFlag = true;
    }

    public Deadline(TimeKeeper timeKeeper, Operation operation, String user, Date dl) {
        this.timeKeeper = timeKeeper;
        this.operation = operation;
        this.deadline = deadline;
        this.userName = user;
    }

    public Deadline(TimeKeeper timeKeeper, Operation operation, String user) {
        this.timeKeeper = timeKeeper;
        this.operation = operation;
        this.userName = user;
        this.expiryFlag = true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void run() {
        timeKeeper.deadlineCallback(rop, this.getUserName(), "", expiryFlag);

    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    /* Checks if is expiry.
       *
  	 * @return true, if is expiry
  	 */
    public boolean isExpiry() {
        return expiryFlag;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
