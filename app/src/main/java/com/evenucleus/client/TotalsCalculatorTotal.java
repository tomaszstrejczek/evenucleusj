package com.evenucleus.client;

import org.androidannotations.annotations.EBean;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomeks on 2015-01-25.
 */
@EBean
public class TotalsCalculatorTotal implements ITotalsCalculator {
    public static final String TotalName = "Total";

    @Override
    public Collection<Total> Calculate(List<EnrichedJournalEntry> entries) {
        Total result = new Total();
        result.Name = TotalName;

        for(EnrichedJournalEntry je: entries) {
            if (je.Amount < 0)
            {
                result.Expense += - je.Amount;
                result.Profit += je.Amount;
            }
            else
            {
                result.Income += je.Amount;
                result.Profit += je.Amount;
            }
        }

        return Arrays.asList(result);
    }
}
