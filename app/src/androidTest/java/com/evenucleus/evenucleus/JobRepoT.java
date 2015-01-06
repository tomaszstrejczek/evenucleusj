package com.evenucleus.evenucleus;

import com.evenucleus.client.Corporation;
import com.evenucleus.client.Job;
import com.evenucleus.client.JobRepo;
import com.evenucleus.client.JobSummary;
import com.evenucleus.client.PendingNotification;
import com.evenucleus.client.PendingNotificationRepo;
import com.evenucleus.client.Pilot;
import com.evenucleus.client.PilotDTO;
import com.evenucleus.client.PilotRepo;
import com.evenucleus.client.SkillRepo;
import com.evenucleus.client.UserData;

import junit.framework.Assert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by tomeks on 2014-12-29.
 */
public class JobRepoT extends TestBase {
    public void test_JobsNotification() throws SQLException {
        PendingNotificationRepo pendingNotificationRepo = new PendingNotificationRepo();
        pendingNotificationRepo._localdb = _localdb;
        PilotRepo pilotsRepo = new PilotRepo();
        pilotsRepo._localdb = _localdb;
        JobRepo jobsRepo = new JobRepo(_localdb, pilotsRepo, pendingNotificationRepo);

        // stage 1
        UserData userData = new UserData();
        PilotDTO p = new PilotDTO();
        p.Name= "Pilot1";
        p.PilotId = 1;
        p.MaxManufacturingJobs = 1;
        p.Skills = new ArrayList<String>();
        userData.Pilots = Arrays.asList(p);
        userData.Corporations = new ArrayList<Corporation>();
        userData.Jobs = new ArrayList<Job>();
        userData.JobSummary = new JobSummary();

        pilotsRepo.Update(userData);
        jobsRepo.Update(userData);

        List<PendingNotification> notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(1, notifications.size());
        PendingNotification n = notifications.iterator().next();
        Assert.assertEquals("Pilot1", n.Message);
        Assert.assertEquals("1 free manufacturing slots", n.Message2);
        pendingNotificationRepo.Remove(n.PendingNotificationId);

        // stage 2
        // Same info provided and expected no further notifications
        pilotsRepo.Update(userData);
        jobsRepo.Update(userData);

        notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(0, notifications.size());
        List<Pilot> pilots = pilotsRepo.GetAll();
        Assert.assertEquals(1, pilots.size());
        Assert.assertEquals(1, pilots.iterator().next().FreeManufacturingJobsNofificationCount);

        // stage 3
        // First job started - still no notification - but notification flag shoud be reset
        UserData userData2 = new UserData();
        p = new PilotDTO();
        p.Name= "Pilot1";
        p.PilotId = 2;
        p.MaxManufacturingJobs = 1;
        p.Skills = new ArrayList<String>();
        userData2.Pilots = Arrays.asList(p);
        userData2.Corporations = new ArrayList<Corporation>();
        Job j = new Job();
        j.IsManufacturing = true;
        j.Owner = "Pilot1";
        userData2.Jobs = Arrays.asList(j);
        userData2.JobSummary = new JobSummary();

        pilotsRepo.Update(userData2);
        jobsRepo.Update(userData2);

        notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(0, notifications.size());
        pilots = pilotsRepo.GetAll();
        Assert.assertEquals(1, pilots.size());
        Assert.assertEquals(0, pilots.iterator().next().FreeManufacturingJobsNofificationCount);

        // stage 4
        // No running jobs - notification expected
        pilotsRepo.Update(userData);
        jobsRepo.Update(userData);

        notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(1, notifications.size());
        n = notifications.iterator().next();
        Assert.assertEquals("Pilot1", n.Message);
        Assert.assertEquals("1 free manufacturing slots", n.Message2);
        pendingNotificationRepo.Remove(n.PendingNotificationId);
    }

    public void test_ResearchNotification() throws SQLException {
        PendingNotificationRepo pendingNotificationRepo = new PendingNotificationRepo();
        pendingNotificationRepo._localdb = _localdb;
        PilotRepo pilotsRepo = new PilotRepo();
        pilotsRepo._localdb = _localdb;
        JobRepo jobsRepo = new JobRepo(_localdb, pilotsRepo, pendingNotificationRepo);

        // stage 1
        UserData userData = new UserData();
        PilotDTO p = new PilotDTO();
        p.Name= "Pilot1";
        p.PilotId = 1;
        p.MaxResearchJobs= 1;
        p.Skills = new ArrayList<String>();
        userData.Pilots = Arrays.asList(p);
        userData.Corporations = new ArrayList<Corporation>();
        userData.Jobs = new ArrayList<Job>();
        userData.JobSummary = new JobSummary();

        pilotsRepo.Update(userData);
        jobsRepo.Update(userData);

        List<PendingNotification> notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(1, notifications.size());
        PendingNotification n = notifications.iterator().next();
        Assert.assertEquals("Pilot1", n.Message);
        Assert.assertEquals("1 free research slots", n.Message2);
        pendingNotificationRepo.Remove(n.PendingNotificationId);

        // stage 2
        // Same info provided and expected no further notifications
        pilotsRepo.Update(userData);
        jobsRepo.Update(userData);

        notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(0, notifications.size());
        List<Pilot> pilots = pilotsRepo.GetAll();
        Assert.assertEquals(1, pilots.size());
        Assert.assertEquals(1, pilots.iterator().next().FreeResearchJobsNofificationCount);

        // stage 3
        // First job started - still no notification - but notification flag shoud be reset
        UserData userData2 = new UserData();
        p = new PilotDTO();
        p.Name= "Pilot1";
        p.PilotId = 2;
        p.MaxResearchJobs= 1;
        p.Skills = new ArrayList<String>();
        userData2.Pilots = Arrays.asList(p);
        userData2.Corporations = new ArrayList<Corporation>();
        Job j = new Job();
        j.IsManufacturing = false;
        j.Owner = "Pilot1";
        userData2.Jobs = Arrays.asList(j);
        userData2.JobSummary = new JobSummary();

        pilotsRepo.Update(userData2);
        jobsRepo.Update(userData2);

        notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(0, notifications.size());
        pilots = pilotsRepo.GetAll();
        Assert.assertEquals(1, pilots.size());
        Assert.assertEquals(0, pilots.iterator().next().FreeResearchJobsNofificationCount);

        // stage 4
        // No running jobs - notification expected
        pilotsRepo.Update(userData);
        jobsRepo.Update(userData);

        notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(1, notifications.size());
        n = notifications.iterator().next();
        Assert.assertEquals("Pilot1", n.Message);
        Assert.assertEquals("1 free research slots", n.Message2);
        pendingNotificationRepo.Remove(n.PendingNotificationId);
    }

}
