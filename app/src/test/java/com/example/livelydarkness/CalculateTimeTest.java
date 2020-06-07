package com.example.livelydarkness;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class CalculateTimeTest {
    @Test
    public void simpleRawList() {
        String raw = "EXIT 1591334687001 37.575339 126.976742" + "\nENTER 1591335687001 37.575339 126.976742";
        CalculateTime ct = new CalculateTime(raw);
        Map<String, Double> actual = ct.organizeAndCalculate();
        TimeEntry exitTimeEntry = new TimeEntry(
                false,
                1591334687001L,
                37.575339,
                126.976742
        );
        TimeEntry enterTimeEntry = new TimeEntry(
                true,
                1591335687001L,
                37.575339,
                126.976742
        );
        String dateString = enterTimeEntry.getDate();
        Assert.assertTrue(actual.containsKey(dateString));
        Assert.assertEquals(1000.0, actual.get(dateString), 0.1);
    }
}
