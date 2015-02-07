package com.evenucleus.client;

import android.util.Log;

import com.beimin.eveapi.EveApi;
import com.beimin.eveapi.account.characters.EveCharacter;
import com.beimin.eveapi.exception.ApiException;
import com.evenucleus.evenucleus.MyDatabaseHelper;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by tomeks on 2014-12-28.
 */
@EBean
public class PilotRepo implements IPilotRepo {
    final Logger logger = LoggerFactory.getLogger(PilotRepo.class);

    @Bean(MyDatabaseHelper.class)
    public DatabaseHelper _localdb;

    @Bean(EveApiCaller.class)
    public IEveApiCaller _eveApiCaller;

    @Override
    public void SimpleUpdateFromKey(int keyid, String vcode) throws ApiException, SQLException {
        List<Pilot> storedPilots = _localdb.getPilotDao().queryForAll();
        KeyInfo k = _localdb.getKeyInfoDao().queryForId(keyid);
        List<String> names = new ArrayList<String>();
        for(Pilot p:storedPilots) names.add(p.Name);

        Set<EveCharacter> characters = _eveApiCaller.getCharacters(keyid, vcode);
        for(EveCharacter c:characters) {
            if (!names.contains(c.getName()))
            {
                Pilot p = new Pilot();
                p.Name = c.getName();
                p.KeyInfo = k;
                p.CharacterId = c.getCharacterID();
                _localdb.getPilotDao().createOrUpdate(p);
            }
        }
    }

    @Override
    public void Update(UserData data) throws SQLException {
        logger.debug("Update");

        List<String> validPilotNames = new ArrayList<String>();
        for(PilotDTO s:data.Pilots)
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

        // add & update new pilots
        List<Pilot> toadd = new ArrayList<Pilot>();
        for(PilotDTO s:data.Pilots)
        {
            Pilot p = null;
            // Update id if this is an update
            for(Pilot ps:storedPilots)
                if (ps.Name.equals(s.Name))
                {
                    p = ps;
                    break;
                }
            // Add if nothing to update
            if (p == null) p = new Pilot();
            p.setFromPilotDTO(s);

            toadd.add(p);
        }

        for(Pilot s:toadd) {
            _localdb.getPilotDao().createOrUpdate(s);
            for(PilotDTO dto: data.Pilots) if (dto.Name.equals(s.Name)) dto.PilotId = s.PilotId;
        }
    }

    @Override
    public List<Pilot> GetAll() throws SQLException {
        logger.debug("GetAll");

        return _localdb.getPilotDao().queryForAll();
    }

    @Override
    public void SetFreeManufacturingJobsNofificationCount(int pilotid, int value) {
        logger.debug("SetFreeManufacturingJobsNofificationCount {} {}", pilotid, value);
        _localdb._database.execSQL("update pilot set FreeManufacturingJobsNofificationCount=? where PilotId=?", new String[] {Integer.toString(value), Integer.toString(pilotid)});
    }

    @Override
    public void SetFreeResearchJobsNofificationCount(int pilotid, int value) {
        logger.debug("SetFreeResearchJobsNofificationCount {} {}", pilotid, value);
        _localdb._database.execSQL("update pilot set FreeResearchJobsNofificationCount=? where PilotId=?", new String[] {Integer.toString(value), Integer.toString(pilotid)});
    }
}
