package com.evenucleus.client;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by tomeks on 2014-12-29.
 */
public class Category {
    @DatabaseField(generatedId = true)
    public int CategoryId;
    @DatabaseField
    public String Name;

}
