package com.evenucleus.client;

import java.util.Date;

/**
 * Created by tomeks on 2015-01-01.
 */
public class EnrichedJournalEntry {
    public int JournalEntryId;
    public int PilotId;
    public int CorporationId;

    public Date Date;
    public double Amount;
    public String Description;

    public String Category;

    public boolean Selected;
}
