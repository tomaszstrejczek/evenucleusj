package com.evenucleus.client;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by tomeks on 2014-12-28.
 */
public class Version {
    @DatabaseField(generatedId = true)
    public int VersionId;
    @DatabaseField(uniqueIndex = true)
    public String Name;
}
