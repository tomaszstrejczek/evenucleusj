package com.evenucleus.evenucleus;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.util.Log;
import android.widget.Toast;


import com.evenucleus.client.IPendingNotificationRepo;
import com.evenucleus.client.MyLogger;
import com.evenucleus.client.PendingNotificationRepo;
import com.logentries.logback.LogentriesAppender;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.joda.time.DateTime;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.android.LogcatAppender;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.LayoutBase;


@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends ActionBarActivity {

    @ViewById
    ListView pilotList;

    @Bean
    PilotListAdapter adapter;

    @Bean(PendingNotificationRepo.class)
    IPendingNotificationRepo pendingNotificationRepo;

    @Bean
    MyLogger logger;

    @AfterViews
    void afterUpdate() {
        Log.d(MainActivity.class.getName(), "afterupdate");
        logger.debug("afterupdate");

        LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
        lc.reset();

        PatternLayout layout = new PatternLayout();
        layout.setContext(lc);
        layout.setPattern("%-5level:%d:%logger:%message");
        layout.start();

        LogentriesAppender appender = new LogentriesAppender();
        appender.setContext(lc);
        appender.setToken("4b5ef73c-f5c7-4b6c-a7c7-01f40b578910");
        appender.setLayout(layout);

        appender.start();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(lc);
        encoder.setPattern("%message%n");
        encoder.start();

        LogcatAppender logcatAppender = new LogcatAppender();
        logcatAppender.setContext(lc);
        logcatAppender.setEncoder(encoder);
        logcatAppender.start();

        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.addAppender(appender);
        root.addAppender(logcatAppender);
        root.setLevel(Level.TRACE);

        Logger log = LoggerFactory.getLogger(getClass());
        log.debug("Logger configured: {}", "test");

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
    @OptionsItem(R.id.action_financials)
    void financials() {
        FinancialsActivity_.intent(this).start();
    }
}
