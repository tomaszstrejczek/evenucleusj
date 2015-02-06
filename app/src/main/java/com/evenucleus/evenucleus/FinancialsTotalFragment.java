package com.evenucleus.evenucleus;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.evenucleus.client.ITotalsCalculator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.fragment_financials_total)
public class FinancialsTotalFragment extends android.support.v4.app.Fragment {
    //@ViewById(R.id.totalsList)
    //ListView totalsList;

    @ViewById(R.id.totalsTable)
    TableLayout totalsTable;

    @Bean
    TotalListAdapter adapter;

    @AfterViews
    public void AfterViews() {
        for(ITotalsCalculator.Total t:adapter._totals) {
            TotalItemView v = TotalItemView_.build(getActivity());
            v.bind(t);
            totalsTable.addView(v);
        }

        //totalsList.setAdapter(adapter);
    }

    @Receiver(actions = Alarm.CategorySetIntent)
    void OnRefresh(Context context) {
        RefreshList();
    }

    @UiThread
    void RefreshList() {
        //adapter.initAdapter();
        //adapter.notifyDataSetChanged();
    }

}
