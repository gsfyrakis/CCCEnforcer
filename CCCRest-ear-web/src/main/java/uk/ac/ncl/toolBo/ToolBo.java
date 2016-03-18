package uk.ac.ncl.toolBo;

import uk.ac.ncl.model.Tool;

public interface ToolBo {
    void create(String name, String owner, Long size, String key);

    void delete(String key) throws Exception;

    Tool query(String key) throws Exception;
}

