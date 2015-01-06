package com.evenucleus.evenucleus;

import com.evenucleus.client.Corporation;
import com.evenucleus.client.CorporationRepo;
import com.evenucleus.client.Job;
import com.evenucleus.client.JobSummary;
import com.evenucleus.client.PendingNotification;
import com.evenucleus.client.PendingNotificationRepo;
import com.evenucleus.client.Pilot;
import com.evenucleus.client.PilotDTO;
import com.evenucleus.client.PilotRepo;
import com.evenucleus.client.UserData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

/**
 * Created by tomeks on 2014-12-29.
 */
public class PilotRepoT extends TestBase {

    public void test_UpdatePilots1() throws SQLException {
        PendingNotificationRepo pendingNotificationRepo = new PendingNotificationRepo();
        pendingNotificationRepo._localdb = _localdb;
        PilotRepo repo = new PilotRepo();
        repo._localdb = _localdb;

        // stage 1
        UserData userData = new UserData();
        PilotDTO p = new PilotDTO();
        p.Name= "Pilot1";
        p.Skills = new ArrayList<String>();
        userData.Pilots = Arrays.asList(p);
        userData.Corporations = new ArrayList<Corporation>();
        userData.Jobs = new ArrayList<Job>();
        userData.JobSummary = new JobSummary();

        repo.Update(userData);
        Assert.assertTrue(0 != userData.Pilots.iterator().next().PilotId);

        List<Pilot> pilots = repo.GetAll();
        Assert.assertEquals(1, pilots.size());
        Assert.assertEquals("Pilot1", pilots.iterator().next().Name);

        // stage 2
        UserData userData2 = new UserData();
        PilotDTO p2 = new PilotDTO();
        p2.Name= "Pilot2";
        p2.Skills = new ArrayList<String>();
        PilotDTO p3 = new PilotDTO();
        p3.Name= "Pilot3";
        p3.Skills = new ArrayList<String>();
        userData2.Pilots = Arrays.asList(p2, p3);
        userData2.Corporations = new ArrayList<Corporation>();
        userData2.Jobs = new ArrayList<Job>();
        userData2.JobSummary = new JobSummary();

        repo.Update(userData2);
        Assert.assertTrue(0 != userData2.Pilots.iterator().next().PilotId);
        Assert.assertTrue(0 != userData2.Pilots.listIterator(1).next().PilotId);
        Assert.assertTrue(userData2.Pilots.iterator().next().PilotId != userData2.Pilots.listIterator(1).next().PilotId);

        pilots = repo.GetAll();
        Assert.assertEquals(2, pilots.size());
        Pilot f2 = null;
        Pilot f3 = null;
        for(Pilot x:pilots)
            if (x.Name.equals("Pilot2")) f2 = x;
            else
                if (x.Name.equals("Pilot3")) f3 = x;
        Assert.assertNotNull(f2);
        Assert.assertNotNull(f3);

        // No notifications expected
        List<PendingNotification> n = pendingNotificationRepo.GetAll();
        Assert.assertEquals(0, n.size());
    }

    public void test_UpdateCorporations() throws SQLException {
        CorporationRepo repo = new CorporationRepo();
        repo._localdb = _localdb;

        // stage 1
        UserData userData = new UserData();
        userData.Pilots = new ArrayList<PilotDTO>();
        Corporation c = new Corporation();
        c.Name= "Corpo1";
        userData.Corporations = Arrays.asList(c);
        userData.Jobs = new ArrayList<Job>();
        userData.JobSummary = new JobSummary();

        repo.Update(userData);
        Assert.assertTrue(0 != userData.Corporations.iterator().next().CorporationId);

        List<Corporation> corporations = repo.GetAll();
        Assert.assertEquals(1, corporations.size());
        Assert.assertEquals("Corpo1", corporations.iterator().next().Name);

        // stage 2
        UserData userData2 = new UserData();
        userData2.Pilots = new ArrayList<PilotDTO>();
        Corporation c2 = new Corporation();
        c2.Name= "Corpo2";
        Corporation c3 = new Corporation();
        c3.Name= "Corpo3";
        userData2.Corporations = Arrays.asList(c2, c3);
        userData2.Jobs = new ArrayList<Job>();
        userData2.JobSummary = new JobSummary();

        repo.Update(userData2);
        Assert.assertTrue(0 != userData2.Corporations.iterator().next().CorporationId);
        Assert.assertTrue(0 != userData2.Corporations.listIterator(1).next().CorporationId);
        Assert.assertTrue(userData2.Corporations.iterator().next().CorporationId != userData2.Corporations.listIterator(1).next().CorporationId);

        corporations = repo.GetAll();
        Assert.assertEquals(2, corporations.size());
        Corporation f2 = null, f3 = null;
        for(Corporation corpo: corporations)
            if (corpo.Name.equals("Corpo2")) f2 = corpo;
            else if (corpo.Name.equals("Corpo3")) f3 = corpo;
        Assert.assertNotNull(f2);
        Assert.assertNotNull(f3);
    }

    public void test_UpdateCorporations2() throws SQLException {
        CorporationRepo repo = new CorporationRepo();
        repo._localdb = _localdb;

        // stage 1
        UserData userData = new UserData();
        userData.Pilots = new ArrayList<PilotDTO>();
        Corporation c = new Corporation();
        c.Name= "Corpo1";
        userData.Corporations = Arrays.asList(c);
        userData.Jobs = new ArrayList<Job>();
        userData.JobSummary = new JobSummary();

        repo.Update(userData);
        Assert.assertTrue(0 != userData.Corporations.iterator().next().CorporationId);

        List<Corporation> corporations = repo.GetAll();
        Assert.assertEquals(1, corporations.size());
        Assert.assertEquals("Corpo1", corporations.iterator().next().Name);

        // stage 2
        UserData userData2 = new UserData();
        userData2.Pilots = new ArrayList<PilotDTO>();
        userData2.Corporations = new ArrayList<Corporation>();
        userData2.Jobs = new ArrayList<Job>();
        userData2.JobSummary = new JobSummary();

        repo.Update(userData2);

        corporations = repo.GetAll();
        Assert.assertEquals(0, corporations.size());
    }

    public void test_UpdatePilotsAndCorporations() throws SQLException {
        PendingNotificationRepo pendingNotificationRepo = new PendingNotificationRepo();
        pendingNotificationRepo._localdb = _localdb;
        PilotRepo pilotRepo = new PilotRepo();
        pilotRepo._localdb = _localdb;
        CorporationRepo corpoRepo = new CorporationRepo();
        corpoRepo._localdb = _localdb;

        // stage 1
        UserData userData = new UserData();
        PilotDTO p = new PilotDTO();
        p.Name= "Pilot1";
        p.Skills = new ArrayList<String>();
        userData.Pilots = Arrays.asList(p);
        Corporation c = new Corporation();
        c.Name= "Corpo1";
        userData.Corporations = Arrays.asList(c);
        userData.Jobs = new ArrayList<Job>();
        userData.JobSummary = new JobSummary();

        pilotRepo.Update(userData);
        corpoRepo.Update(userData);

        List<Pilot> pilots = pilotRepo.GetAll();
        Assert.assertEquals(1, pilots.size());
        Assert.assertEquals("Pilot1", pilots.iterator().next().Name);

        List<Corporation> corporations = corpoRepo.GetAll();
        Assert.assertEquals(1, corporations.size());
        Assert.assertEquals("Corpo1", corporations.iterator().next().Name);

        // stage 2
        UserData userData2 = new UserData();
        userData2.Pilots = new ArrayList<PilotDTO>();
        userData2.Corporations = new ArrayList<Corporation>();
        userData2.Jobs = new ArrayList<Job>();
        userData2.JobSummary = new JobSummary();

        pilotRepo.Update(userData2);
        corpoRepo.Update(userData2);
        pilots = pilotRepo.GetAll();
        Assert.assertEquals(0, pilots.size());
        corporations = corpoRepo.GetAll();
        Assert.assertEquals(0, corporations.size());

        // No notifications expected
        List<PendingNotification> notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(0, notifications.size());
    }

    public void test_SetMethods() throws SQLException {
        PendingNotificationRepo pendingNotificationRepo = new PendingNotificationRepo();
        pendingNotificationRepo._localdb = _localdb;
        PilotRepo repo = new PilotRepo();
        repo._localdb = _localdb;

        // stage 1
        UserData userData = new UserData();
        PilotDTO p2 = new PilotDTO();
        p2.Name= "Pilot1";
        p2.Skills = new ArrayList<String>();
        PilotDTO p3 = new PilotDTO();
        p3.Name= "Pilot2";
        p3.Skills = new ArrayList<String>();
        userData.Pilots = Arrays.asList(p2, p3);
        userData.Corporations = new ArrayList<Corporation>();
        userData.Jobs = new ArrayList<Job>();
        userData.JobSummary = new JobSummary();

        repo.Update(userData);
        Assert.assertTrue(0 != userData.Pilots.iterator().next().PilotId);

        repo.SetFreeManufacturingJobsNofificationCount(userData.Pilots.iterator().next().PilotId, 10);
        repo.SetFreeResearchJobsNofificationCount(userData.Pilots.listIterator(1).next().PilotId, 20);

        List<Pilot> pilots = repo.GetAll();
        Assert.assertEquals(2, pilots.size());
        Pilot f1 = null, f2 = null;
        for(Pilot p:pilots)
            if (p.PilotId == userData.Pilots.iterator().next().PilotId)
                f1 = p;
            else if (p.PilotId == userData.Pilots.listIterator(1).next().PilotId)
                f2 = p;

        Assert.assertNotNull(f1);
        Assert.assertNotNull(f2);
        Assert.assertEquals(10, f1.FreeManufacturingJobsNofificationCount);
        Assert.assertEquals(0, f1.FreeResearchJobsNofificationCount);
        Assert.assertEquals(20, f2.FreeResearchJobsNofificationCount);
        Assert.assertEquals(0, f2.FreeManufacturingJobsNofificationCount);
    }

}
