package com.example.livelydarkness;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalculateDistanceTest {
    private CalculateDistance calculateDistance;
    @Before
    public void init() {
        calculateDistance = new CalculateDistance();
    }
    @Test
    public void oneBlockDistance() {
        double lat1 = 43.769285;
        double lon1 = -79.412848;
        double lat2 = 43.767518;
        double lon2 =  -79.412419;
        double dist = calculateDistance.distance(
                lat1,
                lon1,
                lat2,
                lon2
        );

        // Distance should be approximately 200 meters.
        Assert.assertTrue(dist < 220);
        Assert.assertTrue(dist > 180);
    }
}
