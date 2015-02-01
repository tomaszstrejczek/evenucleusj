package com.evenucleus.client;

import android.content.Context;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import com.logentries.android.AndroidLogger;

/**
 * Created by tomeks on 2015-02-01.
 */
@EBean(scope = EBean.Scope.Singleton)
public class MyLogger {

    @RootContext
    Context context;

    private AndroidLogger _logger;

    @AfterInject
    public void afterInject() {
        _logger = AndroidLogger.getLogger(context, "4b5ef73c-f5c7-4b6c-a7c7-01f40b578910", false);
    }

    public void debug(String logMessage) {
        _logger.debug(logMessage);
    }

}
