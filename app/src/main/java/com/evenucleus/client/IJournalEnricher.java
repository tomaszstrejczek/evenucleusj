package com.evenucleus.client;

import java.util.List;

/**
 * Created by tomeks on 2015-01-01.
 */
public interface IJournalEnricher {
    List<EnrichedJournalEntry> Enrich(List<JournalEntry> jes, List<WalletTransaction> wts);
}
