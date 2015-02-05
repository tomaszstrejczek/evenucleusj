package com.evenucleus.client;

import java.sql.SQLException;
import java.util.Date;
import android.util.Log;

import com.evenucleus.evenucleus.MyDatabaseHelper;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomeks on 2015-01-18.
 */
@EBean
public class SettingsRepo implements ISettingsRepo {
    final Logger logger = LoggerFactory.getLogger(SettingsRepo.class);


    @Bean(MyDatabaseHelper.class)
    public DatabaseHelper _localdb;

    @Override
    public Date getNextAlert() throws SQLException {
        logger.debug("getNextAlert");

        Setting s = _localdb.getSettingDao().queryForId("next_alert");
        if (s == null)
            return null;
        return s.DateValue;
    }

    @Override
    public void setNextAlert(Date value) throws SQLException {
        logger.debug("setNextAlert {}", value.toString());
        Setting s = new Setting();
        s.Key = "next_alert";
        s.DateValue = value;
        _localdb.getSettingDao().createOrUpdate(s);
    }

    @Override
    public Date getLatestAlert() throws SQLException {
        logger.debug("getLatestAlert");

        Setting s = _localdb.getSettingDao().queryForId("latest_alert");
        if (s == null)
            return null;
        return s.DateValue;
    }

    @Override
    public void setLatestAlert(Date value) throws SQLException {
        logger.debug("setLatestAlert {}", value.toString());
        Setting s = new Setting();
        s.Key = "latest_alert";
        s.DateValue = value;
        _localdb.getSettingDao().createOrUpdate(s);
    }

    @Override
    public Date getFinancialsLaterThan() throws SQLException {
        logger.debug("getFinancialsLaterThan");

        Setting s = _localdb.getSettingDao().queryForId("financials_later_than");
        if (s == null)
            return null;
        return s.DateValue;
    }

    @Override
    public void setFinancialsLaterThan(Date value) throws SQLException {
        logger.debug("setFinancialsLaterThan {}", value.toString());
        Setting s = new Setting();
        s.Key = "financials_later_than";
        s.DateValue = value;
        _localdb.getSettingDao().createOrUpdate(s);

    }

    @Override
    public String getFilterBy() throws SQLException {
        logger.debug("getFilterBy");

        Setting s = _localdb.getSettingDao().queryForId("financials_filter_by");
        if (s == null)
            return null;
        return s.StringValue;
    }

    @Override
    public void setFilterBy(String value) throws SQLException {
        logger.debug("setFilterBy {}", value.toString());
        Setting s = new Setting();
        s.Key = "financials_filter_by";
        s.StringValue = value;
        _localdb.getSettingDao().createOrUpdate(s);

    }

    @Override
    public boolean getOnlySuggested() throws SQLException {
        logger.debug("getOnlySuggested");

        Setting s = _localdb.getSettingDao().queryForId("financials_only_suggested");
        if (s == null)
            return false;
        return s.BooleanValue;
    }

    @Override
    public void setOnlySuggested(boolean value) throws SQLException {
        logger.debug("setOnlySuggested {}", value);
        Setting s = new Setting();
        s.Key = "financials_only_suggested";
        s.BooleanValue = value;
        _localdb.getSettingDao().createOrUpdate(s);
    }


}
