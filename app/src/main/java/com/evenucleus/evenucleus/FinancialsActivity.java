package com.evenucleus.evenucleus;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.apptentive.android.sdk.Apptentive;
import com.astuetz.PagerSlidingTabStrip;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_financials)
@OptionsMenu(R.menu.menu_financials)
public class FinancialsActivity extends MyActivityBase {

    @ViewById(R.id.tabs)
    PagerSlidingTabStrip tabs;

    @ViewById(R.id.pager)
    ViewPager pager;

    @AfterViews
    void afterViews() {
        pager.setAdapter(new FinancialsPageAdapter(getSupportFragmentManager()));

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);
        tabs.setDividerWidth(2);

        /*
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==1)
                    Apptentive.engage(FinancialsActivity.this, "totals_selected");
                else
                    Apptentive.engage(FinancialsActivity.this, "financials_selected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        */
    }

    @OptionsItem(R.id.action_settings)
    void settings() {
        FinancialsSettingsActivity_.intent(this).start();
    }

}
