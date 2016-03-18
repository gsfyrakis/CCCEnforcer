package uk.ac.ncl.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import uk.ac.ncl.model.Tool;
import uk.ac.ncl.resource.Resources;

public class ToolManagerImpl implements ToolManager {
    @PersistenceContext(unitName = "RopePU")
    private EntityManager em;

    public ToolManagerImpl() {
        em = Resources.getEntityManager();
    }

    @Override
    public void store(Tool tool) {
        em.persist(tool);
    }

    @Override
    public Tool query(String key) {
        try {
            final String jpaQlQuery = "SELECT e FROM "
                    + Tool.class.getSimpleName() + " e WHERE e.key=:key";
            Query q = em.createQuery(jpaQlQuery);
            q.setParameter("key", key);
            Tool tool = (Tool) q.getSingleResult();
            return tool;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void remove(Tool tool) {
        em.remove(tool);
    }

    @Override
    public boolean contain(String name) {
        try {
            final String jpaQlQuery = "SELECT e FROM "
                    + Tool.class.getSimpleName() + " e WHERE e.name=:name";
            Query q = em.createQuery(jpaQlQuery);
            q.setParameter("name", name);
            q.getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

	/*
    @Override
	public void lastReport(List<String> name, Date lastReport)
	{
		if (name != null)
		{
			for (String result : name)
			{
				if (this.check(result))
				{
					Project tool = this.query(result);
					tool.setLastReport(lastReport);
					em.persist(tool);
				}
			}
		}
	}
	*/
}
