package com.evenucleus.client;

import java.util.Date;

/**
 * Created by tomeks on 2015-01-01.
 */
public class EnrichedJournalEntry {
    public int PilotId;
    public int CorporationId;
    public long RefID;

    public Date Date;
    public double Amount;
    public String Description;

    public String Category;
    public boolean Suggested;
    public String TypeName;    // name for the item sold or bought
    public int Quantity;       // number of items sold or bought

    public boolean Selected;
}
