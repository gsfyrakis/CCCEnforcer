package uk.ac.ncl.core;

import java.util.TimerTask;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.jboss.tm.usertx.client.ServerVMClientUserTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.ncl.resource.Resources;
import uk.ac.ncl.rop.Obligation;
import uk.ac.ncl.rop.Prohibition;
import uk.ac.ncl.rop.RopEntity;
import uk.ac.ncl.state.RopState;
import uk.ac.ncl.state.RopState.ObligationState;
import uk.ac.ncl.state.RopState.ProhibitionState;
import uk.ac.ncl.user.User;

@Stateful
@Transactional
public class Deadline extends TimerTask {
    private RopEntity<? extends RopState> rop;

    @Resource(mappedName = "java:jboss/TransactionManager")
    private TransactionManager transactionManager;

    private UserTransaction userTransaction;

    @PersistenceContext(unitName = "RopePU")
    private EntityManager em;

    public Deadline() {
        em = Resources.getEntityManager();
    }

    public Deadline(RopEntity<? extends RopState> rop) {
        this.rop = rop;
        em = Resources.getEntityManager();
        userTransaction = new ServerVMClientUserTransaction(com.arjuna.ats.jta.TransactionManager.transactionManager());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void run() {
        System.out.println("The deadline starts!");
        try {
            userTransaction.begin();
        } catch (NotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SystemException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        User user = new User("zhao", "belldandy", "PI", null, null, null);
        em.persist(user);
        em.flush();
        System.out.println("The obligation: " + " has been expired!");
        try {
            userTransaction.commit();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RollbackException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (HeuristicMixedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (HeuristicRollbackException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SystemException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*
		 * if (rop instanceof Obligation) { Obligation obligation = (Obligation)
		 * rop; if (obligation.getState() == ObligationState.imposed) {
		 * //Obligation obligationEntity = em.merge(obligation);
		 * //obligationEntity.setState(ObligationState.violated);
		 * //em.persist(obligation); User user = new User("zhao", "belldandy",
		 * "PI", null, null, null); userManager.store(user);
		 * System.out.println("The obligation: " + obligation.getName() +
		 * " has been expired!"); } } else if (rop instanceof Prohibition) {
		 * Prohibition prohibition = (Prohibition) rop; if
		 * (prohibition.getState() == ProhibitionState.imposed) {
		 * prohibition.setState(ProhibitionState.expired);
		 * 
		 * 
		 * System.out.println("The prohibition: " + prohibition.getName() +
		 * " has been expired!"); }
		 * 
		 * }
		 */
    }
}
