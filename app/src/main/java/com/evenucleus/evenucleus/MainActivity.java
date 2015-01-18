package com.evenucleus.evenucleus;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.util.Log;
import android.widget.Toast;


import com.evenucleus.client.IPendingNotificationRepo;
import com.evenucleus.client.PendingNotificationRepo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.joda.time.DateTime;

import java.util.Date;


@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends ActionBarActivity {

    @ViewById
    ListView pilotList;

    @Bean
    PilotListAdapter adapter;

    @Bean(PendingNotificationRepo.class)
    IPendingNotificationRepo pendingNotificationRepo;

    @AfterViews
    void afterUpdate() {
        Log.d(MainActivity.class.getName(), "afterupdate");
        pilotList.setAdapter(adapter);

        DateTime when = new DateTime().plusSeconds(10);

        try {
            new Alarm().SetAlarm(this.getApplicationContext(), when);
            pendingNotificationRepo.IssueNew("Debug", "Alarm started");
        }
        catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(e.toString())
                    .show();
        }
    }

    @Receiver(actions = Alarm.RefreshIntent)
    void OnRefresh(Context context) {
        // Put here YOUR code.
        Toast.makeText(context, "Refresh", Toast.LENGTH_LONG).show(); // For example
        RefreshList();
    }

    @UiThread
    void RefreshList() {
        adapter.initAdapter();
        adapter.notifyDataSetChanged();
    }


    @OptionsItem
    void add_key() {
        AddKey_.intent(this).start();
    }
    @OptionsItem(R.id.jobs)
    void menu_jobs() {
        JobsActivity_.intent(this).start();
    }
    @OptionsItem(R.id.action_settings)
    void settings() {
        SettingsActivity_.intent(this).start();
    }
}
