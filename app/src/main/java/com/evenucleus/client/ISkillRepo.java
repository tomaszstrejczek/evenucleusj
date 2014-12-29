package com.evenucleus.client;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by tomeks on 2014-12-29.
 */
public interface ISkillRepo {
    void Update(UserData data) throws SQLException;
    List<String> GetForPilot(int pilotid) throws SQLException;
}
