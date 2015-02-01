package com.evenucleus.evenucleus;

import android.content.Context;

import com.logentries.android.AndroidLogger;

import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import java.util.Map;
import android.util.Log;

/**
 * Created by tomeks on 2015-02-01.
 */
public class LogentriesSender implements ReportSender {

    private final AndroidLogger logger;

    private static final String NEW_LINE = System.getProperty("line.separator");
    public static final String LOG_LEVEL_KEY = "LOGLEVEL";

    public LogentriesSender(Context appContext, boolean immediateUpload) {
        logger = AndroidLogger.getLogger(appContext, MainActivity.LogentriesToken);
        logger.setImmediateUpload(immediateUpload);
    }

    @Override
    public void send(Context context, CrashReportData report) throws ReportSenderException {
        int logLevel = Log.ERROR;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<ReportField, String> reportField : report.entrySet()) {
            String key = reportField.getKey().name();
            if (key.equals(LOG_LEVEL_KEY)) {
                try {
                    logLevel = Integer.parseInt(reportField.getValue());
                } catch (Exception e) {
                    Log.e("LOGGER","failed to parse log level", e);
                }
            } else {
                sb.append(key);
                sb.append("=");
                sb.append(reportField.getValue());
                sb.append(NEW_LINE);
            }
        }

        switch (logLevel) {

            case Log.DEBUG:
                logger.debug(sb.toString());
                break;

            case Log.ERROR:
                logger.error(sb.toString());
                break;

            case Log.VERBOSE:
                logger.verbose(sb.toString());
                break;

            case Log.INFO:
                logger.info(sb.toString());
                break;

            case Log.WARN:
                logger.warn(sb.toString());
                break;

            default:
                logger.verbose(sb.toString());
                break;
        }

    }
}