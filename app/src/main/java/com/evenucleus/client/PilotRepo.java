package com.evenucleus.client;

import android.util.Log;

import com.beimin.eveapi.EveApi;
import com.beimin.eveapi.account.characters.EveCharacter;
import com.beimin.eveapi.exception.ApiException;
import com.evenucleus.evenucleus.MyDatabaseHelper;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by tomeks on 2014-12-28.
 */
@EBean
public class PilotRepo implements IPilotRepo {

    @Bean(MyDatabaseHelper.class)
    public DatabaseHelper _localdb;

    @Bean(EveApiCaller.class)
    public IEveApiCaller _eveApiCaller;

    @Override
    public void SimpleUpdateFromKey(int keyid, String vcode) throws ApiException, SQLException {
        List<Pilot> storedPilots = _localdb.getPilotDao().queryForAll();
        List<String> names = new ArrayList<String>();
        for(Pilot p:storedPilots) names.add(p.Name);

        Set<EveCharacter> characters = _eveApiCaller.getCharacters(keyid, vcode);
        for(EveCharacter c:characters) {
            if (!names.contains(c.getName()))
            {
                Pilot p = new Pilot();
                p.Name = c.getName();
                p.CharacterId = c.getCharacterID();
                _localdb.getPilotDao().createOrUpdate(p);
            }
        }
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
        _localdb._database.execSQL("update pilot set FreeManufacturingJobsNofificationCount=? where PilotId=?", new String[] {Integer.toString(value), Integer.toString(pilotid)});
    }

    @Override
    public void SetFreeResearchJobsNofificationCount(int pilotid, int value) {
        Log.d(PilotRepo.class.getName(), String.format("SetFreeResearchJobsNofificationCount %d %d", pilotid, value));
        _localdb._database.execSQL("update pilot set FreeResearchJobsNofificationCount=? where PilotId=?", new String[] {Integer.toString(value), Integer.toString(pilotid)});
    }
}
