package com.evenucleus.evenucleus;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EActivity(R.layout.activity_jobs)
@OptionsMenu(R.menu.menu_jobs)
public class JobsActivity extends ActionBarActivity {
    final Logger logger = LoggerFactory.getLogger(JobsActivity.class);

    @ViewById(R.id.jobsList)
    ListView jobList;

    @Bean
    JobListAdapter adapter;

    @AfterViews
    void afterUpdate() {
        logger.debug("afterupdate");
        jobList.setAdapter(adapter);
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

}
