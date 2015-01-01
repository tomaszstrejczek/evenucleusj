package com.evenucleus.client;

import android.util.Log;

import com.beimin.eveapi.exception.ApiException;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by tomeks on 2015-01-01.
 */
public class WalletRepo implements IWalletRepo {
    DatabaseHelper _localdb;
    IPilotRepo _pilotRepo;
    ICorporationRepo _corporationRepo;
    IEveApiCaller _eveApiCaller;

    public WalletRepo(DatabaseHelper localdb, IPilotRepo pilotRepo, ICorporationRepo corporationRepo, IEveApiCaller eveApiCaller) {
        _localdb = localdb;
        _pilotRepo = pilotRepo;
        _corporationRepo = corporationRepo;
        _eveApiCaller = eveApiCaller;
    }
    @Override
    public void ReplicateFromEve() throws SQLException, ParseException, ApiException {
        Log.d(WalletRepo.class.getName(), "ReplicateFromEve");

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

    private void replicateForPilot(Pilot p) throws SQLException, ParseException, ApiException {
        Log.d(WalletRepo.class.getName(), String.format("Replicating for pilot %s", p.Name));

        QueryBuilder<WalletTransaction, Integer> qb = _localdb.getWalletTransactionDao().queryBuilder().selectRaw("MAX(transactionID)");
        qb.where().eq("PilotId", p.PilotId);
        GenericRawResults<String[]> results = _localdb.getWalletTransactionDao().queryRaw(qb.prepareStatementString());
        String[] rs = results.getFirstResult();
        long lastStoredID = 0;
        if (rs != null && rs[0]!=null)
            lastStoredID = Long.parseLong(rs[0]);

        List<WalletTransaction> list = _eveApiCaller.getWalletTransactions(p.KeyInfo.KeyId, p.KeyInfo.VCode, p.CharacterId, p.PilotId, lastStoredID);
        for(WalletTransaction x:list)
            _localdb.getWalletTransactionDao().createOrUpdate(x);
    }

    private void replicateForCorporation(Corporation c) throws SQLException, ApiException {
        Log.d(WalletRepo.class.getName(), String.format("Replicating for corporation %s", c.Name));

        QueryBuilder<WalletTransaction, Integer> qb = _localdb.getWalletTransactionDao().queryBuilder().selectRaw("MAX(transactionID)");
        qb.where().eq("CorporationId", c.CorporationId);
        GenericRawResults<String[]> results = _localdb.getWalletTransactionDao().queryRaw(qb.prepareStatementString());
        String[] rs = results.getFirstResult();
        long lastStoredID = 0;
        if (rs != null && rs[0]!=null)
            lastStoredID = Long.parseLong(rs[0]);

        List<WalletTransaction> list = _eveApiCaller.getWalletTransactionsCorpo(c.KeyInfo.KeyId, c.KeyInfo.VCode, c.CorporationId, lastStoredID);
        for(WalletTransaction x:list)
            _localdb.getWalletTransactionDao().createOrUpdate(x);
    }
}
