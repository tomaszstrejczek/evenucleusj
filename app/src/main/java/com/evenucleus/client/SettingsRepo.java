package com.evenucleus.client;

import java.sql.SQLException;
import java.util.Date;
import android.util.Log;

import com.evenucleus.evenucleus.MyDatabaseHelper;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

/**
 * Created by tomeks on 2015-01-18.
 */
@EBean
public class SettingsRepo implements ISettingsRepo {
    @Bean(MyDatabaseHelper.class)
    public DatabaseHelper _localdb;

    @Override
    public Date getNextAlert() throws SQLException {
        Log.d(SettingsRepo.class.getName(), "getNextAlert");

        Setting s = _localdb.getSettingDao().queryForId("next_alert");
        if (s == null)
            return null;
        return s.DateValue;
    }

    @Override
    public void setNextAlert(Date value) throws SQLException {
        Log.d(SettingsRepo.class.getName(), String.format("setNextAlert %s", value.toString()));
        Setting s = new Setting();
        s.Key = "next_alert";
        s.DateValue = value;
        _localdb.getSettingDao().createOrUpdate(s);
    }

    @Override
    public Date getLatestAlert() throws SQLException {
        Log.d(SettingsRepo.class.getName(), "getLatestAlert");

        Setting s = _localdb.getSettingDao().queryForId("latest_alert");
        if (s == null)
            return null;
        return s.DateValue;
    }

    @Override
    public void setLatestAlert(Date value) throws SQLException {
        Log.d(SettingsRepo.class.getName(), String.format("setLatestAlert %s", value.toString()));
        Setting s = new Setting();
        s.Key = "latest_alert";
        s.DateValue = value;
        _localdb.getSettingDao().createOrUpdate(s);
    }

    @Override
    public Date getFinancialsLaterThan() throws SQLException {
        Log.d(SettingsRepo.class.getName(), "getFinancialsLaterThan");

        Setting s = _localdb.getSettingDao().queryForId("financials_later_than");
        if (s == null)
            return null;
        return s.DateValue;
    }

    @Override
    public void setFinancialsLaterThan(Date value) throws SQLException {
        Log.d(SettingsRepo.class.getName(), String.format("setFinancialsLaterThan %s", value.toString()));
        Setting s = new Setting();
        s.Key = "financials_later_than";
        s.DateValue = value;
        _localdb.getSettingDao().createOrUpdate(s);

    }


}
