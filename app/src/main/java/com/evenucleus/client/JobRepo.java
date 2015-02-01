package com.evenucleus.client;

import android.os.Debug;
import android.util.Log;

import com.evenucleus.evenucleus.MyDatabaseHelper;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by tomeks on 2014-12-29.
 */
@EBean
public class JobRepo implements IJobRepo {
    final Logger logger = LoggerFactory.getLogger(JobRepo.class);

    @Bean(MyDatabaseHelper.class)
    public DatabaseHelper _localdb;

    @Bean(PilotRepo.class)
    public IPilotRepo _pilotRepo;

    @Bean(PendingNotificationRepo.class)
    public IPendingNotificationRepo _pendingNotificationRepo;

    @Override
    public void Update(UserData data) throws SQLException {
        logger.debug("Update");
        updateRepo(data);
        updateNotifications(data);
    }

    private void updateRepo(UserData data) throws SQLException {
        _localdb._database.execSQL("delete from Job");

        for (Job d:data.Jobs)
            _localdb.getJobDao().createOrUpdate(d);
    }

    private void updateNotifications(UserData data) throws SQLException {
        List<Pilot> pilots = _pilotRepo.GetAll();

        for(Pilot p:pilots)
        {
            PilotDTO pd = null;
            for(PilotDTO x: data.Pilots)
                if (x.Name.equals(p.Name))
                {
                    pd = x;
                    break;
                }

            assert pd!=null;

            int actualManufacturingCount = 0;
            for(Job x:data.Jobs)
                if (x.Owner.equals(p.Name) && x.IsManufacturing)
                    ++actualManufacturingCount;

            int actualResearchCount = 0;
            for(Job x:data.Jobs)
                if (x.Owner.equals(p.Name) && !x.IsManufacturing)
                    ++actualResearchCount;

            if (p.FreeManufacturingJobsNofificationCount > 0)
            {
                if (actualManufacturingCount >= pd.MaxManufacturingJobs)
                {
                    Log.d(JobRepo.class.getName(), String.format("reset notification - maximum number of jobs running %s", p.Name));
                    _pilotRepo.SetFreeManufacturingJobsNofificationCount(p.PilotId, 0);                  // reset notification - maximum number of jobs running
                }
            }
            else
            {
                if (actualManufacturingCount < pd.MaxManufacturingJobs)
                {   // notify about free manufacturing slots
                    Log.d(JobRepo.class.getName(), String.format("{scheduling man notification for %s", p.Name));
                    _pendingNotificationRepo.IssueNew(p.Name,
                            String.format("%d free manufacturing slots",
                                    pd.MaxManufacturingJobs - actualManufacturingCount));
                    _pilotRepo.SetFreeManufacturingJobsNofificationCount(p.PilotId, 1);
                }
            }

            if (p.FreeResearchJobsNofificationCount > 0)
            {
                if (actualResearchCount >= pd.MaxResearchJobs)
                {
                    Log.d(JobRepo.class.getName(), String.format("resetting notification for %s", p.Name));
                    _pilotRepo.SetFreeResearchJobsNofificationCount(p.PilotId,0);                  // reset notification - maximum number of jobs running
                }
            }
            else
            {
                if (actualResearchCount < pd.MaxResearchJobs)
                {   // notify about free research slots
                    Log.d(JobRepo.class.getName(), String.format("Scheduling research notification for %s", p.Name));
                    _pendingNotificationRepo.IssueNew(p.Name,
                            String.format("%d free research slots", pd.MaxResearchJobs - actualResearchCount));
                    _pilotRepo.SetFreeResearchJobsNofificationCount(p.PilotId, 1);
                }
            }
        }
    }


    @Override
    public List<Job> GetAll() throws SQLException {
        logger.debug("GetAll");
        return _localdb.getJobDao().queryForAll();
    }
}
