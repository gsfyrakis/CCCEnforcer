package uk.ac.ncl.dao;

import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import uk.ac.ncl.user.User;
import uk.ac.ncl.event.Operation;
import uk.ac.ncl.resource.Resources;
import uk.ac.ncl.rop.Obligation;
import uk.ac.ncl.rop.Prohibition;
import uk.ac.ncl.rop.Right;

public class UserManagerImpl implements UserManager {
    @PersistenceContext(unitName = "RopePU")
    private EntityManager em;


    public UserManagerImpl() {
        em = Resources.getEntityManager();
    }

    @Override
    public void store(User user) {
        em.persist(user);
    }

    @Override
    public User query(String name) {
        try {
            final String jpaQlQuery = "SELECT e FROM "
                    + User.class.getSimpleName() + " e WHERE e.name=:name";
            Query q = em.createQuery(jpaQlQuery);
            q.setParameter("name", name);
            User user = (User) q.getSingleResult();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void remove(User user) {
        em.remove(user);

    }

    @Override
    public Right getRight(User user, Operation operation) {
        Set<Right> userRight = user.getRightSet();
        for (Right right : userRight) {
            Set<Operation> operationSet = right.getOperationSet();
            for (Operation result : operationSet) {
                if (result.contain(operation))
                    return right;
            }
        }
        return null;
    }

    @Override
    public Obligation getObligation(User user, Operation operation) {
        Set<Obligation> userObligation = user.getObligationSet();
        for (Obligation obligation : userObligation) {
            System.out.println("getObligation: obligation: " + obligation);
            Set<Operation> operationSet = obligation.getOperationSet();
            for (Operation result : operationSet) {
                System.out.println("getObligation: operation: " + result);
                if (result.contain(operation))
                    System.out.println("getObligation: return obligation: " + obligation);
                    return obligation;
            }
        }
        return null;
    }

    @Override
    public Prohibition getProhibition(User user, Operation operation) {
        Set<Prohibition> userProhibition = user.getProhibitionSet();
        for (Prohibition prohibition : userProhibition) {
            Set<Operation> operationSet = prohibition.getOperationSet();
            for (Operation result : operationSet) {
                if (result.contain(operation))
                    return prohibition;
            }
        }
        return null;
    }
}
