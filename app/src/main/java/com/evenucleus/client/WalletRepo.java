package com.evenucleus.client;

import android.util.Log;

import com.beimin.eveapi.exception.ApiException;
import com.evenucleus.evenucleus.MyDatabaseHelper;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomeks on 2015-01-01.
 */
@EBean
public class WalletRepo implements IWalletRepo {
    final Logger logger = LoggerFactory.getLogger(WalletRepo.class);

    @Bean(MyDatabaseHelper.class)
    public DatabaseHelper _localdb;

    @Bean(PilotRepo.class)
    public IPilotRepo _pilotRepo;

    @Bean(CorporationRepo.class)
    public ICorporationRepo _corporationRepo;

    @Bean(EveApiCaller.class)
    public IEveApiCaller _eveApiCaller;

    @Override
    public void ReplicateFromEve() throws SQLException, ParseException, ApiException {
        logger.debug("ReplicateFromEve");

        // For testing we allow null _pilotRepo
        if (_pilotRepo != null)
        {
            List<Pilot> pilots = _pilotRepo.GetAll();
            for(Pilot p:pilots)
                replicateForPilot(p);
        }

        // For testing we allow null _corporationRepo
        if (_corporationRepo == null)
            return;

        List<Corporation> corporations = _corporationRepo.GetAll();
        for(Corporation c:corporations)
            replicateForCorporation(c);
    }

    @Override
    public List<WalletTransaction> GetAll() throws SQLException {
        return _localdb.getWalletTransactionDao().queryForAll();
    }

    private void replicateForPilot(Pilot p) throws SQLException, ParseException, ApiException {
        logger.debug("Replicating for pilot {}", p.Name);

        List<WalletTransaction> stored = _localdb.getWalletTransactionDao().queryForEq("pilotid", p.PilotId);

        List<WalletTransaction> list = _eveApiCaller.getWalletTransactions(p.KeyInfo.KeyId, p.KeyInfo.VCode, p.CharacterId, p.PilotId, 0);
        for(WalletTransaction x:list)
            if (!stored.contains(x))
                _localdb.getWalletTransactionDao().createOrUpdate(x);

        // removed duplicates from stored
        for(int i = stored.size()-1; i > 0; --i) {
            WalletTransaction je = stored.get(i);
            stored.remove(i);
            int idx = stored.indexOf(je);
            if (idx >= 0)
                _localdb.getWalletTransactionDao().deleteById(stored.get(idx).WalletTransactionId);
        }

    }

    private void replicateForCorporation(Corporation c) throws SQLException, ApiException {
        logger.debug("Replicating for corporation {}", c.Name);

        List<WalletTransaction> stored = _localdb.getWalletTransactionDao().queryForEq("CorporationID", c.CorporationId);

        List<WalletTransaction> list = _eveApiCaller.getWalletTransactionsCorpo(c.KeyInfo.KeyId, c.KeyInfo.VCode, c.CorporationId, 0);
        for(WalletTransaction x:list)
            if (!stored.contains(x))
                _localdb.getWalletTransactionDao().createOrUpdate(x);

        // removed duplicates from stored
        for(int i = stored.size()-1; i > 0; --i) {
            WalletTransaction je = stored.get(i);
            stored.remove(i);
            int idx = stored.indexOf(je);
            if (idx >= 0)
                _localdb.getWalletTransactionDao().deleteById(stored.get(idx).WalletTransactionId);
        }
    }
}
