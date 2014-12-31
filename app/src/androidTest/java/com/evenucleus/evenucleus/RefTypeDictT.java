package com.evenucleus.evenucleus;

import com.beimin.eveapi.exception.ApiException;
import com.evenucleus.client.CacheProvider;
import com.evenucleus.client.RefTypeDict;

import junit.framework.Assert;

import org.json.JSONException;

import java.sql.SQLException;

/**
 * Created by tomeks on 2014-12-31.
 */
public class RefTypeDictT extends TestBase {
    public void test_Basic() throws SQLException, JSONException, ApiException {
        CacheProvider cacheProvider = new CacheProvider(_localdb);
        RefTypeDict dict = new RefTypeDict(cacheProvider);

        String result = dict.GetById(10);
        Assert.assertEquals("Player Donation", result);

        result = dict.GetById(-1);
        Assert.assertEquals("<unknown>", result);
    }
}
