package com.evenucleus.client;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by tomeks on 2014-12-28.
 */
public interface IPilotRepo {
    void Update(UserData data) throws SQLException;
    List<Pilot> GetAll() throws SQLException;
    void SetFreeManufacturingJobsNofificationCount(int pilotid, int value);
    void SetFreeResearchJobsNofificationCount(int pilotid, int value);
}
