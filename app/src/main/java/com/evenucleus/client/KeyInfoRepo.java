package com.evenucleus.client;

import android.util.Log;

import com.evenucleus.evenucleus.MyDatabaseHelper;
import com.evenucleus.evenucleus.R;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by tomeks on 2014-12-28.
 */
@EBean
public class KeyInfoRepo implements IKeyInfoRepo {
    final Logger logger = LoggerFactory.getLogger(KeyInfoRepo.class);

    @Bean(MyDatabaseHelper.class)
    public DatabaseHelper _localdb;
    @Bean(StringProvider.class)
    public IStringProvider _stringProvider;

    @Override
    public void AddKey(final int keyid, final String vcode) throws SQLException, UserException {
        logger.debug("AddKey {}", keyid);
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
        logger.debug("DeleteKey {}", keyid);

        _localdb.getKeyInfoDao().deleteById(keyid);
    }

    @Override
    public List<KeyInfo> GetKeys() throws SQLException {
        logger.debug("GetKeys");

        return _localdb.getKeyInfoDao().queryForAll();
    }

    @Override
    public KeyInfo GetById(int id) throws SQLException {
        logger.debug("GetById {}", id);

        KeyInfo existing = _localdb.getKeyInfoDao().queryForId(id);
        return existing;
    }
}
