package com.evenucleus.evenucleus;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.InstrumentationTestCase;

import com.evenucleus.client.DatabaseHelper;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by tomeks on 2014-12-28.
 */
public class TestBase extends InstrumentationTestCase {

    protected DatabaseHelper _localdb;

    protected Context getContext()
    {
        return getInstrumentation().getContext();
    }


    @Override
    public void setUp() throws Exception {
        super.setUp();

        File databasePath = File.createTempFile("evenucleus", "db", getContext().getFilesDir());

        _localdb = new DatabaseHelper(databasePath.getPath());
        _localdb.Initialize();
    }
}
