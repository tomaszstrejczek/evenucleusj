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

    @DatabaseField
    public long EveCorporationId;

    @DatabaseField(canBeNull = true,foreign = true, foreignAutoRefresh = true)
    public KeyInfo KeyInfo;

    public String getUrl() {
        return String.format("https://image.eveonline.com/Corporation/%d_64.png", EveCorporationId);
    }
}
