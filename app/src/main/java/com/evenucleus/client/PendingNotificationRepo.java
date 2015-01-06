package com.evenucleus.client;

import android.util.Log;

import com.evenucleus.evenucleus.MyDatabaseHelper;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by tomeks on 2014-12-29.
 */
@EBean
public class PendingNotificationRepo implements IPendingNotificationRepo{

    @Bean(MyDatabaseHelper.class)
    public DatabaseHelper _localdb;

    @Override
    public void IssueNew(String message, String message2) throws SQLException {
        Log.d(PendingNotificationRepo.class.getName(), String.format("Issue new %s %s", message, message2));

        PendingNotification n = new PendingNotification();
        n.Status = PendingNotificationStatus.Ready;
        n.CreatedOn = new Date();
        n.Message = message;
        n.Message2 = message2;

        _localdb.getPendingNotificationDao().createOrUpdate(n);
    }

    @Override
    public List<PendingNotification> GetAll() throws SQLException {
        Log.d(PendingNotificationRepo.class.getName(), "GetAll");
        return _localdb.getPendingNotificationDao().queryForAll();
    }

    @Override
    public void Remove(int notificationId) throws SQLException {
        Log.d(PendingNotificationRepo.class.getName(), String.format("Remove %d", notificationId));
        _localdb.getPendingNotificationDao().deleteById(notificationId);
    }
}
