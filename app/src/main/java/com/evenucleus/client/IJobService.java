package com.evenucleus.client;

import com.beimin.eveapi.exception.ApiException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;

/**
 * Created by tomeks on 2015-01-11.
 */
public interface IJobService {
    public class Result {
        public List<Job> jobs;
        public DateTime cachedUntil;
    }

    Result Get() throws SQLException, ApiException;
}
