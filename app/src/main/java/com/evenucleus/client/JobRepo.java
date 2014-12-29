package com.evenucleus.client;

import android.os.Debug;
import android.util.Log;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by tomeks on 2014-12-29.
 */
public class JobRepo implements IJobRepo {

    DatabaseHelper _localdb;
    IPilotRepo _pilotRepo;
    IPendingNotificationRepo _pendingNotificationRepo;

    public JobRepo(DatabaseHelper localdb, IPilotRepo pilotRepo, IPendingNotificationRepo pendingNotificationRepo) {
        _localdb = localdb;
        _pilotRepo = pilotRepo;
        _pendingNotificationRepo = pendingNotificationRepo;
    }
    @Override
    public void Update(UserData data) throws SQLException {
        Log.d(JobRepo.class.getName(), "Update");
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
            Pilot pd = null;
            for(Pilot x: data.Pilots)
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
        Log.d(JobRepo.class.getName(), "GetAll");
        return _localdb.getJobDao().queryForAll();
    }
}
