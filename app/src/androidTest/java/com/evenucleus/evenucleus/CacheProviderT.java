package com.evenucleus.evenucleus;

import com.beimin.eveapi.exception.ApiException;
import com.evenucleus.client.CacheProvider;
import com.evenucleus.client.ICacheValueProvider;

import junit.framework.Assert;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static java.util.Map.Entry;

/**
 * Created by tomeks on 2014-12-31.
 */
public class CacheProviderT extends TestBase{
    public void test_AddKey() throws SQLException, JSONException, ApiException {
        CacheProvider cache = new CacheProvider(_localdb);

        ICacheValueProvider provider = new ICacheValueProvider() {
            @Override
            public Entry<Date, Object> Get() {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, 12);
                return new AbstractMap.SimpleEntry<Date, Object>(cal.getTime(), "ala ma kota");
            }
        };

        String result = cache.Get("key", provider, String.class);
        Assert.assertEquals("ala ma kota", result);

        ICacheValueProvider provider2 = new ICacheValueProvider() {
            @Override
            public Entry<Date, Object> Get() {
                assert false; // should not be called
                return null;
            }
        };

        result = cache.Get("key", provider2, String.class);
        Assert.assertEquals("ala ma kota", result);
    }

    public void test_AddKeyExpired() throws SQLException, JSONException, InterruptedException, ApiException {
        CacheProvider cache = new CacheProvider(_localdb);

        ICacheValueProvider provider = new ICacheValueProvider() {
            @Override
            public Entry<Date, Object> Get() {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MILLISECOND, 10);
                return new AbstractMap.SimpleEntry<Date, Object>(cal.getTime(), "ala ma kota");
            }
        };
        String result = cache.Get("key", provider, String.class);
        Assert.assertEquals("ala ma kota", result);

        Thread.sleep(50);
        ICacheValueProvider provider2 = new ICacheValueProvider() {
            @Override
            public Entry<Date, Object> Get() {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MILLISECOND, 10);
                return new AbstractMap.SimpleEntry<Date, Object>(cal.getTime(), "kot ma kota");
            }
        };
        result = cache.Get("key", provider2, String.class);
        Assert.assertEquals("kot ma kota", result);

    }
}
