package com.evenucleus.evenucleus;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.evenucleus.client.EnrichedJournalEntry;
import com.evenucleus.client.IJournalEnricher;
import com.evenucleus.client.IJournalRepo;
import com.evenucleus.client.IWalletRepo;
import com.evenucleus.client.JournalEnricher;
import com.evenucleus.client.JournalEntry;
import com.evenucleus.client.JournalRepo;
import com.evenucleus.client.WalletRepo;
import com.evenucleus.client.WalletTransaction;

import java.util.List;

/**
 * Created by tomeks on 2015-01-19.
 */
@EBean
public class JournalListAdapter extends BaseAdapter {

    List<EnrichedJournalEntry> _entries;

    @Bean(JournalRepo.class)
    IJournalRepo _journalRepo;

    @Bean(WalletRepo.class)
    IWalletRepo _walletRepo;

    @Bean(JournalEnricher.class)
    IJournalEnricher _journalEnricher;

    @RootContext
    Context context;

    @AfterInject
    public void afterInject() {
        try {
            List<JournalEntry> jes = _journalRepo.GetAll();
            List<WalletTransaction> wes = _walletRepo.GetAll();
            _entries = _journalEnricher.Enrich(jes, wes);
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

        journalItemView.bind(getItem(position));

        return journalItemView;
    }
}
