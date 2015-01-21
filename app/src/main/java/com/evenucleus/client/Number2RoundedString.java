package com.evenucleus.client;

import java.math.BigDecimal;

/**
 * Created by tomeks on 2015-01-21.
 */
public class Number2RoundedString {
    public static String Convert(double number) {
        double a = Math.abs(number);
        String suffix = "";
        double divider = 1;
        int roundedto = 0;

        if (a > 1000000000.0)
        {
            divider = 1000000000.0;
            suffix = "b";
            roundedto = 2;
        }
        else if (a > 1000000.0)
        {
            divider = 1000000.0;
            suffix = "m";
            roundedto = 1;
        }
        else if (a > 1000.0)
        {
            divider = 1000.0;
            suffix = "k";
            roundedto = 0;
        }

        return BigDecimal.valueOf(number/divider).setScale(roundedto, BigDecimal.ROUND_HALF_UP).toString() + suffix;
    }
}
