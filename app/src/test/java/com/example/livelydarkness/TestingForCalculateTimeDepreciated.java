package com.example.livelydarkness;

import org.junit.Assert;
import org.junit.Test;

public class TestingForCalculateTimeDepreciated {

    @Test
    public void testCalculateTime() {
        _CalculateTimeDepreciated test = new _CalculateTimeDepreciated("ENTER 0100 EXIT 0200 ENTER 0300 EXIT 0400 ENTER 1000");
        long[] result = test.calculateTimeDiff();

        Assert.assertEquals(200, result[0]); // time inside of genfence
        Assert.assertEquals(700, result[1]); // time outside of genfence
    }
}
