package com.example.livelydarkness.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class IntervalSetTest {
    @Test
    public void twoEqualIntervalSets() {
        IntervalSet<Integer> first = new IntervalSet<>(
                new Interval<>(1, 3),
                new Interval<>(5, 7)
        );
        IntervalSet<Integer> second = new IntervalSet<>(
                new Interval<>(5, 7),
                new Interval<>(1, 3)
        );
        Assert.assertEquals(first, second);
    }

    @Test
    public void intersectingIntervalSets() {
        IntervalSet<Integer> first = new IntervalSet<>(
                new Interval<>(1, 5),
                new Interval<>(3, 7)
        );
        IntervalSet<Integer> second = new IntervalSet<>(
                new Interval<>(1, 7)
        );
        Assert.assertEquals(first, second);
    }

    @Test
    public void simpleIntersection() {
        IntervalSet<Integer> first = new IntervalSet<>(
                new Interval<>(2, 4)
        );
        IntervalSet<Integer> second = new IntervalSet<>(
                new Interval<>(3, 5)
        );
        IntervalSet<Integer> actual = IntervalSet.intersection(first, second);
        IntervalSet<Integer> expected = new IntervalSet<>(
                new Interval<>(3, 4)
        );
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void noIntersection() {
        IntervalSet<Integer> first = new IntervalSet<>(
                new Interval<>(10, 20)
        );
        IntervalSet<Integer> second = new IntervalSet<>(
                new Interval<>(100, 200)
        );
        IntervalSet<Integer> actual = IntervalSet.intersection(first, second);
        IntervalSet<Integer> expected = new IntervalSet<>();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void complexIntersection() {
        ArrayList<Interval<Integer>> firstIntervals = new ArrayList<>();
        ArrayList<Interval<Integer>> secondIntervals = new ArrayList<>();
        ArrayList<Interval<Integer>> expectedIntervals = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            firstIntervals.add(new Interval<>(5 * i, 5 * i + 3));
            secondIntervals.add(new Interval<>(5 * i + 1, 5 * i + 4));
            expectedIntervals.add(new Interval<>(5 * i + 1, 5 * i + 3));
        }
        IntervalSet<Integer> first = new IntervalSet<Integer>(firstIntervals);
        IntervalSet<Integer> second = new IntervalSet<>(secondIntervals);
        IntervalSet<Integer> expected = new IntervalSet<>(expectedIntervals);
        IntervalSet<Integer> actual = IntervalSet.intersection(first, second);
        Assert.assertEquals(expected, actual);
    }
}
