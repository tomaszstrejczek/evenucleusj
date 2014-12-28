package com.evenucleus.client;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by tomeks on 2014-12-28.
 */
public class KeyInfo
{
    @DatabaseField(id=true)
    public int KeyId;

    @DatabaseField
    public String VCode;
}
