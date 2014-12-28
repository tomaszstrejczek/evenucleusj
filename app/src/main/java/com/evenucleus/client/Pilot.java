package com.evenucleus.client;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.Date;

/**
 * Created by tomeks on 2014-12-28.
 */
public class Pilot {
    @DatabaseField(generatedId = true)
    public int PilotId;

    @DatabaseField
    public String Name;
    @DatabaseField
    public String Url;

    @DatabaseField
    public String CurrentTrainingNameAndLevel;
    @DatabaseField
    public Date CurrentTrainingEnd;
    @DatabaseField
    public Date TrainingQueueEnd;
    @DatabaseField
    public boolean TrainingActive;

    @DatabaseField
    public int MaxManufacturingJobs;
    @DatabaseField
    public int MaxResearchJobs;

    @DatabaseField
    public int FreeManufacturingJobsNofificationCount;
    @DatabaseField
    public int FreeResearchJobsNofificationCount;

    @DatabaseField(canBeNull = false, foreign = true)
    public KeyInfo KeyInfo;

    @ForeignCollectionField
    public ForeignCollection<Skill> Skills;
}
