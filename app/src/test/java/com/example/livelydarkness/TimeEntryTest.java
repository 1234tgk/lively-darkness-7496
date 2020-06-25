package com.example.livelydarkness;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TimeEntryTest {
    private TimeEntry seoulTimeEntry;

    @Before
    public void init() {
        // Seoul, Korea
        // Jun 5
        // 0918
        seoulTimeEntry = new TimeEntry(
                false,
                1591316285287L,
                37.575339,
                126.976742
        );
    }

    @Test
    public void seoulSunRise() {
        double actual = seoulTimeEntry.showSunRise() / 1000.0;
        double expected = 1591301460000L / 1000.0;
        Assert.assertEquals(expected, actual, 600);
    }

    @Test
    public void seoulSunSet() {
        double actual = seoulTimeEntry.showSunSet() / 1000.0;
        double expected = 1591354200000L / 1000.0;
        Assert.assertEquals(expected, actual, 600);
    }
}
