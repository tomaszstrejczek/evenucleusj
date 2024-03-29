package com.evenucleus.client;

import android.util.Log;

import com.beimin.eveapi.exception.ApiException;
import com.evenucleus.evenucleus.MyDatabaseHelper;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.stmt.QueryBuilder;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tomeks on 2014-12-31.
 */
@EBean
public class JournalRepo implements IJournalRepo {
    final Logger logger = LoggerFactory.getLogger(JournalRepo.class);

    @Bean(MyDatabaseHelper.class)
    public DatabaseHelper _localdb;

    @Bean(PilotRepo.class)
    public IPilotRepo _pilotRepo;

    @Bean(CorporationRepo.class)
    public ICorporationRepo _corporationRepo;

    @Bean(EveApiCaller.class)
    public IEveApiCaller _eveApiCaller;

    @Override
    public void ReplicateFromEve() throws SQLException, ParseException, ApiException {
        logger.debug("ReplicateFromEve");

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

    @Override
    public void AssignCategory(long journalEntryId, String category) throws SQLException {
        logger.debug("AssignCategory {} {}", journalEntryId, category);

        JournalEntry je = _localdb.getJournalEntryDao().queryForId(journalEntryId);
        logger.debug("AssignCategoryInfo {} {}", je.refID, je.date);
        je.CategoryName = category;
        _localdb.getJournalEntryDao().createOrUpdate(je);
    }

    private Set<Long> getAllIds() throws SQLException {
        Set<Long> ids = new HashSet<Long>();
        QueryBuilder<JournalEntry, Long> qb = _localdb.getJournalEntryDao().queryBuilder().selectColumns("refID");
        GenericRawResults<String[]> results = _localdb.getJournalEntryDao().queryRaw(qb.prepareStatementString());
        for(String[] v: results) {
            if (v!=null && v.length>0) {
                long id = Long.parseLong(v[0]);
                ids.add(id);
            }
        }

        return ids;
    }
    private void replicateForPilot(Pilot p) throws SQLException, ParseException, ApiException {
        logger.debug("Replicating for pilot {}", p.Name);

        long lastStoredID = 0;

        Set<Long> ids = getAllIds();
/*
        QueryBuilder<JournalEntry, Long> qb = _localdb.getJournalEntryDao().queryBuilder().selectRaw("MAX(refID)");
        qb.where().eq("PilotId", p.PilotId);
        GenericRawResults<String[]> results = _localdb.getJournalEntryDao().queryRaw(qb.prepareStatementString());
        String[] rs = results.getFirstResult();
        if (rs != null && rs[0]!=null)
            lastStoredID = Long.parseLong(rs[0]);
*/

        List<JournalEntry> list = _eveApiCaller.getJournalEntries(p.KeyInfo.KeyId, p.KeyInfo.VCode, p.CharacterId, p.PilotId, lastStoredID);

        if (p.Name.equals("MicioGatto") || p.Name.equals("stryju")) {
            for(JournalEntry je:list) {
                if (je.refID == 10756311108l)
                    logger.debug("ala");
            }

        }

        List<JournalEntry> all = _localdb.getJournalEntryDao().queryForAll();
        _localdb.getJournalEntryDao().executeRaw("delete from journalentry where pilotid=?", String.valueOf(p.PilotId));
        for(JournalEntry eve:list) {
            boolean found = false;
            for(JournalEntry stored:all) {
                if (eve.refID==stored.refID) {
                    found = true;
                    eve.CategoryName = stored.CategoryName;
                    _localdb.getJournalEntryDao().deleteById(stored.refID);
                    _localdb.getJournalEntryDao().createOrUpdate(eve);
                }
            }
            if (!found)
                _localdb.getJournalEntryDao().createOrUpdate(eve);
        }
        return;
/*
        for(JournalEntry x:list)
            if (!ids.contains(x.refID)) {
                _localdb.getJournalEntryDao().createOrUpdate(x);
            }
*/
    }

    private void replicateForCorporation(Corporation c) throws SQLException, ApiException {
        logger.debug("Replicating for corporation {}", c.Name);

        long lastStoredID = 0;

        Set<Long> ids = getAllIds();

/*
        QueryBuilder<JournalEntry, Long> qb = _localdb.getJournalEntryDao().queryBuilder().selectRaw("MAX(refID)");
        qb.where().eq("CorporationId", c.CorporationId);
        GenericRawResults<String[]> results = _localdb.getJournalEntryDao().queryRaw(qb.prepareStatementString());
        String[] rs = results.getFirstResult();
        if (rs != null && rs[0]!=null)
            lastStoredID = Long.parseLong(rs[0]);
*/
        List<JournalEntry> list = _eveApiCaller.getJournalEntriesCorpo(c.KeyInfo.KeyId, c.KeyInfo.VCode, c.CorporationId, lastStoredID);

        List<JournalEntry> all = _localdb.getJournalEntryDao().queryForAll();
        _localdb.getJournalEntryDao().executeRaw("delete from journalentry where CorporationId=?", String.valueOf(c.CorporationId));

        for(JournalEntry eve:list) {
            boolean found = false;
            for(JournalEntry stored:all) {
                if (eve.refID==stored.refID) {
                    found = true;
                    eve.CategoryName = stored.CategoryName;
                    _localdb.getJournalEntryDao().deleteById(stored.refID);
                    _localdb.getJournalEntryDao().createOrUpdate(eve);
                }
            }
            if (!found)
                _localdb.getJournalEntryDao().createOrUpdate(eve);
        }
        return;

/*
        for(JournalEntry x:list)
            if (!ids.contains(x.refID))
                _localdb.getJournalEntryDao().createOrUpdate(x);
*/
    }
}
