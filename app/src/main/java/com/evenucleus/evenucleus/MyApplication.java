package com.evenucleus.evenucleus;

import android.app.AlertDialog;
import android.app.Application;

import com.evenucleus.client.EnrichedJournalEntry;
import com.evenucleus.client.IJournalEnricher;
import com.evenucleus.client.IJournalRepo;
import com.evenucleus.client.IWalletRepo;
import com.evenucleus.client.JournalEnricher;
import com.evenucleus.client.JournalEntry;
import com.evenucleus.client.JournalRepo;
import com.evenucleus.client.WalletRepo;
import com.evenucleus.client.WalletTransaction;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by tomeks on 2015-01-24.
 */
@EApplication
public class MyApplication extends Application {

    @Bean(JournalRepo.class)
    IJournalRepo _journalRepo;

    @Bean(WalletRepo.class)
    IWalletRepo _walletRepo;

    @Bean(JournalEnricher.class)
    IJournalEnricher _journalEnricher;

    List<EnrichedJournalEntry> _entries;

    public List<EnrichedJournalEntry> getEnrichedJournalEntries() throws SQLException {
        if (_entries != null)
            return _entries;

        List<JournalEntry> jes = _journalRepo.GetAll();
        List<WalletTransaction> wes = _walletRepo.GetAll();
        _entries = _journalEnricher.Enrich(jes, wes);

        return _entries;
    }

}
