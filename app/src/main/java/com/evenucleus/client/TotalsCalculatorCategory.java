package com.evenucleus.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.EBean;

/**
 * Created by tomeks on 2015-01-25.
 */
@EBean
public class TotalsCalculatorCategory implements ITotalsCalculator {
    @Override
    public Collection<Total> Calculate(List<EnrichedJournalEntry> entries) {
        Map<String, Total> result = new HashMap<String, Total>();
        for(EnrichedJournalEntry je: entries) {
            if (je.Category != null && !je.Category.equals("")) {
                Total t = null;
                if (result.containsKey(je.Category))
                    t = result.get(je.Category);
                else
                {
                    t = new Total();
                    t.Name = je.Category;
                    result.put(je.Category, t);
                }

                if (je.Amount < 0)
                {
                    t.Expense += - je.Amount;
                    t.Profit += je.Amount;
                }
                else
                {
                    t.Income += je.Amount;
                    t.Profit += je.Amount;
                }
            }
        }

        return result.values();
    }
}
