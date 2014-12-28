package com.evenucleus.client;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by tomeks on 2014-12-28.
 */
public class Job {
    @DatabaseField(generatedId = true)
    public int JobId;

    @DatabaseField
    public String Owner;
    @DatabaseField
    public String JobDescription;
    @DatabaseField
    public String Url;
    @DatabaseField
    public Date JobEnd;
    @DatabaseField
    public int PercentageOfCompletion; // 0-100
    @DatabaseField
    public boolean JobCompleted;
    @DatabaseField
    public boolean IsManufacturing;
}
