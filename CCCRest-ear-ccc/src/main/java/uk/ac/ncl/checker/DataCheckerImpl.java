package uk.ac.ncl.checker;

import java.util.Date;
import java.util.List;
import java.util.Set;

import uk.ac.ncl.dao.DataManager;
import uk.ac.ncl.dao.DataManagerImpl;
import uk.ac.ncl.dao.ToolManager;
import uk.ac.ncl.dao.ToolManagerImpl;
import uk.ac.ncl.model.Data;

public class DataCheckerImpl implements DataChecker {
    private DataManager dm;

    private ToolManager tm;

    public DataCheckerImpl() {
        dm = new DataManagerImpl();
        tm = new ToolManagerImpl();
    }

    @Override
    public boolean checkDependency(String key) throws Exception {
        Data data = dm.query(key);
        if (data != null) {
            Set<String> rawData = data.getRawData();
            Set<String> tool = data.getTool();
            if (!rawData.isEmpty()) {
                for (String result : rawData) {
                    if (!dm.contain(result))
                        return false;
                }
            }
            if (!tool.isEmpty()) {
                for (String result : tool) {
                    if (!tm.contain(result))
                        return false;
                }
            }
            if (tool.isEmpty() && rawData.isEmpty()) {
                return true;
            }
            return true;
        } else {
            throw new Exception("This data does not exsist!");
        }
    }

    @Override
    public String getUser(String key) throws Exception {
        Data data = dm.query(key);
        if (data != null) {
            return data.getOwner();
        } else {
            throw new Exception("This data does not exsist!");
        }
    }

    @Override
    public Long getSize(String key) throws Exception {
        Data data = dm.query(key);
        if (data != null) {
            return data.getSize();
        } else {
            throw new Exception("This data does not exsist!");
        }
    }

    @Override
    public Date getLastVisit(String key) throws Exception {
        Data data = dm.query(key);
        if (data != null) {
            return data.getLastVisit();
        } else {
            throw new Exception("This data does not exsist!");
        }
    }

}
