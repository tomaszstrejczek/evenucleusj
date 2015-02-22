package com.evenucleus.client;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Collection;

/**
 * Created by tomeks on 2015-02-02.
 */
public class NextRefreshCalculator {
    public DateTime Calculate(DateTime now, Collection<DateTime> cachedUntils, int frequencyInMinutes) {
        if (frequencyInMinutes == 0)
            return null;

        return now.plusMinutes(frequencyInMinutes);
    }
}
