package com.evenucleus.evenucleus;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.evenucleus.client.ISettingsRepo;
import com.evenucleus.client.SettingsRepo;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;

@EActivity(R.layout.activity_settings)
public class SettingsActivity extends MyActivityBase {
    @Bean(SettingsRepo.class)
    ISettingsRepo _settingsRepo;

    @ViewById(R.id.SynchronizedOn)
    TextView SynchronizedOn;

    @ViewById(R.id.NextSynchronization)
    TextView NextSynchronization;

    @AfterViews
    void afterInject() {
        try {
            Date d = _settingsRepo.getLatestAlert();
            SynchronizedOn.setText(DateFormat.getDateTimeInstance().format(d));

            d = _settingsRepo.getNextAlert();
            NextSynchronization.setText(DateFormat.getDateTimeInstance().format(d));
        }
        catch (SQLException e) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(e.toString())
                    .show();
        }
    }

}
