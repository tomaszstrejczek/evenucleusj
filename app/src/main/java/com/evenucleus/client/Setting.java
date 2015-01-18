package com.evenucleus.client;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by tomeks on 2015-01-18.
 */
public class Setting {
    @DatabaseField(id=true)
    public String Key;
    @DatabaseField
    public String StringValue;
    @DatabaseField
    public Integer IntegerValue;
    @DatabaseField
    public Date DateValue;
    @DatabaseField
    public Boolean BooleanValue;
}
