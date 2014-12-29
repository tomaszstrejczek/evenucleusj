package com.evenucleus.evenucleus;

import com.evenucleus.client.Corporation;
import com.evenucleus.client.Job;
import com.evenucleus.client.JobSummary;
import com.evenucleus.client.PendingNotification;
import com.evenucleus.client.PendingNotificationRepo;
import com.evenucleus.client.Pilot;
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

    public void test_PilotLocalRepo_UpdatePilots1() throws SQLException {
        PendingNotificationRepo pendingNotificationRepo = new PendingNotificationRepo(_localdb);
        PilotRepo repo = new PilotRepo(_localdb);

        // stage 1
        UserData userData = new UserData();
        Pilot p = new Pilot();
        p.Name= "Pilot1";
        _localdb.getPilotDao().assignEmptyForeignCollection(p, "Skills");
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
        Pilot p2 = new Pilot();
        p2.Name= "Pilot2";
        _localdb.getPilotDao().assignEmptyForeignCollection(p2, "Skills");
        Pilot p3 = new Pilot();
        p3.Name= "Pilot3";
        _localdb.getPilotDao().assignEmptyForeignCollection(p3, "Skills");
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

}
