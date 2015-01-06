package com.evenucleus.evenucleus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.widget.Toast;

import com.evenucleus.client.CorporationRepo;
import com.evenucleus.client.EveApiCaller;
import com.evenucleus.client.ICorporationRepo;
import com.evenucleus.client.IPilotRepo;
import com.evenucleus.client.IPilotService;
import com.evenucleus.client.ISkillRepo;
import com.evenucleus.client.Job;
import com.evenucleus.client.JobSummary;
import com.evenucleus.client.KeyInfoRepo;
import com.evenucleus.client.PendingNotificationRepo;
import com.evenucleus.client.PilotRepo;
import com.evenucleus.client.PilotService;
import com.evenucleus.client.SkillRepo;
import com.evenucleus.client.TypeNameDict;
import com.evenucleus.client.UserData;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import android.util.Log;

/**
 * Created by tomeks on 2015-01-05.
 */
public class Alarm extends BroadcastReceiver {
    final public static String ONE_TIME = "onetime";
    final public static String RefreshIntent = "com.evenucleus.evenucleus.refresh";

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
            PowerManager pm = (PowerManager) _context.getSystemService(Context.POWER_SERVICE);
            _wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
            _wl.acquire();

            Toast.makeText(_context, "Starting refresh", Toast.LENGTH_LONG).show(); // For example
        }

        @Override
        protected DateTime doInBackground(Void... params) {
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


                PilotService.Result result = pilotService.Get();
                UserData userData = new UserData();
                userData.Pilots = result.pilots;
                userData.Corporations = result.corporations;
                userData.Jobs = new ArrayList<Job>();
                userData.JobSummary = new JobSummary();

                pilotRepo.Update(userData);
                skillRepo.Update(userData);
                corpoRepo.Update(userData);

                return new DateTime(result.cachedUntil);
            }
            catch (Exception e)
            {
                Log.e(Alarm.class.getName(), String.format("Exception %s: %s", e.toString(), e.getStackTrace().toString()));
                _ex = e;
                return new DateTime().plusMinutes(5);
            }
        }

        @Override
        protected void onPostExecute(DateTime result) {
            if (_ex != null)
                Toast.makeText(_context, "Exception: " + _ex.toString(), Toast.LENGTH_LONG).show(); // For example

            Period per = new Period(new DateTime(), result, PeriodType.time());
            String msg = String.format("Next refresh in %dh %dm", per.getHours(), per.getMinutes());
            Toast.makeText(_context, msg, Toast.LENGTH_LONG).show();


            _alarm.SetAlarm(_context, result);
            _context.sendBroadcast(new Intent(RefreshIntent));

            _wl.release();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        MyTask task = new MyTask(context, this);
        task.execute();
    }

    public void DoWork(Context context) {

    }

    public void SetAlarm(Context context, DateTime when)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Alarm.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

        DateTime dtNow = new DateTime();
        Duration d = new Duration(dtNow, when);

        am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + d.getMillis() , pi);

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
