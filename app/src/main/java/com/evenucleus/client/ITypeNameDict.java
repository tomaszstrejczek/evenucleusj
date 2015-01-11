package com.evenucleus.client;

import com.beimin.eveapi.exception.ApiException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by tomeks on 2014-12-31.
 */
public interface ITypeNameDict {
    Map<Integer,String> GetById(Iterable<Integer> ids) throws SQLException, ApiException;
}
