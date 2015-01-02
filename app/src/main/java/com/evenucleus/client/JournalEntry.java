package com.evenucleus.client;

import com.beimin.eveapi.shared.wallet.RefType;
import com.beimin.eveapi.shared.wallet.journal.ApiJournalEntry;
import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by tomeks on 2014-12-31.
 */
public class JournalEntry {
    public JournalEntry() {
    }

    public JournalEntry(ApiJournalEntry entry) {
        date = entry.getDate();
        refID = entry.getRefID();
        refTypeID = entry.getRefTypeID();
        ownerName1 = entry.getOwnerName1();
        ownerID1 = entry.getOwnerID1();
        ownerName2 = entry.getOwnerName2();
        ownerID2 = entry.getOwnerID2();
        argName1 = entry.getArgName1();
        argID1 = entry.getArgID1();
        amount = entry.getAmount();
        balance = entry.getBalance();
        reason = entry.getReason();
        taxReceiverID = entry.getTaxReceiverID();
        taxAmount = entry.getTaxAmount();
    }

    @DatabaseField(generatedId = true)
    public int JournalEntryId;

    @DatabaseField
    public int PilotId;
    @DatabaseField
    public int CorporationId;
    @DatabaseField
    public String CategoryName;

    @DatabaseField
    public Date date;
    @DatabaseField
    public long refID;
    @DatabaseField
    public int refTypeID;
    @DatabaseField
    public String ownerName1;
    @DatabaseField
    public long ownerID1;
    @DatabaseField
    public String ownerName2;
    @DatabaseField
    public long ownerID2;
    @DatabaseField
    public String argName1;
    @DatabaseField
    public long argID1;
    @DatabaseField
    public double amount;
    @DatabaseField
    public double balance;
    @DatabaseField
    public String reason;
    @DatabaseField
    public Long taxReceiverID;
    @DatabaseField
    public Double taxAmount;

    @DatabaseField
    public String RefTypeName;
}
