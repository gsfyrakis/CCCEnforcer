package uk.ac.ncl.dao;

import uk.ac.ncl.model.Data;

public interface DataManager {
    void store(Data date);

    Data query(String key);

    void remove(Data data);

    boolean contain(String name);
}

