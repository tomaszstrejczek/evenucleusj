package com.evenucleus.client;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by tomeks on 2014-12-31.
 */
public class CacheEntry {
    @DatabaseField(id = true)
    public String Key;
    @DatabaseField
    public Date CachedUntil;
    @DatabaseField
    public String Data;

}
