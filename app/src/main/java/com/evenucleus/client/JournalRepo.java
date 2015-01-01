package com.evenucleus.client;

import android.util.Log;

import com.beimin.eveapi.exception.ApiException;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by tomeks on 2014-12-31.
 */
public class JournalRepo implements IJournalRepo {

    DatabaseHelper _localdb;
    IPilotRepo _pilotRepo;
    ICorporationRepo _corporationRepo;
    IEveApiCaller _eveApiCaller;

    public JournalRepo(DatabaseHelper localdb, IPilotRepo pilotRepo, ICorporationRepo corporationRepo, IEveApiCaller eveApiCaller) {
        _localdb = localdb;
        _pilotRepo = pilotRepo;
        _corporationRepo = corporationRepo;
        _eveApiCaller = eveApiCaller;
    }
    @Override
    public void ReplicateFromEve() throws SQLException, ParseException, ApiException {
        Log.d(JournalRepo.class.getName(), "ReplicateFromEve");

        // For testing we allow null _pilotRepo
        if (_pilotRepo != null)
        {
            List<Pilot> pilots = _pilotRepo.GetAll();
            for(Pilot p:pilots)
                replicateForPilot(p);
        }

        // For testing we allow null _corporationRepo
        if (_corporationRepo == null)
            return;

        List<Corporation> corporations = _corporationRepo.GetAll();
        for(Corporation c:corporations)
            replicateForCorporation(c);
    }

    @Override
    public List<JournalEntry> GetAll() throws SQLException {
        return _localdb.getJournalEntryDao().queryForAll();
    }

    private void replicateForPilot(Pilot p) throws SQLException, ParseException, ApiException {
        Log.d(JournalRepo.class.getName(), String.format("Replicating for pilot %s", p.Name));

        QueryBuilder<JournalEntry, Integer> qb = _localdb.getJournalEntryDao().queryBuilder().selectRaw("MAX(refID)");
        qb.where().eq("PilotId", p.PilotId);
        GenericRawResults<String[]> results = _localdb.getJournalEntryDao().queryRaw(qb.prepareStatementString());
        String[] rs = results.getFirstResult();
        long lastStoredID = 0;
        if (rs != null && rs[0]!=null)
            lastStoredID = Long.parseLong(rs[0]);

        List<JournalEntry> list = _eveApiCaller.getJournalEntries(p.KeyInfo.KeyId, p.KeyInfo.VCode, p.CharacterId, p.PilotId, lastStoredID);
        for(JournalEntry x:list)
            _localdb.getJournalEntryDao().createOrUpdate(x);
    }

    private void replicateForCorporation(Corporation c) throws SQLException, ApiException {
        Log.d(JournalRepo.class.getName(), String.format("Replicating for corporation %s", c.Name));

        QueryBuilder<JournalEntry, Integer> qb = _localdb.getJournalEntryDao().queryBuilder().selectRaw("MAX(refID)");
        qb.where().eq("CorporationId", c.CorporationId);
        GenericRawResults<String[]> results = _localdb.getJournalEntryDao().queryRaw(qb.prepareStatementString());
        String[] rs = results.getFirstResult();
        long lastStoredID = 0;
        if (rs != null && rs[0]!=null)
            lastStoredID = Long.parseLong(rs[0]);

        List<JournalEntry> list = _eveApiCaller.getJournalEntriesCorpo(c.KeyInfo.KeyId, c.KeyInfo.VCode, c.CorporationId, lastStoredID);
        for(JournalEntry x:list)
            _localdb.getJournalEntryDao().createOrUpdate(x);
    }
}
