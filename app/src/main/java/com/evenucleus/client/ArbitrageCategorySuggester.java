package com.evenucleus.client;

import org.androidannotations.annotations.EBean;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TS15187 on 2015-01-28.
 */
@EBean
public class ArbitrageCategorySuggester implements ICategorySuggester {
    private class TypeNameInfo {
        public String TypeName;
        public int Sold;
        public int Bought;
    }
    @Override
    public void Suggest(Collection<EnrichedJournalEntry> entries) {
        // summarize aold and bought items
        Map<String, TypeNameInfo> map = new HashMap<String, TypeNameInfo>();
        for(EnrichedJournalEntry je:entries) {
            if (je.TypeName!=null && !je.TypeName.equals("")) {
                TypeNameInfo info = null;
                if (map.containsKey(je.TypeName))
                    info = map.get(je.TypeName);
                else
                {
                    info = new TypeNameInfo();
                    info.TypeName = je.TypeName;
                    map.put(je.TypeName, info);
                }
                if (je.Quantity > 0) info.Sold += je.Quantity;
                else
                    info.Bought -= je.Quantity;
            }
        }

        // set suggested categoriws
        for(TypeNameInfo info: map.values()) {
            if (info.Sold == 0 || info.Bought == 0)
                continue;

            for(EnrichedJournalEntry je: entries)
                if (je.TypeName.equals(info.TypeName) && (je.Category==null || je.Category.equals(""))) {
                    je.Category = "Arbitrage";
                    je.Suggested = true;
                }
        }
    }
}
