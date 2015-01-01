package com.evenucleus.client;

import com.beimin.eveapi.exception.ApiException;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by tomeks on 2015-01-01.
 */
public interface IWalletRepo {
    void ReplicateFromEve() throws SQLException, ParseException, ApiException;
    List<WalletTransaction> GetAll() throws SQLException;
}
