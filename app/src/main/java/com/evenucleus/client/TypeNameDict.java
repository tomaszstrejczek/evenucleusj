package com.evenucleus.client;

import android.util.Log;

import com.beimin.eveapi.eve.typename.EveTypeName;
import com.beimin.eveapi.eve.typename.TypeNameParser;
import com.beimin.eveapi.eve.typename.TypeNameResponse;
import com.beimin.eveapi.exception.ApiException;
import com.evenucleus.evenucleus.MyDatabaseHelper;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomeks on 2014-12-31.
 */
@EBean
public class TypeNameDict implements ITypeNameDict {
    final Logger logger = LoggerFactory.getLogger(TypeNameDict.class);

    @Bean(MyDatabaseHelper.class)
    public DatabaseHelper _localdb;

    @Override
    public Map<Integer, String> GetById(Iterable<Integer> ids) throws SQLException, ApiException {
        logger.debug("GetById");

        Map<Integer, String> result = new HashMap<Integer, String>();

        // delete old entries to create space
        purgeCacheIfNeeded();

        // hit entries
        List<TypeNameEntry> hit = _localdb.getTypeNameEntryDao().queryBuilder().where().in("Key", ids).query();

        List<Integer> found = new ArrayList<Integer>();
        List<Integer> missing = new ArrayList<Integer>();
        for(TypeNameEntry t:hit) found.add(t.Key);
        for(Integer i:ids)
            if (!found.contains(i))
                missing.add(i);

        if (missing.size() > 0)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, 7);

            TypeNameParser parser = TypeNameParser.getInstance();
            TypeNameResponse r = parser.getResponse(missing);
            for(EveTypeName t: r.getAll())
            {
                TypeNameEntry entry = new TypeNameEntry();
                entry.Key = t.getTypeID();
                entry.CachedUntil = cal.getTime();
                entry.Data = t.getTypeName();
                hit.add(entry);
                _localdb.getTypeNameEntryDao().createOrUpdate(entry);
            }
        }

        for(TypeNameEntry t:hit)
            result.put(t.Key, t.Data);

        return result;
    }

    private Date _recentPurgeDate = null;
    private void purgeCacheIfNeeded() {
        logger.debug("purgeCacheIfNeeded");

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, -12);

        if (_recentPurgeDate == null || _recentPurgeDate.before(cal.getTime()))
        {
            _localdb._database.execSQL("delete from TypeNameEntry where CachedUntil < DATETIME('now')");
            _recentPurgeDate = new Date();
        }
    }

}
