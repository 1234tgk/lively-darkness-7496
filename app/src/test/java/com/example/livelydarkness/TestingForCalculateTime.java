package com.example.livelydarkness;

import org.junit.Assert;
import org.junit.Test;

public class TestingForCalculateTime {

    @Test
    public void testCalculateTime() {
        CalculateTime test = new CalculateTime("ENTER 0100 EXIT 0200 ENTER 0300 EXIT 0400 ENTER 1000");
        long[] result = test.calculateTimeDiff();

        Assert.assertEquals(200, result[0]); // time inside of genfence
        Assert.assertEquals(700, result[1]); // time outside of genfence
    }
}
