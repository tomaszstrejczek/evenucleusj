package com.evenucleus.client;


import java.util.Date;
import java.util.List;

/**
 * Created by tomeks on 2015-01-06.
 */
public class PilotDTO {
    public int PilotId;
    public long CharacterId;
    public String Name;
    public String CurrentTrainingNameAndLevel;
    public Date CurrentTrainingEnd;
    public Date TrainingQueueEnd;
    public boolean TrainingActive;
    public int MaxManufacturingJobs;
    public int MaxResearchJobs;
    public KeyInfo KeyInfo;
    public List<String> Skills;

}
