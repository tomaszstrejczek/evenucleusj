package com.evenucleus.client;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

public class PendingNotification {
    public PendingNotification()
    {
        SentOn = new Date(1900, 1, 1);
    }

    @DatabaseField(generatedId = true)
    public int PendingNotificationId;

    @DatabaseField
    public String Message;
    @DatabaseField
    public String Message2;
    @DatabaseField
    public String Error;

    @DatabaseField
    public PendingNotificationStatus Status;

    @DatabaseField
    public Date CreatedOn;
    @DatabaseField
    public Date SentOn;

}
