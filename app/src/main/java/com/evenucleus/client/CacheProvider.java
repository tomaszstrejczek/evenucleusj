package com.evenucleus.client;

import android.util.Log;

import com.beimin.eveapi.exception.ApiException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by tomeks on 2014-12-31.
 */
public class CacheProvider implements ICacheProvider {
    final Logger logger = LoggerFactory.getLogger(CacheProvider.class);


    DatabaseHelper _localdb;
    public CacheProvider(DatabaseHelper localdb) {
        _localdb = localdb;
    }

    @Override
    public <T> T Get(String key, ICacheValueProvider valueProvider, Class<T> clazz) throws SQLException, JSONException, ApiException {
        logger.debug("Get {}", key);

        if (valueProvider == null)
            throw new IllegalArgumentException("valueProvider");

        purgeCacheIfNeeded();

        String storeKey = "/CacheProvider/"+key;

        CacheEntry entry = _localdb.getCacheEntryDao().queryForId(storeKey);
        if ((entry != null) && (entry.CachedUntil.after(new Date())))
        {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.fromJson(entry.Data, clazz);
        }

        // Not cached - get the calue
        Map.Entry<Date,Object> value = valueProvider.Get();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String serialized = gson.toJson(value.getValue(), clazz);
        entry = new CacheEntry();
        entry.Key = storeKey;
        entry.CachedUntil = value.getKey();
        entry.Data = serialized;
        _localdb.getCacheEntryDao().createOrUpdate(entry);

        return (T) value.getValue();
    }

    private Date _recentPurgeDate = null;
    private void purgeCacheIfNeeded() {
        logger.debug("purgeCacheIfNeeded");

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, -12);

        if (_recentPurgeDate == null || _recentPurgeDate.before(cal.getTime()))
        {
            _localdb._database.execSQL("delete from CacheEntry where CachedUntil < DATETIME('now')");
            _recentPurgeDate = new Date();
        }
    }

}
