package uk.ac.ncl.core;

import java.util.Date;
import java.util.TimerTask;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.tm.usertx.client.ServerVMClientUserTransaction;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.ncl.resource.Resources;
import uk.ac.ncl.rop.DeadlineInt;
import uk.ac.ncl.rop.RopEntity;
import uk.ac.ncl.state.RopState;

@Stateful
@Transactional
public class Deadline extends TimerTask implements DeadlineInt{
    private Date deadline;
    private RopEntity<? extends RopState> rop;

    private TimeKeeper timeKeeper = null;
//   	private RopEntity rop = null;
   	private String originator = null;
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
        em = Resources.getEntityManager();
        userTransaction = new ServerVMClientUserTransaction(com.arjuna.ats.jta.TransactionManager.transactionManager());
    }

    public Deadline(TimeKeeper timeKeeper, RopEntity rop, String originator, String responder, boolean b) {
        this.timeKeeper = timeKeeper;
        this.rop = rop;
        this.expiryFlag = expiryFlag;

    }

    public Deadline(TimeKeeper timeKeeper, RopEntity rop, String originator, String responder) {

        this.timeKeeper = timeKeeper;
        this.rop = rop;
        this.expiryFlag = true;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void run() {
        timeKeeper.deadlineCallback(rop, originator, responder, expiryFlag);

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


}
