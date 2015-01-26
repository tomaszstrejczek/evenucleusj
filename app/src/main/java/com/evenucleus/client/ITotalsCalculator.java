package com.evenucleus.client;

import java.util.Collection;
import java.util.List;

/**
 * Created by tomeks on 2015-01-25.
 */
public interface ITotalsCalculator {
    public class Total {
        public String Name;
        public double Income;
        public double Expense;
        public double Profit;
    }

    Collection<Total> Calculate(List<EnrichedJournalEntry> entries);
}
