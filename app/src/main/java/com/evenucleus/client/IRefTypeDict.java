package com.evenucleus.client;

import com.beimin.eveapi.exception.ApiException;

import org.json.JSONException;

import java.sql.SQLException;

/**
 * Created by tomeks on 2014-12-31.
 */
public interface IRefTypeDict {
    String GetById(int id) throws SQLException, JSONException, ApiException;
}
