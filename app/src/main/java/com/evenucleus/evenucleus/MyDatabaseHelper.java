package com.evenucleus.evenucleus;

import com.evenucleus.client.DatabaseHelper;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * Created by tomeks on 2015-01-05.
 */
@EBean
public class MyDatabaseHelper extends DatabaseHelper {
    public MyDatabaseHelper(Context context)
    {
        super();

        Log.d(MyDatabaseHelper.class.getName(), "AfterInject");

        File databasePath = context.getDatabasePath("evenucleus6.db");
        Log.d(MyDatabaseHelper.class.getName(), String.format("Database path %s", databasePath.getPath()));

        try {
            Initialize(databasePath.getPath());
        }
        catch (Exception e) {
            Log.e(MyDatabaseHelper.class.getName(), String.format("Fatal error %s: %s", e.toString(), e.getStackTrace().toString()));
        }
    }
}
