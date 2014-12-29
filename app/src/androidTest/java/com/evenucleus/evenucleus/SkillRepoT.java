package com.evenucleus.evenucleus;

import com.evenucleus.client.Corporation;
import com.evenucleus.client.Job;
import com.evenucleus.client.JobSummary;
import com.evenucleus.client.PendingNotification;
import com.evenucleus.client.PendingNotificationRepo;
import com.evenucleus.client.Pilot;
import com.evenucleus.client.PilotRepo;
import com.evenucleus.client.Skill;
import com.evenucleus.client.SkillRepo;
import com.evenucleus.client.UserData;

import junit.framework.Assert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tomeks on 2014-12-29.
 */
public class SkillRepoT extends TestBase {
    public void test_SkillsNotificationAdded() throws SQLException {
        PendingNotificationRepo pendingNotificationRepo = new PendingNotificationRepo(_localdb);
        SkillRepo skillRepo = new SkillRepo(_localdb, pendingNotificationRepo);
        PilotRepo pilotRepo = new PilotRepo(_localdb);

        // stage 1 - skills firstly seen - no notification expected
        UserData userData = new UserData();
        Pilot p = new Pilot();
        p.Name= "Pilot1";
        _localdb.getPilotDao().assignEmptyForeignCollection(p, "Skills");
        p.Skills.add(new Skill() {{ SkillName = "skill1"; }});
        p.Skills.add(new Skill() {{SkillName="skill2";}});
        userData.Pilots = Arrays.asList(p);
        userData.Corporations = new ArrayList<Corporation>();
        userData.Jobs = new ArrayList<Job>();
        userData.JobSummary = new JobSummary();

        pilotRepo.Update(userData);
        skillRepo.Update(userData);

        List<String> skills = skillRepo.GetForPilot(userData.Pilots.iterator().next().PilotId);
        Assert.assertEquals(2, skills.size());

        List<PendingNotification> notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(0, notifications.size());

        pilotRepo.Update(userData);
        skillRepo.Update(userData);

        skills = skillRepo.GetForPilot(userData.Pilots.iterator().next().PilotId);
        Assert.assertEquals(2, skills.size());

        notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(0, notifications.size());

        // stage 3 - added a skill
        UserData userData2 = new UserData();
        p = new Pilot();
        p.Name= "Pilot1";
        _localdb.getPilotDao().assignEmptyForeignCollection(p, "Skills");
        p.Skills.add(new Skill() {{SkillName = "skill1";}});
        p.Skills.add(new Skill() {{SkillName="skill2";}});
        p.Skills.add(new Skill() {{SkillName="skill3";}});
        userData2.Pilots = Arrays.asList(p);
        userData2.Corporations = new ArrayList<Corporation>();
        userData2.Jobs = new ArrayList<Job>();
        userData2.JobSummary = new JobSummary();

        pilotRepo.Update(userData2);
        skillRepo.Update(userData2);

        skills = skillRepo.GetForPilot(userData.Pilots.iterator().next().PilotId);
        Assert.assertEquals(3, skills.size());

        notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(1, notifications.size());
        PendingNotification n = notifications.iterator().next();
        Assert.assertEquals("Pilot1", n.Message);
        Assert.assertEquals("skill3 trained", n.Message2);
        pendingNotificationRepo.Remove(n.PendingNotificationId);

        // stage 4 - added two skills
        UserData userData3 = new UserData();
        p = new Pilot();
        p.Name= "Pilot1";
        _localdb.getPilotDao().assignEmptyForeignCollection(p, "Skills");
        p.Skills.add(new Skill() {{SkillName = "skill1";}});
        p.Skills.add(new Skill() {{SkillName="skill2";}});
        p.Skills.add(new Skill() {{SkillName="skill3";}});
        p.Skills.add(new Skill() {{SkillName="skill4";}});
        p.Skills.add(new Skill() {{SkillName="skill5";}});
        userData3.Pilots = Arrays.asList(p);
        userData3.Corporations = new ArrayList<Corporation>();
        userData3.Jobs = new ArrayList<Job>();
        userData3.JobSummary = new JobSummary();

        pilotRepo.Update(userData3);
        skillRepo.Update(userData3);

        skills = skillRepo.GetForPilot(userData.Pilots.iterator().next().PilotId);
        Assert.assertEquals(5, skills.size());

        notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(2, notifications.size());
        n = null;
        for(PendingNotification x: notifications)
            if (x.Message2.contains("skill4")) {n = x;break;}
        Assert.assertEquals("Pilot1", n.Message);
        Assert.assertEquals("skill4 trained", n.Message2);
        pendingNotificationRepo.Remove(n.PendingNotificationId);

        for(PendingNotification x: notifications)
            if (x.Message2.contains("skill5")) {n = x;break;}
        Assert.assertEquals("Pilot1", n.Message);
        Assert.assertEquals("skill5 trained", n.Message2);
        pendingNotificationRepo.Remove(n.PendingNotificationId);
    }

    public void test_SkillsNotificationRemoved() throws SQLException {
        PendingNotificationRepo pendingNotificationRepo = new PendingNotificationRepo(_localdb);
        SkillRepo skillRepo = new SkillRepo(_localdb, pendingNotificationRepo);
        PilotRepo pilotRepo = new PilotRepo(_localdb);

        // stage 1 - skills firstly seen - no notification expected
        UserData userData = new UserData();
        Pilot p = new Pilot();
        p.Name= "Pilot1";
        p.PilotId = 1;
        _localdb.getPilotDao().assignEmptyForeignCollection(p, "Skills");
        p.Skills.add(new Skill() {{SkillName = "skilla1";}});
        p.Skills.add(new Skill() {{SkillName="skillb2";}});
        p.Skills.add(new Skill() {{SkillName="skillc3";}});
        userData.Pilots = Arrays.asList(p);
        userData.Corporations = new ArrayList<Corporation>();
        userData.Jobs = new ArrayList<Job>();
        userData.JobSummary = new JobSummary();

        pilotRepo.Update(userData);
        skillRepo.Update(userData);

        List<String> skills = skillRepo.GetForPilot(userData.Pilots.iterator().next().PilotId);
        Assert.assertEquals(3, skills.size());

        List<PendingNotification> notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(0, notifications.size());

        // stage 2 - removed a skill
        UserData userData2 = new UserData();
        Pilot p2 = new Pilot();
        p2.Name= "Pilot1";
        p2.PilotId = 2;
        _localdb.getPilotDao().assignEmptyForeignCollection(p2, "Skills");
        p2.Skills.add(new Skill() {{SkillName = "skilla1";}});
        p2.Skills.add(new Skill() {{SkillName="skillb2";}});
        userData2.Pilots = Arrays.asList(p2);
        userData2.Corporations = new ArrayList<Corporation>();
        userData2.Jobs = new ArrayList<Job>();
        userData2.JobSummary = new JobSummary();

        pilotRepo.Update(userData2);
        skillRepo.Update(userData2);

        skills = skillRepo.GetForPilot(userData.Pilots.iterator().next().PilotId);
        Assert.assertEquals(2, skills.size());

        notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(1, notifications.size());
        PendingNotification n = notifications.iterator().next();
        Assert.assertEquals("Pilot1", n.Message);
        Assert.assertEquals("skillc3 removed", n.Message2);
        pendingNotificationRepo.Remove(n.PendingNotificationId);

        // stage 3 - removed two skills
        UserData userData3 = new UserData();
        Pilot p3 = new Pilot();
        p3.Name= "Pilot1";
        p3.PilotId = 3;
        _localdb.getPilotDao().assignEmptyForeignCollection(p3, "Skills");
        userData3.Pilots = Arrays.asList(p3);
        userData3.Corporations = new ArrayList<Corporation>();
        userData3.Jobs = new ArrayList<Job>();
        userData3.JobSummary = new JobSummary();

        pilotRepo.Update(userData3);
        skillRepo.Update(userData3);

        skills = skillRepo.GetForPilot(userData.Pilots.iterator().next().PilotId);
        Assert.assertEquals(0, skills.size());

        notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(2, notifications.size());
        n = null;
        for(PendingNotification x: notifications)
            if (x.Message2.contains("skilla1")) {n = x;break;}

        Assert.assertEquals("Pilot1", n.Message);
        Assert.assertEquals("skilla1 removed", n.Message2);
        pendingNotificationRepo.Remove(n.PendingNotificationId);

        n = null;
        for(PendingNotification x: notifications)
            if (x.Message2.contains("skillb2")) {n = x;break;}
        Assert.assertEquals("Pilot1", n.Message);
        Assert.assertEquals("skillb2 removed", n.Message2);
        pendingNotificationRepo.Remove(n.PendingNotificationId);
    }

    public void test_SkillsNotificationAddedRemoved() throws SQLException {
        PendingNotificationRepo pendingNotificationRepo = new PendingNotificationRepo(_localdb);
        SkillRepo skillRepo = new SkillRepo(_localdb, pendingNotificationRepo);
        PilotRepo pilotRepo = new PilotRepo(_localdb);

        // stage 1 - skills firstly seen - no notification expected
        UserData userData = new UserData();
        Pilot p = new Pilot();
        p.Name= "Pilot1";
        p.PilotId = 1;
        _localdb.getPilotDao().assignEmptyForeignCollection(p, "Skills");
        p.Skills.add(new Skill() {{SkillName = "skilla1";}});
        p.Skills.add(new Skill() {{SkillName="skillb2";}});
        p.Skills.add(new Skill() {{SkillName="skillc3";}});
        userData.Pilots = Arrays.asList(p);
        userData.Corporations = new ArrayList<Corporation>();
        userData.Jobs = new ArrayList<Job>();
        userData.JobSummary = new JobSummary();

        pilotRepo.Update(userData);
        skillRepo.Update(userData);

        List<String> skills = skillRepo.GetForPilot(userData.Pilots.iterator().next().PilotId);
        Assert.assertEquals(3, skills.size());

        List<PendingNotification> notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(0, notifications.size());

        // stage 2 - added and removed a skill
        UserData userData2 = new UserData();
        p = new Pilot();
        p.Name= "Pilot1";
        p.PilotId = 2;
        _localdb.getPilotDao().assignEmptyForeignCollection(p, "Skills");
        p.Skills.add(new Skill() {{SkillName = "skilla1";}});
        p.Skills.add(new Skill() {{SkillName="skillb2";}});
        p.Skills.add(new Skill() {{SkillName="skillx4";}});
        userData2.Pilots = Arrays.asList(p);
        userData2.Corporations = new ArrayList<Corporation>();
        userData2.Jobs = new ArrayList<Job>();
        userData2.JobSummary = new JobSummary();

        pilotRepo.Update(userData2);
        skillRepo.Update(userData2);

        skills = skillRepo.GetForPilot(userData.Pilots.iterator().next().PilotId);
        Assert.assertEquals(3, skills.size());

        notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(2, notifications.size());

        PendingNotification n = null;
        for(PendingNotification x: notifications)
            if (x.Message2.contains("skillc3")) {n = x;break;}
        Assert.assertEquals("Pilot1", n.Message);
        Assert.assertEquals("skillc3 removed", n.Message2);
        pendingNotificationRepo.Remove(n.PendingNotificationId);

        n = null;
        for(PendingNotification x: notifications)
            if (x.Message2.contains("skillx4")) {n = x;break;}
        Assert.assertEquals("Pilot1", n.Message);
        Assert.assertEquals("skillx4 trained", n.Message2);
        pendingNotificationRepo.Remove(n.PendingNotificationId);
    }

    public void test_SkillsNotificationLevelUp() throws SQLException {
        PendingNotificationRepo pendingNotificationRepo = new PendingNotificationRepo(_localdb);
        SkillRepo skillRepo = new SkillRepo(_localdb, pendingNotificationRepo);
        PilotRepo pilotRepo = new PilotRepo(_localdb);

        // stage 1 - skills firstly seen - no notification expected
        UserData userData = new UserData();
        Pilot p = new Pilot();
        p.Name= "Pilot1";
        p.PilotId = 1;
        _localdb.getPilotDao().assignEmptyForeignCollection(p, "Skills");
        p.Skills.add(new Skill() {{SkillName = "skill1";}});
        p.Skills.add(new Skill() {{SkillName="skill2";}});
        p.Skills.add(new Skill() {{SkillName="skill 3";}});
        userData.Pilots = Arrays.asList(p);
        userData.Corporations = new ArrayList<Corporation>();
        userData.Jobs = new ArrayList<Job>();
        userData.JobSummary = new JobSummary();

        pilotRepo.Update(userData);
        skillRepo.Update(userData);

        List<String> skills = skillRepo.GetForPilot(userData.Pilots.iterator().next().PilotId);
        Assert.assertEquals(3, skills.size());

        List<PendingNotification> notifications = pendingNotificationRepo.GetAll();
        Assert.assertEquals(0, notifications.size());

        // stage 2 - level up
        UserData userData2 = new UserData();
        p = new Pilot();
        p.Name= "Pilot1";
        p.PilotId = 2;
        _localdb.getPilotDao().assignEmptyForeignCollection(p, "Skills");
        p.Skills.add(new Skill() {{SkillName = "skill1";}});
        p.Skills.add(new Skill() {{SkillName="skill2";}});
        p.Skills.add(new Skill() {{SkillName="skill 4";}});
        userData2.Pilots = Arrays.asList(p);
        userData2.Corporations = new ArrayList<Corporation>();
        userData2.Jobs = new ArrayList<Job>();
        userData2.JobSummary = new JobSummary();

        pilotRepo.Update(userData2);
        skillRepo.Update(userData2);

        skills = skillRepo.GetForPilot(userData.Pilots.iterator().next().PilotId);
        Assert.assertEquals(3, skills.size());

        notifications = pendingNotificationRepo.GetAll();

        Assert.assertEquals(1, notifications.size());
        PendingNotification n = null;
        for(PendingNotification x: notifications)
            if (x.Message2.contains("skill 4")) {n = x;break;}
        Assert.assertEquals("Pilot1", n.Message);
        Assert.assertEquals("skill 4 trained", n.Message2);
        pendingNotificationRepo.Remove(n.PendingNotificationId);
    }
}
