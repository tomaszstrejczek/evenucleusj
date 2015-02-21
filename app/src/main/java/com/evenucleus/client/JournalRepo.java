package com.evenucleus.client;

import android.util.Log;

import com.beimin.eveapi.exception.ApiException;
import com.evenucleus.evenucleus.MyDatabaseHelper;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

            StringBuilder sb = new StringBuilder();
            sb.append('(');
            for(Pilot p: pilots) {
                sb.append(String.valueOf(p.PilotId));
                sb.append(',');
            }
            sb.append("0)");

            _localdb.getJournalEntryDao().executeRaw("delete from journalentry where pilotid not in " + sb.toString());
        }

        // For testing we allow null _corporationRepo
        if (_corporationRepo == null)
            return;

        List<Corporation> corporations = _corporationRepo.GetAll();
        for(Corporation c:corporations)
            replicateForCorporation(c);

        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for(Corporation c: corporations) {
            sb.append(String.valueOf(c.CorporationId));
            sb.append(',');
        }
        sb.append("0)");

        _localdb.getJournalEntryDao().executeRaw("delete from journalentry where corporationid not in " + sb.toString());
    }

    @Override
    public List<JournalEntry> GetAll() throws SQLException {
        return _localdb.getJournalEntryDao().queryForAll();
    }

    @Override
    public void AssignCategory(int journalEntryId, String category) throws SQLException {
        logger.debug("AssignCategory {} {}", journalEntryId, category);

        JournalEntry je = _localdb.getJournalEntryDao().queryForId(journalEntryId);
        logger.debug("AssignCategoryInfo {} {}", je.refID, je.date);
        je.CategoryName = category;
        _localdb.getJournalEntryDao().createOrUpdate(je);
    }

    private void replicateForPilot(Pilot p) throws SQLException, ParseException, ApiException {
        logger.debug("Replicating for pilot {}", p.Name);

        List<JournalEntry> stored = _localdb.getJournalEntryDao().queryForEq("pilotid", p.PilotId);

        /*
        if (true || p.Name.equals("stryju")) {
            List<JournalEntry> dbg = new ArrayList<JournalEntry>();
            for(JournalEntry je:stored)
                if (je.date.getDate()==14 && je.amount < -1.5e9)
                    dbg.add(je);
            if (dbg.size()>0)
                logger.debug("done");
        }
        */

        List<JournalEntry> list = _eveApiCaller.getJournalEntries(p.KeyInfo.KeyId, p.KeyInfo.VCode, p.CharacterId, p.PilotId, 0);
        for(JournalEntry x:list)
            if (!stored.contains(x))
                _localdb.getJournalEntryDao().createOrUpdate(x);

        // removed duplicates from stored
        for(int i = stored.size()-1; i > 0; --i) {
            JournalEntry je = stored.get(i);
            stored.remove(i);
            int idx = stored.indexOf(je);
            if (idx >= 0) {
                JournalEntry duplicate = stored.get(idx);
                if (StringUtils.isEmpty(je.CategoryName) && StringUtils.isNotEmpty(duplicate.CategoryName)) {
                    je.CategoryName = duplicate.CategoryName;
                    _localdb.getJournalEntryDao().createOrUpdate(je);
                }
                _localdb.getJournalEntryDao().deleteById(duplicate.JournalEntryId);
            }
        }
    }

    private void replicateForCorporation(Corporation c) throws SQLException, ApiException {
        logger.debug("Replicating for corporation {}", c.Name);

        List<JournalEntry> stored = _localdb.getJournalEntryDao().queryForEq("CorporationId", c.CorporationId);

        List<JournalEntry> list = _eveApiCaller.getJournalEntriesCorpo(c.KeyInfo.KeyId, c.KeyInfo.VCode, c.CorporationId, 0);
        for(JournalEntry x:list)
            if (!stored.contains(x))
                _localdb.getJournalEntryDao().createOrUpdate(x);

        // removed duplicates from stored
        for(int i = stored.size()-1; i > 0; --i) {
            JournalEntry je = stored.get(i);
            stored.remove(i);
            int idx = stored.indexOf(je);
            if (idx >= 0) {
                JournalEntry duplicate = stored.get(idx);
                if (StringUtils.isEmpty(je.CategoryName) && StringUtils.isNotEmpty(duplicate.CategoryName)) {
                    je.CategoryName = duplicate.CategoryName;
                    _localdb.getJournalEntryDao().createOrUpdate(je);
                }
                _localdb.getJournalEntryDao().deleteById(duplicate.JournalEntryId);
            }
        }
    }
}
