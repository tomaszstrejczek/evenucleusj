package com.evenucleus.client;

import java.util.Date;
import java.util.List;

/**
 * Created by tomeks on 2014-12-28.
 */
public class UserData {
    public List<Pilot> Pilots;
    public List<Corporation> Corporations;
    public List<Job> Jobs;
    public JobSummary JobSummary;
    public Date CachedUntilUTC;
}
