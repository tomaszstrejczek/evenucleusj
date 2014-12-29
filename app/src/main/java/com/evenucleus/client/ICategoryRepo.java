package com.evenucleus.client;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by tomeks on 2014-12-29.
 */
public interface ICategoryRepo {
    void AddCategory(String name) throws SQLException, UserException;
    List<String> Get() throws SQLException;
}
