package com.evenucleus.evenucleus;

import com.evenucleus.client.DatabaseHelper;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * Created by tomeks on 2015-01-05.
 */
@EBean
public class MyDatabaseHelper extends DatabaseHelper {
    final Logger logger = LoggerFactory.getLogger(MyDatabaseHelper.class);

    public MyDatabaseHelper(Context context)
    {
        super();

        logger.debug("AfterInject");

        File databasePath = context.getDatabasePath("evenucleus1.db");
        logger.debug("Database path {}", databasePath.getPath());

        try {
            Initialize(databasePath.getPath());
        }
        catch (Exception e) {
            logger.warn("Fatal error", e);
        }
    }
}
