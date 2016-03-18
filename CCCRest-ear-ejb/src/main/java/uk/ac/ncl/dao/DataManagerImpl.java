package uk.ac.ncl.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import uk.ac.ncl.model.Data;
import uk.ac.ncl.resource.Resources;

public class DataManagerImpl implements DataManager {
    @PersistenceContext(unitName = "RopePU")
    private EntityManager em;

    public DataManagerImpl() {
        em = Resources.getEntityManager();
    }


    @Override
    public void store(Data data) {
        em.persist(data);
    }

    @Override
    public void remove(Data data) {
        em.remove(data);
    }

    @Override
    public Data query(String key) {
        try {
            final String jpaQlQuery = "SELECT e FROM " + Data.class.getSimpleName() + " e WHERE e.key=:key";
            Query q = em.createQuery(jpaQlQuery);
            q.setParameter("key", key);
            Data data = (Data) q.getSingleResult();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public boolean contain(String name) {
        try {
            final String jpaQlQuery = "SELECT e FROM " + Data.class.getSimpleName() + " e WHERE e.name=:name";
            Query q = em.createQuery(jpaQlQuery);
            q.setParameter("name", name);
            q.getSingleResult();
            return true;
        } catch (javax.persistence.NoResultException e) {
            return false;
        }
    }
}
