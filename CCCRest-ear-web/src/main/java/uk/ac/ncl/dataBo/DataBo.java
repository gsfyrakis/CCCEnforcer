package uk.ac.ncl.dataBo;

import java.util.Date;

import uk.ac.ncl.model.Data;

public interface DataBo {
    void create(String name, String username, Long size, String key) throws Exception;

    Data query(String key) throws Exception;

    void delete(String key) throws Exception;

    void update(String key, Date publish) throws Exception;

    void addDependency(String key, String type, String dependency) throws Exception;
}
