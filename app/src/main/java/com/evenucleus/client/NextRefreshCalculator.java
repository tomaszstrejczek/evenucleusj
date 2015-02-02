package com.evenucleus.client;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Collection;

/**
 * Created by tomeks on 2015-02-02.
 */
public class NextRefreshCalculator {
    public DateTime Calculate(DateTime now, Collection<DateTime> cachedUntils) {
        Duration sum = new Duration(0);
        int cnt = 0;
        for(DateTime d: cachedUntils) {
            Duration dur = new Duration(now, d);
            if (dur.getMillis() > 0) {
                sum = sum.plus(dur);
                ++cnt;
            }
        }

        Duration avg = new Duration(sum.getMillis()/cnt);
        return now.plus(avg);
    }
}
