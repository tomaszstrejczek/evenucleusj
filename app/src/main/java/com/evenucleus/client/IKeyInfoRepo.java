package com.evenucleus.client;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by tomeks on 2014-12-28.
 */
public interface IKeyInfoRepo {
        void AddKey(int keyid, String vcode) throws SQLException, UserException;
        void DeleteKey(int keyid) throws SQLException;
        List<KeyInfo> GetKeys() throws SQLException;
        KeyInfo GetById(int id) throws SQLException;
}
