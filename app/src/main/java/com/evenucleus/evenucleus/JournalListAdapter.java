package com.evenucleus.evenucleus;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.beimin.eveapi.utils.StringUtils;
import com.evenucleus.client.ArbitrageCategorySuggester;
import com.evenucleus.client.EnrichedJournalEntry;
import com.evenucleus.client.ICategorySuggester;
import com.evenucleus.client.IJournalEnricher;
import com.evenucleus.client.IJournalRepo;
import com.evenucleus.client.ISettingsRepo;
import com.evenucleus.client.IWalletRepo;
import com.evenucleus.client.JournalEnricher;
import com.evenucleus.client.JournalEntry;
import com.evenucleus.client.JournalRepo;
import com.evenucleus.client.SettingsRepo;
import com.evenucleus.client.WalletRepo;
import com.evenucleus.client.WalletTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Map;

/**
 * Created by tomeks on 2015-01-19.
 */
@EBean
public class JournalListAdapter extends BaseAdapter {

    List<EnrichedJournalEntry> _entries;

    @Bean(SettingsRepo.class)
    ISettingsRepo _settingsRepo;

    @Bean(ArbitrageCategorySuggester.class)
    ICategorySuggester _categrySuggester1;

    @App
    MyApplication _app;

    @RootContext
    Context context;

    @AfterInject
    public void afterInject() {
        try {
            Date laterThan = _settingsRepo.getFinancialsLaterThan();
            String byCategory = _settingsRepo.getFilterBy();
            boolean onlySuggested = _settingsRepo.getOnlySuggested();

            List<EnrichedJournalEntry> data = _app.getEnrichedJournalEntries();
            List<EnrichedJournalEntry> entries1 = new ArrayList<EnrichedJournalEntry>();

            for(EnrichedJournalEntry je: data) {
                if (laterThan==null || je.Date.after(laterThan)) {
                    entries1.add(je);
                }
            }
            _categrySuggester1.Suggest(entries1);
            _entries = new ArrayList<EnrichedJournalEntry>();
            for(EnrichedJournalEntry je: entries1) {
                if   (byCategory == null || byCategory.equals(FinancialsSettingsActivity.NOFILTER)
                        || ((je.Category==null || je.Category.isEmpty()) && byCategory.equals(FinancialsSettingsActivity.NOCATEGORY))
                        || (je.Category!=null && je.Category.equals(byCategory))) {
                    if (!onlySuggested || je.Suggested || je.Category==null || je.Category.isEmpty()) {
                        _entries.add(je);
                    }
                }
            }
        }
        catch (Exception e) {
            new AlertDialog.Builder(context)
                    .setTitle("Error")
                    .setMessage(e.toString())
                    .show();
        }
    }

    @Override
    public int getCount() {
        return _entries.size();
    }

    @Override
    public EnrichedJournalEntry getItem(int position) {
        return _entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JournalItemView journalItemView;
        if (convertView == null) {
            journalItemView = JournalItemView_.build(context);
        } else {
            journalItemView = (JournalItemView) convertView;
        }

        EnrichedJournalEntry je =getItem(position);
        journalItemView.bind(je);
        journalItemView.setLongClickable(true);

        return journalItemView;
    }
}
