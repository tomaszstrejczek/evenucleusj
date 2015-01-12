package com.evenucleus.client;

import com.beimin.eveapi.exception.ApiException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by tomeks on 2015-01-06.
 */
public interface IPilotService {
    public class Result {
        public List<PilotDTO> pilots;
        public List<Corporation> corporations;
        public org.joda.time.DateTime cachedUntil;
    }

    Result Get() throws SQLException, UserException, ApiException;
}
