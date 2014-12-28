package com.evenucleus.client;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by tomeks on 2014-12-28.
 */
public class Skill {
    @DatabaseField(generatedId = true)
    public int SkillId;

    @DatabaseField(foreign = true, canBeNull = false)
    public Pilot Pilot;

    @DatabaseField
    public String SkillName;
}
