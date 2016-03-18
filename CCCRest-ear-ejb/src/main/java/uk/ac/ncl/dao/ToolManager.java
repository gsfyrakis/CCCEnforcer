package uk.ac.ncl.dao;

import uk.ac.ncl.model.Tool;


public interface ToolManager {
    void store(Tool tool);

    Tool query(String key);

    void remove(Tool tool);

    boolean contain(String name);

}
