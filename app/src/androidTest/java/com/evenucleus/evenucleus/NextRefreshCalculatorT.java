package com.evenucleus.evenucleus;

import com.evenucleus.client.NextRefreshCalculator;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Arrays;

/**
 * Created by tomeks on 2015-02-02.
 */
public class NextRefreshCalculatorT {
/*
    public void test_Simple() {
        DateTime now = new DateTime(2000, 1, 1, 10, 10, 0);
        DateTime case1 = new DateTime(2000, 1, 1, 10, 10, 10);

        DateTime result = new NextRefreshCalculator().Calculate(now, Arrays.asList(case1), 0);

        Assert.assertEquals(case1, result);
    }

    public void test_SameAsNow() {
        DateTime now = new DateTime(2000, 1, 1, 10, 10, 0);

        DateTime result = new NextRefreshCalculator().Calculate(now, Arrays.asList(now), 0);

        Assert.assertEquals(now, result);
    }

    public void test_Simple2() {
        DateTime now = new DateTime(2000, 1, 1, 10, 10, 0);
        DateTime case1 = new DateTime(2000, 1, 1, 10, 1, 0);

        DateTime result = new NextRefreshCalculator().Calculate(now, Arrays.asList(case1, case1), 1);

        Assert.assertEquals(case1, result);
    }

    public void test_Simple2b() {
        DateTime now = new DateTime(2000, 1, 1, 10, 10, 0);
        DateTime case1 = new DateTime(2000, 1, 1, 10, 10, 0);
        DateTime case2 = new DateTime(2000, 1, 1, 10, 20, 0);
        DateTime exp = new DateTime(2000, 1, 1, 10, 10, 15);

        DateTime result = new NextRefreshCalculator().Calculate(now, Arrays.asList(case1, case2), 5);

        Assert.assertEquals(exp, result);
    }

    public void test_Simple2c() {
        DateTime now = new DateTime(2000, 1, 1, 10, 10, 0);
        DateTime case1 = new DateTime(2000, 1, 1, 10, 10, 0);
        DateTime case2 = new DateTime(2000, 1, 1, 11, 10, 0);
        DateTime exp = new DateTime(2000, 1, 1, 10, 40, 0);

        DateTime result = new NextRefreshCalculator().Calculate(now, Arrays.asList(case1, case2));

        Assert.assertEquals(exp, result);
    }

    public void test_Simple3() {
        DateTime now = new DateTime(2000, 1, 1, 10, 10, 0);
        DateTime case1 = new DateTime(2000, 1, 1, 10, 10, 10);
        DateTime case2 = new DateTime(2000, 1, 1, 10, 10, 20);
        DateTime case3 = new DateTime(2000, 1, 1, 10, 10, 15);
        DateTime exp = new DateTime(2000, 1, 1, 10, 10, 15);

        DateTime result = new NextRefreshCalculator().Calculate(now, Arrays.asList(case1, case2, case3));

        Assert.assertEquals(exp, result);
    }
*/

}
