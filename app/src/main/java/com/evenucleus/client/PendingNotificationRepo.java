package com.evenucleus.client;

import android.util.Log;

import com.evenucleus.evenucleus.MyDatabaseHelper;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by tomeks on 2014-12-29.
 */
@EBean
public class PendingNotificationRepo implements IPendingNotificationRepo{
    final Logger logger = LoggerFactory.getLogger(PendingNotificationRepo.class);

    @Bean(MyDatabaseHelper.class)
    public DatabaseHelper _localdb;

    @Override
    public void IssueNew(String message, String message2) throws SQLException {
        logger.debug("Issue new {} {}", message, message2);

        PendingNotification n = new PendingNotification();
        n.Status = PendingNotificationStatus.Ready;
        n.CreatedOn = new Date();
        n.Message = message;
        n.Message2 = message2;

        _localdb.getPendingNotificationDao().createOrUpdate(n);
    }

    @Override
    public List<PendingNotification> GetAll() throws SQLException {
        logger.debug("GetAll");
        return _localdb.getPendingNotificationDao().queryForAll();
    }

    @Override
    public void Remove(int notificationId) throws SQLException {
        logger.debug("Remove {}", notificationId);
        _localdb.getPendingNotificationDao().deleteById(notificationId);
    }
}
