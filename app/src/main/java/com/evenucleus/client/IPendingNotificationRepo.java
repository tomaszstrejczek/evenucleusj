package com.evenucleus.client;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by tomeks on 2014-12-29.
 */
public interface IPendingNotificationRepo {
    void IssueNew(String message, String message2) throws SQLException;
    List<PendingNotification> GetAll() throws SQLException;
    void Remove(int notificationId) throws SQLException;
}
