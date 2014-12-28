package com.evenucleus.client;

import android.util.Log;

import com.evenucleus.evenucleus.R;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by tomeks on 2014-12-28.
 */
public class KeyInfoRepo implements IKeyInfoRepo {

    DatabaseHelper _localdb;
    IStringProvider _stringProvider;
    public KeyInfoRepo(DatabaseHelper localdb, IStringProvider stringProvider)
    {
        _localdb = localdb;
        _stringProvider = stringProvider;
    }


    @Override
    public void AddKey(final int keyid, final String vcode) throws SQLException, UserException {
        Log.d(KeyInfoRepo.class.getName(), String.format("AddKey %d", keyid));
        KeyInfo existing = _localdb.getKeyInfoDao().queryForId(keyid);
        if (existing!=null)
        {
            Log.d(KeyInfoRepo.class.getName(), "Errors.ErrorKeyAlreadyDefined");
            throw new UserException(_stringProvider.Get(R.string.ErrorKeyAlreadyDefined));
        }

        KeyInfo rec = new KeyInfo() {{KeyId = keyid; VCode = vcode;}};
        _localdb.getKeyInfoDao().createOrUpdate(rec);
    }

    @Override
    public void DeleteKey(int keyid) throws SQLException {
        Log.d(KeyInfoRepo.class.getName(), String.format("DeleteKey %d", keyid));

        _localdb.getKeyInfoDao().deleteById(keyid);
    }

    @Override
    public List<KeyInfo> GetKeys() throws SQLException {
        Log.d(KeyInfoRepo.class.getName(), "GetKeys");

        return _localdb.getKeyInfoDao().queryForAll();
    }

    @Override
    public KeyInfo GetById(int id) throws SQLException {
        Log.d(KeyInfoRepo.class.getName(), String.format("GetById %d", id));

        KeyInfo existing = _localdb.getKeyInfoDao().queryForId(id);
        return existing;
    }
}
