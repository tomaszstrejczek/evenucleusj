package com.evenucleus.evenucleus;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_financials)
public class FinancialsFragment extends android.support.v4.app.Fragment {
    @ViewById(R.id.transactionList)
    ListView transactionList;

    @Bean
    JournalListAdapter adapter;

    @AfterViews
    public void AfterViews() {
        transactionList.setAdapter(adapter);
    }
}
