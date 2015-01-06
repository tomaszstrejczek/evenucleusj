package com.evenucleus.client;

import android.util.Log;

import com.evenucleus.evenucleus.MyDatabaseHelper;
import com.j256.ormlite.dao.ForeignCollection;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomeks on 2014-12-29.
 */
@EBean
public class SkillRepo implements ISkillRepo {

    @Bean(MyDatabaseHelper.class)
    public DatabaseHelper _localdb;
    @Bean(PendingNotificationRepo.class)
    public IPendingNotificationRepo _pendingNotificationRepo;

    @Override
    public void Update(UserData data) throws SQLException {
        Log.d(SkillRepo.class.getName(), "Update");

        List<Pilot> pilots = _localdb.getPilotDao().queryForAll();
        for(Pilot p:pilots)
        {
            PilotDTO pd = null;
            for(PilotDTO p2: data.Pilots)
                if (p2.Name.equals(p.Name))
                {
                    pd = p2;
                    break;
                }
            ForeignCollection<Skill> storedSkills = p.Skills;
            boolean suspendNotification = storedSkills.size()== 0;   // suspend notification if the pilot is seen for the first time

            // remove skills
            List<Skill> toremove = new ArrayList<Skill>();
            List<String> trainedSkills = new ArrayList<String>();
            for(String s: pd.Skills) trainedSkills.add(s);
            for(Skill s: storedSkills)
                if (!trainedSkills.contains(s.SkillName))
                    toremove.add(s);

            // it is not expected that we need to remove a skill. Probably it could happen if skills are renamed or skills are lost due to clone kill
            // however if a skill is leveled up than we technically removed old level and replaced with a new level. No notification should be sent in such case
            for(Skill r:toremove)
            {
                boolean leveledUp = false;
                for(String x: pd.Skills)
                    if (x.length() == r.SkillName.length()
                            && x.substring(0, x.length()-1).equals(r.SkillName.substring(0, r.SkillName.length()-1)))
                    {
                        leveledUp = true;
                        break;
                    }

                if (!leveledUp)
                {
                    Log.d(SkillRepo.class.getName(), String.format("removing skill %s for %s", "SkillLocalRepo::Update", r.SkillName, p.Name));
                    _pendingNotificationRepo.IssueNew(p.Name, String.format("%s removed", r.SkillName));
                }
                _localdb.getSkillDao().deleteById(r.SkillId);
            }

            List<Skill> toadd = new ArrayList<Skill>();
            List<String> storedSkillNames = new ArrayList<String>();
            for(Skill x: storedSkills) storedSkillNames.add(x.SkillName);

            for(String x: pd.Skills)
                if (!storedSkillNames.contains(x))
                {
                    Skill sk = new Skill();
                    sk.SkillName = x;
                    sk.Pilot = p;
                    toadd.add(sk);
                }

            for(Skill a:toadd)
            {
                Log.d(SkillRepo.class.getName(), String.format("adding skill %s for %s", a.SkillName, p.Name));
                _localdb.getSkillDao().createOrUpdate(a);
                if (!suspendNotification)
                {
                    _pendingNotificationRepo.IssueNew(p.Name, String.format("%s trained", a.SkillName));
                }
            }
        }
    }

    @Override
    public List<String> GetForPilot(int pilotid) throws SQLException {
        Log.d(SkillRepo.class.getName(), String.format("GetForPilot %d", pilotid));

        List<String> result = new ArrayList<String>();
        List<Skill> skills = _localdb.getSkillDao().queryForEq("PilotId", pilotid);

        for(Skill x:skills) result.add(x.SkillName);
        return result;
    }
}
