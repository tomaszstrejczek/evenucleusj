package com.evenucleus.client;

import android.util.Log;

import com.beimin.eveapi.exception.ApiException;
import com.evenucleus.evenucleus.MyDatabaseHelper;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    private Set<Long> getAllIds() throws SQLException {
        Set<Long> ids = new HashSet<Long>();
        QueryBuilder<WalletTransaction, Long> qb = _localdb.getWalletTransactionDao().queryBuilder().selectColumns("transactionID");
        GenericRawResults<String[]> results = _localdb.getWalletTransactionDao().queryRaw(qb.prepareStatementString());
        for(String[] v: results) {
            if (v!=null && v.length>0) {
                long id = Long.parseLong(v[0]);
                ids.add(id);
            }
        }

        return ids;
    }

    private void replicateForPilot(Pilot p) throws SQLException, ParseException, ApiException {
        logger.debug("Replicating for pilot {}", p.Name);

        long lastStoredID = 0;
        Set<Long> ids = getAllIds();


/*
        QueryBuilder<WalletTransaction, Long> qb = _localdb.getWalletTransactionDao().queryBuilder().selectRaw("MAX(transactionID)");
        qb.where().eq("PilotId", p.PilotId);
        GenericRawResults<String[]> results = _localdb.getWalletTransactionDao().queryRaw(qb.prepareStatementString());
        String[] rs = results.getFirstResult();
        if (rs != null && rs[0]!=null)
            lastStoredID = Long.parseLong(rs[0]);
*/
        List<WalletTransaction> list = _eveApiCaller.getWalletTransactions(p.KeyInfo.KeyId, p.KeyInfo.VCode, p.CharacterId, p.PilotId, lastStoredID);


        List<WalletTransaction> all = _localdb.getWalletTransactionDao().queryForAll();
        _localdb.getWalletTransactionDao().executeRaw("delete from walettransaction where pilotid=?", String.valueOf(p.PilotId));
        for(WalletTransaction eve:list) {
            _localdb.getWalletTransactionDao().createOrUpdate(eve);
        }
        return;
/*
        for(WalletTransaction x:list)
            if (!ids.contains(x.transactionID))
                _localdb.getWalletTransactionDao().createOrUpdate(x);
*/
    }

    private void replicateForCorporation(Corporation c) throws SQLException, ApiException {
        logger.debug("Replicating for corporation {}", c.Name);

        long lastStoredID = 0;
        Set<Long> ids = getAllIds();

/*
        QueryBuilder<WalletTransaction, Long> qb = _localdb.getWalletTransactionDao().queryBuilder().selectRaw("MAX(transactionID)");
        qb.where().eq("CorporationId", c.CorporationId);
        GenericRawResults<String[]> results = _localdb.getWalletTransactionDao().queryRaw(qb.prepareStatementString());
        String[] rs = results.getFirstResult();
        long lastStoredID = 0;
        if (rs != null && rs[0]!=null)
            lastStoredID = Long.parseLong(rs[0]);
*/
        List<WalletTransaction> list = _eveApiCaller.getWalletTransactionsCorpo(c.KeyInfo.KeyId, c.KeyInfo.VCode, c.CorporationId, lastStoredID);

        List<WalletTransaction> all = _localdb.getWalletTransactionDao().queryForAll();
        _localdb.getWalletTransactionDao().executeRaw("delete from walettransaction where CorporationID=?", String.valueOf(c.CorporationId));
        for(WalletTransaction eve:list) {
            _localdb.getWalletTransactionDao().createOrUpdate(eve);
        }
        return;
/*
        for(WalletTransaction x:list)
            if (!ids.contains(x.transactionID))
                _localdb.getWalletTransactionDao().createOrUpdate(x);
*/
    }
}
