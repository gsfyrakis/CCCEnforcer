package uk.ac.ncl.toolBo;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.ncl.dao.ToolManager;
import uk.ac.ncl.model.Tool;

public class ToolBoImpl implements ToolBo {
    @Autowired
    private ToolManager toolManager;

    @Override
    public void create(String name, String owner, Long size, String key) {
        Tool tool = new Tool(name, owner, size, new Date(), key);
        toolManager.store(tool);

    }

    @Override
    public void delete(String key) throws Exception {
        Tool tool = toolManager.query(key);
        if (tool != null) {
            toolManager.remove(tool);
        } else {
            throw new Exception("This tool does not exsist!");
        }

    }

    @Override
    public Tool query(String key) throws Exception {
        Tool tool = toolManager.query(key);
        if (tool != null) {
            return tool;
        } else {
            throw new Exception("This tool does not exsist!");
        }
    }
}
