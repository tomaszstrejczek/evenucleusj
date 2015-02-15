package com.evenucleus.evenucleus;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.evenucleus.client.IPilotRepo;
import com.evenucleus.client.Pilot;
import com.evenucleus.client.PilotRepo;

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

import java.util.List;

@EActivity(R.layout.activity_jobs)
public class JobsActivity extends MyActivityBase {
    final Logger logger = LoggerFactory.getLogger(JobsActivity.class);

    @ViewById(R.id.jobsList)
    ListView jobList;

    @ViewById(R.id.ManufacturingSlots)
    TextView manufacturingSlot;

    @ViewById(R.id.ResearchSlots)
    TextView reserchSlots;

    @Bean
    JobListAdapter adapter;

    @Bean(PilotRepo.class)
    IPilotRepo _pilotRepo;

    @AfterViews
    void afterUpdate() {
        logger.debug("afterupdate");
        jobList.setAdapter(adapter);

        try {
            int maxManufacturing = 0;
            int maxResearch = 0;

            List<Pilot> pilots = _pilotRepo.GetAll();
            for(Pilot p: pilots) {
                maxManufacturing += p.MaxManufacturingJobs;
                maxResearch += p.MaxResearchJobs;
            }

            manufacturingSlot.setText(String.format("%d/%d", adapter.getCurrentManufacturingCount(), maxManufacturing));
            reserchSlots.setText(String.format("%d/%d", adapter.getCurrentResearchCount(), maxResearch));
        } catch(Exception e) {
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

}
