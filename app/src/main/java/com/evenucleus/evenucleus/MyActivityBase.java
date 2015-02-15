package com.evenucleus.evenucleus;

import android.support.v7.app.ActionBarActivity;

import com.apptentive.android.sdk.Apptentive;

/**
 * Created by tomeks on 2015-02-15.
 */
public class MyActivityBase extends ActionBarActivity {
    @Override
    protected void onStart() {
        super.onStart();
        Apptentive.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Apptentive.onStop(this);
    }
}

