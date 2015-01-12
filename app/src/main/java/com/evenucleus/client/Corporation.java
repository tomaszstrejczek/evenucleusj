package com.evenucleus.client;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by tomeks on 2014-12-28.
 */
public class Corporation {
    @DatabaseField(generatedId = true)
    public int CorporationId;

    @DatabaseField
    public String Name;

    @DatabaseField(canBeNull = true,foreign = true, foreignAutoRefresh = true)
    public KeyInfo KeyInfo;
}
