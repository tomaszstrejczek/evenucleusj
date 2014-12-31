package com.evenucleus.client;

import org.json.JSONException;

import java.sql.SQLException;

/**
 * Created by tomeks on 2014-12-31.
 */
public interface ICacheProvider {
    <T> T Get(String key, ICacheValueProvider valueProvider, Class<T> clazz) throws SQLException, JSONException;
}
