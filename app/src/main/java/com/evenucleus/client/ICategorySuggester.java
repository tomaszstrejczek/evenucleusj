package com.evenucleus.client;

import java.util.Collection;

/**
 * Created by TS15187 on 2015-01-28.
 */
public interface ICategorySuggester {
    public void Suggest(Collection<EnrichedJournalEntry> entries);
}
