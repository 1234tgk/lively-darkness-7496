package com.example.livelydarkness;

import org.junit.Assert;
import org.junit.Test;

public class TestingForCalculateTime {

    @Test
    public void testCalculateTime() {
        CalculateTime test = new CalculateTime(Constants.ENTER_EVENT + " 0100 " + Constants.EXIT_EVENT + " 0200 " + Constants.ENTER_EVENT + " 0300 " + Constants.EXIT_EVENT + " 0400 " + Constants.ENTER_EVENT + " 1000");
        long[] result = test.calculateTimeDiff();

        Assert.assertEquals(200, result[0]); // time inside of genfence
        Assert.assertEquals(700, result[1]); // time outside of genfence
    }
}
