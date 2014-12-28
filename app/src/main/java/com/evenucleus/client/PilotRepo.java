package com.evenucleus.client;

import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomeks on 2014-12-28.
 */
public class PilotRepo implements IPilotRepo {

    DatabaseHelper _localdb;
    public PilotRepo(DatabaseHelper localdb)
    {

    }


    @Override
    public void Update(UserData data) throws SQLException {
        Log.d(PilotRepo.class.getName(), "Update");

        List<String> validPilotNames = new ArrayList<String>();
        for(Pilot s:data.Pilots)
            validPilotNames.add(s.Name);

        // delete inactive pilots
        List<Pilot> storedPilots = _localdb.getPilotDao().queryForAll();
        List<Pilot> inactive = new ArrayList<Pilot>();
        for(Pilot s:storedPilots)
            if (!validPilotNames.contains(s.Name))
                inactive.add(s);
        for(Pilot s:inactive)
        {
            Log.d(PilotRepo.class.getName(), String.format("Removing pilot %s", s.Name));
            _localdb.getPilotDao().deleteById(s.PilotId);
        }

        // add new pilots
        List<Pilot> toadd = new ArrayList<Pilot>();
        List<String> storedPilotNames = new ArrayList<String>();
        for(Pilot s:storedPilots)
            storedPilotNames.add(s.Name);
        for(Pilot s:data.Pilots)
            if (!storedPilotNames.contains(s.Name))
                toadd.add(s);
        for(Pilot s:toadd)
            _localdb.getPilotDao().createOrUpdate(s);
    }

    @Override
    public List<Pilot> GetAll() throws SQLException {
        Log.d(PilotRepo.class.getName(), "GetAll");

        return _localdb.getPilotDao().queryForAll();
    }

    @Override
    public void SetFreeManufacturingJobsNofificationCount(int pilotid, int value) {
        Log.d(PilotRepo.class.getName(), String.format("SetFreeManufacturingJobsNofificationCount %d %d", pilotid, value));
        _localdb._database.rawQuery("update pilot set FreeManufacturingJobsNofificationCount=? where PilotId=?", new String[] {Integer.toString(value), Integer.toString(pilotid)});
    }

    @Override
    public void SetFreeResearchJobsNofificationCount(int pilotid, int value) {
        Log.d(PilotRepo.class.getName(), String.format("SetFreeResearchJobsNofificationCount %d %d", pilotid, value));
        _localdb._database.rawQuery("update pilot set FreeResearchJobsNofificationCount=? where PilotId=?", new String[] {Integer.toString(value), Integer.toString(pilotid)});
    }
}
