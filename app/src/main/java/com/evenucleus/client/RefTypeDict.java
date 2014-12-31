package com.evenucleus.client;

import android.util.Log;

import com.beimin.eveapi.eve.reftypes.ApiRefType;
import com.beimin.eveapi.eve.reftypes.RefTypesParser;
import com.beimin.eveapi.eve.reftypes.RefTypesResponse;
import com.beimin.eveapi.exception.ApiException;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Map;

/**
 * Created by tomeks on 2014-12-31.
 */
public class RefTypeDict implements IRefTypeDict {

    ICacheProvider _cacheProvider;
    public RefTypeDict(ICacheProvider cacheProvider) {
        _cacheProvider = cacheProvider;
    }

    @Override
    public String GetById(int id) throws SQLException, JSONException, ApiException {
        Log.d(RefTypeDict.class.getName(), String.format("GetById %d", id));

        RefTypesResponse refTypes = _cacheProvider.Get("RefTypes", new ICacheValueProvider() {
            @Override
            public Map.Entry<Date, Object> Get() throws ApiException {
                RefTypesResponse response = RefTypesParser.getInstance().getResponse();
                return new AbstractMap.SimpleEntry<Date, Object>(response.getCachedUntil(), response);
            }
        }, RefTypesResponse.class);

        ApiRefType found = null;
        for(ApiRefType refType: refTypes.getAll())
            if (refType.getRefTypeID() == id) {
                found = refType;
                break;
            }
        if (found == null)
            return "<unknown>";
        else
            return found.getRefTypeName();
    }

    private class ValueProvider implements ICacheValueProvider {
        @Override
        public Map.Entry<Date, Object> Get() throws ApiException {
            RefTypesResponse response = RefTypesParser.getInstance().getResponse();
            return new AbstractMap.SimpleEntry<Date, Object>(response.getCachedUntil(), response);
        }
    }

}
