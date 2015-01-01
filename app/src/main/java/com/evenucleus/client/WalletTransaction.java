package com.evenucleus.client;

import com.beimin.eveapi.shared.wallet.transactions.ApiWalletTransaction;
import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by tomeks on 2015-01-01.
 */
public class WalletTransaction {
    public WalletTransaction() {

    }
    public WalletTransaction(ApiWalletTransaction entry) {
        transactionDateTime = entry.getTransactionDateTime();
        transactionID = entry.getTransactionID();
        quantity = entry.getQuantity();
        typeName = entry.getTypeName();
        typeID = entry.getTypeID();
        price = entry.getPrice();
        clientID = entry.getClientID();
        clientName = entry.getClientName();
        characterID = entry.getCharacterID();
        characterName = entry.getCharacterName();
        stationID = entry.getStationID();
        stationName = entry.getStationName();
        transactionType = entry.getTransactionType();
        transactionFor = entry.getTransactionFor();
    }

    @DatabaseField(generatedId = true)
    public int WalletTransactionId;

    @DatabaseField
    public int PilotId;
    @DatabaseField
    public int CorporationId;
    @DatabaseField
    public int CategoryId;

    @DatabaseField
    public Date transactionDateTime;
    @DatabaseField
    public long transactionID;
    @DatabaseField
    public int quantity;
    @DatabaseField
    public String typeName;
    @DatabaseField
    public int typeID;
    @DatabaseField
    public double price;
    @DatabaseField
    public long clientID;
    @DatabaseField
    public String clientName;
    @DatabaseField
    public Long characterID;
    @DatabaseField
    public String characterName;
    @DatabaseField
    public int stationID;
    @DatabaseField
    public String stationName;
    @DatabaseField
    public String transactionType;
    @DatabaseField
    public String transactionFor;
}
