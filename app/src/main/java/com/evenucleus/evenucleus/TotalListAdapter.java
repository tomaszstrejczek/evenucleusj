package com.evenucleus.evenucleus;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.evenucleus.client.EnrichedJournalEntry;
import com.evenucleus.client.ISettingsRepo;
import com.evenucleus.client.ITotalsCalculator;
import com.evenucleus.client.SettingsRepo;
import com.evenucleus.client.TotalsCalculatorCategory;
import com.evenucleus.client.TotalsCalculatorTotal;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomeks on 2015-01-19.
 */
@EBean
public class TotalListAdapter extends BaseAdapter {
    List<EnrichedJournalEntry> _entries;
    List<ITotalsCalculator.Total> _totals;

    @Bean(SettingsRepo.class)
    ISettingsRepo _settingsRepo;

    @Bean(TotalsCalculatorCategory.class)
    ITotalsCalculator _calculator1;
    @Bean(TotalsCalculatorTotal.class)
    ITotalsCalculator _calculatorTotal;

    @App
    MyApplication _app;

    @RootContext
    Context context;

    @AfterInject
    public void initAdapter() {
        try {
            Date laterThan = _settingsRepo.getFinancialsLaterThan();
            List<EnrichedJournalEntry> data = _app.getEnrichedJournalEntries();
            _entries = new ArrayList<EnrichedJournalEntry>();
            for(EnrichedJournalEntry je: data) {
                if (laterThan==null || je.Date.after(laterThan))
                    _entries.add(je);
            }

            List<ITotalsCalculator> calculators = Arrays.asList(_calculator1, _calculatorTotal);
            _totals = new ArrayList<ITotalsCalculator.Total>();
            for(ITotalsCalculator calc: calculators)
                _totals.addAll(calc.Calculate(_entries));
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
        return _totals.size();
    }

    @Override
    public ITotalsCalculator.Total getItem(int position) {
        return _totals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TotalItemView totalItemView;
        if (convertView == null) {
            totalItemView = TotalItemView_.build(context);
        } else {
            totalItemView = (TotalItemView) convertView;
        }

        ITotalsCalculator.Total item =getItem(position);
        totalItemView.bind(item);

        return totalItemView;
    }
}
