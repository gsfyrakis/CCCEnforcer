package uk.ac.ncl.dataBo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.ncl.dao.DataManager;
import uk.ac.ncl.model.Data;

public class DataBoImpl implements DataBo {
    @Autowired
    private DataManager dataManager;

    private final String DATA = "data";

    private final String TOOL = "tool";

    @Override
    public void create(String name, String username, Long size, String key)
            throws Exception {
        try {
            Data data = new Data(name, username, size, null, new Date(), key,
                    null, null);
            dataManager.store(data);
        } catch (Exception e) {
            throw new Exception("Creating data fails!");
        }
    }

    @Override
    public void delete(String key) throws Exception {
        Data data = dataManager.query(key);
        if (data != null) {
            dataManager.remove(data);
        } else {
            throw new Exception("This data does not exsist!");
        }
    }

    @Override
    public void update(String key, Date publish) throws Exception {
        Data data = dataManager.query(key);
        if (data != null) {
            data.setPublish(publish);
        } else {
            throw new Exception("This data does not exsist!");
        }
    }

    @Override
    public void addDependency(String key, String type, String dependency)
            throws Exception {
        Data data = dataManager.query(key);
        if (data != null) {
            if (type == DATA) {
                Set<String> rawData = data.getRawData();
                if (rawData == null) {
                    rawData = new HashSet<String>();
                }
                rawData.add(dependency);
                data.setRawData(rawData);
            } else if (type == TOOL) {
                Set<String> tool = data.getTool();
                if (tool == null) {
                    tool = new HashSet<String>();
                }
                tool.add(dependency);
                data.setTool(tool);
            } else {
                throw new Exception("This type does not exsist!");
            }
        } else {
            throw new Exception("This data does not exsist!");
        }

    }

    @Override
    public Data query(String key) throws Exception {
        Data data = dataManager.query(key);
        if (data != null) {
            return data;
        } else {
            throw new Exception("The data does not exist!");
        }

    }

}
