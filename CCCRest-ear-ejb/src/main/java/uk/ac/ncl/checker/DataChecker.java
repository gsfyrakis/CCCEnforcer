package uk.ac.ncl.checker;

import java.util.Date;

/*
 * all the methods from this interface should be immutable
 */
public interface DataChecker {
    public Long getSize(String key) throws Exception;

    public String getUser(String key) throws Exception;

    public Date getLastVisit(String key) throws Exception;

    public boolean checkDependency(String key) throws Exception;
}
