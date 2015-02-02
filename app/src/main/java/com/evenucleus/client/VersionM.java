package com.evenucleus.client;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by tomeks on 2014-12-28.
 */
@DatabaseTable(tableName = "VersionM")
public class VersionM {
    @DatabaseField(generatedId = true)
    public int VersionId;
    @DatabaseField(uniqueIndex = true)
    public String Name;
}
