package com.evenucleus.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.util.Log;
import org.androidannotations.annotations.EBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomeks on 2015-01-01.
 */
@EBean
public class JournalEnricher implements IJournalEnricher {
    final Logger logger = LoggerFactory.getLogger(JournalEnricher.class);

    class DateComparator2 implements Comparator<EnrichedJournalEntry> {
        @Override
        public int compare(EnrichedJournalEntry o1, EnrichedJournalEntry o2) {
            return -o1.Date.compareTo(o2.Date);
        }
    }

    @Override
    public List<EnrichedJournalEntry> Enrich(List<JournalEntry> jes, List<WalletTransaction> wts) {
        logger.debug("Enrich");

        List<EnrichedJournalEntry> result = new ArrayList<EnrichedJournalEntry>();

        // Match pilot by pilot
        Set<Integer> ids = new HashSet<Integer>();
        for(JournalEntry je:jes) if (je.PilotId!=0 && !ids.contains(je.PilotId)) ids.add(je.PilotId);

        for(Integer id:ids)
        {
            List<JournalEntry> filteredJes = new ArrayList<JournalEntry>();
            for(JournalEntry je:jes) if (je.PilotId==id && Math.abs(je.amount)>0.0) filteredJes.add(je);

            List<WalletTransaction> filteredWts = new ArrayList<WalletTransaction>();
            for(WalletTransaction wt: wts) if (wt.PilotId == id) filteredWts.add(wt);

            result.addAll(EnrichIndividual(filteredJes, filteredWts));
        }

        // Match corporation by corporation
        ids.clear();
        for(JournalEntry je:jes) if (je.CorporationId!=0 && !ids.contains(je.CorporationId)) ids.add(je.CorporationId);

        for(Integer id:ids)
        {
            List<JournalEntry> filteredJes = new ArrayList<JournalEntry>();
            for(JournalEntry je:jes) if (je.CorporationId==id && Math.abs(je.amount)>0.0) filteredJes.add(je);

            List<WalletTransaction> filteredWts = new ArrayList<WalletTransaction>();
            for(WalletTransaction wt: wts) if (wt.CorporationId== id) filteredWts.add(wt);

            result.addAll(EnrichIndividual(filteredJes, filteredWts));
        }

        Collections.sort(result, new DateComparator2());
        
        return result;
    }

    class DateComparator implements Comparator<JournalEntry> {
        @Override
        public int compare(JournalEntry o1, JournalEntry o2) {
            return o1.date.compareTo(o2.date);
        }
    }
    private List<EnrichedJournalEntry> EnrichIndividual(List<JournalEntry> jes, List<WalletTransaction> wts) {
        List<EnrichedJournalEntry> result = new ArrayList<EnrichedJournalEntry>();

        if (jes.size() == 0)
            return result;

        Collections.sort(jes, new DateComparator());

        // Filter out transactions before first date of journal entry
        Date first = jes.iterator().next().date;
        List<WalletTransaction> wts1 = new ArrayList<WalletTransaction>();
        for(WalletTransaction wt: wts) if (!wt.transactionDateTime.before(first)) wts1.add(wt);
        wts = wts1;

        Map<JournalEntry, WalletTransaction> matching = new IdentityHashMap<JournalEntry, WalletTransaction>();
        Map<Long, WalletTransaction> dictWts = new HashMap<Long, WalletTransaction>();
        for(WalletTransaction w: wts) dictWts.put(w.journalTransactionID, w);
        Map<Long, JournalEntry> dictJes = new HashMap<Long, JournalEntry>();
        for(JournalEntry je: jes) dictJes.put(je.refID, je);

        // Match by journalTransactionId && skip them from furher matching
        wts1 = new ArrayList<WalletTransaction>();
        for(WalletTransaction w:wts)
        {
            if (dictJes.containsKey(w.journalTransactionID))
                matching.put(dictJes.get(w.journalTransactionID), w);
            else
                wts1.add(w);
        }
        wts = wts1;

        // Match by date and amount
        wts1 = new ArrayList<WalletTransaction>();
        for(WalletTransaction w:wts)
        {
            JournalEntry found = null;
            for(JournalEntry je: jes)
                if (je.date.equals(w.transactionDateTime) && Math.abs(je.amount - w.price*w.quantity)<0.01)
                {
                    found = je;
                    break;
                }

            if (found != null)
                matching.put(found, w);
            else
                wts1.add(w);
        }
        wts = wts1;

        GroupMarketEscrow(jes, matching);

        // spread broker fee and transaction tax into transactions
        double brokerfee = 0;
        double tax = 0;
        double manufacturingTax = 0;
        double buyAmount = 0;
        double sellAmount = 0;
        double manufacturing = 0;
        double marketEscrow = 0;
        for(JournalEntry x:jes)
        {
            if (x.RefTypeName.equals("Brokers Fee")) brokerfee += Math.abs(x.amount);
            if (x.RefTypeName.equals("Transaction Tax")) tax += Math.abs(x.amount);
            //if (x.RefTypeName.equals("Market Escrow") && !matching.containsKey(x)) marketEscrow+= x.amount;
            if (x.refTypeID == 120/*Manufacturing tax*/) manufacturingTax += Math.abs(x.amount);
            if (x.RefTypeName.equals("Manufacturing")) manufacturing += Math.abs(x.amount);

            if (matching.containsKey(x) && matching.get(x).transactionType.equals("buy"))
                buyAmount += Math.abs(x.amount);
            if (matching.containsKey(x) && matching.get(x).transactionType.equals("sell"))
                sellAmount += Math.abs(x.amount);
        }

        // in broker fee we also take into account market escroow
        double brokerRate = buyAmount + sellAmount == 0.0 ? 0.0 : (brokerfee+Math.abs(marketEscrow)) / (buyAmount + sellAmount);
        double sellRate = sellAmount == 0.0 ? 0.0 : tax / sellAmount;
        double manufacturingRate = manufacturing == 0.0 ? 0.0 : manufacturingTax / manufacturing;

        for(JournalEntry x:jes) {
            if (x.RefTypeName.equals("Brokers Fee") || x.RefTypeName.equals("Transaction Tax") || x.refTypeID==120/*Manufacturing tax*/ /*|| x.RefTypeName.equals("Market Escrow")*/)
                continue;

            double amount = x.amount;
            String typeName = "";
            int quantity = 0;
            if (matching.containsKey(x) && matching.get(x).transactionType.equals("buy")) {
                amount = Math.round(amount + brokerRate * amount);
                typeName = matching.get(x).typeName;
                quantity = -matching.get(x).quantity;
            }
            else if (matching.containsKey(x) && matching.get(x).transactionType.equals("sell"))
            {
                amount = Math.round(amount - brokerRate * amount - sellRate * amount);
                typeName = matching.get(x).typeName;
                quantity = matching.get(x).quantity;
            }
            else if (x.RefTypeName.equals("Manufacturing"))
                amount = Math.round(amount + manufacturingRate * amount);

            EnrichedJournalEntry entry = new EnrichedJournalEntry();
            entry.JournalEntryId = x.JournalEntryId;
            entry.PilotId = x.PilotId;
            entry.CorporationId = x.CorporationId;
            entry.Date = x.date;
            entry.Amount = amount;
            if (matching.containsKey(x))
            {
                WalletTransaction wt =matching.get(x);
                entry.Description = String.format("%c %s x %d @ %s", wt.transactionType.charAt(0),  wt.typeName, wt.quantity,
                        Number2RoundedString.Convert(wt.price));
            }
            else
                entry.Description = x.RefTypeName;
            entry.Category = x.CategoryName;
            entry.TypeName = typeName;
            entry.Quantity = quantity;
            entry.Suggested = false;
            result.add(entry);
        }
        return result;
    }

    private void GroupMarketEscrow(List<JournalEntry> jes, Map<JournalEntry, WalletTransaction> matching) {
        // select escrow
        List<JournalEntry> escrow = new ArrayList<JournalEntry>();
        for(JournalEntry je: jes) if (je.RefTypeName.equals("Market Escrow") && je.amount < 0.0 && !matching.containsKey(je)) escrow.add(je);

        Set<Long> matched = new HashSet<Long>();

        for(Map.Entry<JournalEntry, WalletTransaction> je: matching.entrySet()) {
            for(int i = escrow.size()-1; i >= 0; --i) {
                JournalEntry candidate = escrow.get(i);
                // same sign
                if (je.getKey().amount < 0) {
                    if (Math.abs(je.getKey().amount) + Math.abs(candidate.amount) <= Math.abs(je.getValue().price * je.getValue().quantity)) {
                        je.getKey().amount += candidate.amount;
                        escrow.remove(i);
                        matched.add(candidate.refID);
                    }
                }
            }
        }

        for(int i = jes.size()-1; i >= 0; --i)
            if (matched.contains(jes.get(i).refID))
                jes.remove(i);
    }

}
