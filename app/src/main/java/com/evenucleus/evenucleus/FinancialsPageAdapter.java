package com.evenucleus.evenucleus;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by tomeks on 2015-01-18.
 */
public class FinancialsPageAdapter extends FragmentStatePagerAdapter {
    public FinancialsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new FinancialsFragment_();
            case 1:
                return new FinancialsTotalFragment_();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Not filtered";
            case 1:
                return "Totals";
            default:
                return null;
        }

    }
}
