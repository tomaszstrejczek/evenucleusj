package com.evenucleus.evenucleus;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.util.Log;
import android.widget.Toast;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends ActionBarActivity {

    @ViewById
    ListView pilotList;

    @Bean
    PilotListAdapter adapter;

    Alarm _alarm;

    @AfterViews
    void afterUpdate() {
        Log.d(MainActivity.class.getName(), "afterupdate");
        pilotList.setAdapter(adapter);

        _alarm = new Alarm();
        _alarm.SetAlarm(this.getApplicationContext());
    }

    @Receiver(actions = Alarm.RefreshIntent)
    void OnRefresh(Context context) {
        // Put here YOUR code.
        Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example
    }

    @UiThread
    void RefreshList() {
        adapter.notifyDataSetChanged();
    }


    @OptionsItem
    void add_key() {
        AddKey_.intent(this).start();
    }
}
