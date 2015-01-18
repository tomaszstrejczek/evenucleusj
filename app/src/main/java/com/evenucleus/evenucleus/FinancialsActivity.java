package com.evenucleus.evenucleus;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_financials)
public class FinancialsActivity extends ActionBarActivity {

    @ViewById(R.id.tabs)
    PagerSlidingTabStrip tabs;

    @ViewById(R.id.pager)
    ViewPager pager;

    @AfterViews
    void afterViews() {
        pager.setAdapter(new FinancialsPageAdapter(getSupportFragmentManager()));
        tabs.setViewPager(pager);
    }

}
