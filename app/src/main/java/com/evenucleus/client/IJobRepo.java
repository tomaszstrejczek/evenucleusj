package com.evenucleus.client;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by tomeks on 2014-12-29.
 */
public interface IJobRepo {
    void Update(UserData data) throws SQLException;
    List<Job> GetAll() throws SQLException;

}
