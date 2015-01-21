package com.evenucleus.evenucleus;

import com.evenucleus.client.Number2RoundedString;

import junit.framework.Assert;

/**
 * Created by tomeks on 2015-01-21.
 */
public class Number2RoundedStringT {
    public void test_Number2RoundedString() {
        Assert.assertEquals("100", Number2RoundedString.Convert(100.0));
        Assert.assertEquals("100", Number2RoundedString.Convert(100.23));
        Assert.assertEquals("1000", Number2RoundedString.Convert(1000));
        Assert.assertEquals("1k", Number2RoundedString.Convert(1000.01));
        Assert.assertEquals("7k", Number2RoundedString.Convert(6744));
        Assert.assertEquals("674k", Number2RoundedString.Convert(674400));
        Assert.assertEquals("1.0m", Number2RoundedString.Convert(1000000.01));
        Assert.assertEquals("10.2m", Number2RoundedString.Convert(10200000.01));
        Assert.assertEquals("1.00b", Number2RoundedString.Convert(1000000000.01));
        Assert.assertEquals("2.53b", Number2RoundedString.Convert(2530000000.01));

    }
}
