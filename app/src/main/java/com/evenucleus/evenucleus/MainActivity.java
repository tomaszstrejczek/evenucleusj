package com.evenucleus.evenucleus;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends ActionBarActivity {

    @ViewById
    ListView pilotList;

    @Bean
    PilotListAdapter adapter;

    @AfterViews
    void afterUpdate() {
        Log.d(MainActivity.class.getName(), "afterupdate");
        pilotList.setAdapter(adapter);
    }

    @OptionsItem
    void add_key() {
        AddKey_.intent(this).start();
    }
}
