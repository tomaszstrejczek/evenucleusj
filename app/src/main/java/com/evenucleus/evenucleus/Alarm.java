package com.evenucleus.evenucleus;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.evenucleus.client.CorporationRepo;
import com.evenucleus.client.DatabaseHelper;
import com.evenucleus.client.EveApiCaller;
import com.evenucleus.client.ICorporationRepo;
import com.evenucleus.client.IPilotRepo;
import com.evenucleus.client.IPilotService;
import com.evenucleus.client.ISkillRepo;
import com.evenucleus.client.Job;
import com.evenucleus.client.JobRepo;
import com.evenucleus.client.JobService;
import com.evenucleus.client.JobSummary;
import com.evenucleus.client.JournalRepo;
import com.evenucleus.client.KeyInfoRepo;
import com.evenucleus.client.NextRefreshCalculator;
import com.evenucleus.client.PendingNotification;
import com.evenucleus.client.PendingNotificationRepo;
import com.evenucleus.client.PilotRepo;
import com.evenucleus.client.PilotService;
import com.evenucleus.client.SettingsRepo;
import com.evenucleus.client.SkillRepo;
import com.evenucleus.client.TypeNameDict;
import com.evenucleus.client.UserData;
import com.evenucleus.client.WalletRepo;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.util.Log;

/**
 * Created by tomeks on 2015-01-05.
 */
public class Alarm extends BroadcastReceiver {
    final public static String ONE_TIME = "onetime";
    final public static String RefreshIntent = "com.evenucleus.evenucleus.refresh";
    final public static String OneTimeIntent = "com.evenucleus.evenucleus.onetime";
    final public static String CategorySetIntent = "com.evenucleus.evenucleus.categoryset";

    final Logger logger = LoggerFactory.getLogger(Alarm.class);

    public Alarm() {

    }

    private class MyTask extends AsyncTask<Void, Void, DateTime> {

        Context _context;
        Alarm _alarm;
        PowerManager.WakeLock _wl;
        Exception _ex;

        public MyTask(Context context, Alarm alarm) {
            _context = context;
            _alarm = alarm;
            _ex = null;
        }

        @Override
        protected void onPreExecute () {
            logger.debug("onPreExecute");
            PowerManager pm = (PowerManager) _context.getSystemService(Context.POWER_SERVICE);
            _wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
            _wl.acquire();

            Toast.makeText(_context, "Starting refresh", Toast.LENGTH_LONG).show(); // For example
        }

        @Override
        protected DateTime doInBackground(Void... params) {
            logger.debug("doInBackground");
            try {
                MyDatabaseHelper localdb = new MyDatabaseHelper(_context);
                TypeNameDict typeNameDict = new TypeNameDict();
                typeNameDict._localdb = localdb;
                KeyInfoRepo keyInfoRepo = new KeyInfoRepo();
                keyInfoRepo._localdb = localdb;

                PilotService pilotService = new PilotService();
                pilotService._typeNameDict = typeNameDict;
                pilotService._eveApiCaller = new EveApiCaller();
                pilotService._keyInfoRepo = keyInfoRepo;

                PilotRepo pilotRepo = new PilotRepo();
                pilotRepo._localdb = localdb;
                pilotRepo._eveApiCaller = new EveApiCaller();

                CorporationRepo corpoRepo = new CorporationRepo();
                corpoRepo._localdb = localdb;

                PendingNotificationRepo pendingNotificationRepo = new PendingNotificationRepo();
                pendingNotificationRepo._localdb = localdb;
                SkillRepo skillRepo = new SkillRepo();
                skillRepo._pendingNotificationRepo = pendingNotificationRepo;
                skillRepo._localdb= localdb;

                JobService jobService = new JobService();
                jobService._pilotRepo = pilotRepo;
                jobService._corporationRepo = corpoRepo;
                jobService._eveApiCaller = new EveApiCaller();
                jobService._typeNameDict = typeNameDict;

                JobRepo jobRepo = new JobRepo();
                jobRepo._localdb = localdb;
                jobRepo._pilotRepo = pilotRepo;
                jobRepo._pendingNotificationRepo = pendingNotificationRepo;

                SettingsRepo settingsRepo = new SettingsRepo();
                settingsRepo._localdb = localdb;

                PilotService.Result result = pilotService.Get();
                JobService.Result resultJobs = jobService.Get();
                UserData userData = new UserData();
                userData.Pilots = result.pilots;
                userData.Corporations = result.corporations;
                userData.Jobs = resultJobs.jobs;
                userData.JobSummary = new JobSummary();

                pilotRepo.Update(userData);
                skillRepo.Update(userData);
                corpoRepo.Update(userData);
                jobRepo.Update(userData);

                JournalRepo journalRepo = new JournalRepo();
                journalRepo._localdb = localdb;
                journalRepo._pilotRepo = pilotRepo;
                journalRepo._corporationRepo = corpoRepo;
                journalRepo._eveApiCaller = new EveApiCaller();
                WalletRepo walletRepo = new WalletRepo();
                walletRepo._localdb = localdb;
                walletRepo._pilotRepo = pilotRepo;
                walletRepo._corporationRepo = corpoRepo;
                walletRepo._eveApiCaller = new EveApiCaller();

                journalRepo.ReplicateFromEve();
                walletRepo.ReplicateFromEve();

                postNotifications(pendingNotificationRepo);

                settingsRepo.setLatestAlert(new Date());

                DateTime cachedUntil = new NextRefreshCalculator().Calculate(new DateTime(), Arrays.asList(resultJobs.cachedUntil, result.cachedUntil));
                logger.debug("Alarm cachedUntil {}", cachedUntil);
                    return cachedUntil;
            }
            catch (Exception e)
            {
                logger.error("Exception ", e);
                _ex = e;
                return new DateTime().plusMinutes(5);
            }
        }

        private void postNotifications(PendingNotificationRepo repo) throws SQLException {
            logger.debug("postNotifications");
            NotificationManager mNotificationManager = (NotificationManager) _context.getSystemService(_context.NOTIFICATION_SERVICE);

            List<PendingNotification> notifications = repo.GetAll();
            logger.debug("Got {} notification to send", notifications.size());
            String ns = Context.NOTIFICATION_SERVICE;

            for(PendingNotification n:notifications) {
                PendingIntent pendingIntent = PendingIntent.getActivity(_context, 0, new Intent(), 0);

                logger.debug("Posting notification {}: {}", n.PendingNotificationId, n.Message2);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(_context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(n.Message)
                        .setContentText(n.Message2)
                        .setContentIntent(pendingIntent);

                mNotificationManager.notify(n.PendingNotificationId, builder.build());
                repo.Remove(n.PendingNotificationId);
            }
        }

        @Override
        protected void onPostExecute(DateTime result) {
            logger.debug("onPostExecute");
            if (_ex != null)
                Toast.makeText(_context, "Exception: " + _ex.toString(), Toast.LENGTH_LONG).show(); // For example

            Period per = new Period(new DateTime(), result, PeriodType.time());
            String msg = String.format("Next refresh in %dh %dm", per.getHours(), per.getMinutes());
            Toast.makeText(_context, msg, Toast.LENGTH_LONG).show();


            try {
                _alarm.SetAlarm(_context, result);
            } catch (SQLException e) {
                logger.error("onPostExecute SetAlarm", e.toString());
                e.printStackTrace();
            }
            _context.sendBroadcast(new Intent(RefreshIntent));

            _wl.release();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        logger.debug("onReceive");
        MyTask task = new MyTask(context, this);
        task.execute();
    }

    public void DoWork(Context context) {

    }

    public void StartAlarm(Context context) throws SQLException {
        logger.debug("StartAlarm");

        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Alarm.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        intent.setAction(OneTimeIntent);

        onReceive(context, intent);
    }

    public void SetAlarm(Context context, DateTime when) throws SQLException {
        logger.debug("SetAlarm {}", when.toString());

        DatabaseHelper localdb = new MyDatabaseHelper(context);
        SettingsRepo settingsRepo = new SettingsRepo();
        settingsRepo._localdb = localdb;
        settingsRepo.setNextAlert(when.toDate());

        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Alarm.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        intent.setAction(OneTimeIntent);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

        DateTime dtNow = new DateTime();
        Duration d = new Duration(dtNow, when);

        // If the alarm is too close then we invoke it one time and set periodic to something longer
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime()+d.getMillis(),
                d.getMillis()<AlarmManager.INTERVAL_FIFTEEN_MINUTES?AlarmManager.INTERVAL_FIFTEEN_MINUTES:d.getMillis(), pi);

        //After after 30 seconds
        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5 , pi);
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
