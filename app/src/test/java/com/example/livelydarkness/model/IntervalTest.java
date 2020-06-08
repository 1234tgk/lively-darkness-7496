package com.example.livelydarkness.model;

import org.junit.Assert;
import org.junit.Test;

public class IntervalTest {
    @Test
    public void notIntersectingIntervals() {
        Interval<Integer> first = new Interval<>(1, 3);
        Interval<Integer> second = new Interval<>(5, 7);
        Interval<Integer> actual = Interval.intersection(first, second);
        Assert.assertNull(actual);
    }

    @Test
    public void intersectingIntervals() {
        Interval<Integer> first = new Interval<>(1, 5);
        Interval<Integer> second = new Interval<>(3, 7);
        Interval<Integer> actual = Interval.intersection(first, second);
        Interval<Integer> expected = new Interval<>(3, 5);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void joinIntervals() {
        Interval<Integer> first = new Interval<>(10, 20);
        Interval<Integer> second = new Interval<>(15, 30);
        Interval<Integer> actual = Interval.join(first, second);
        Interval<Integer> expected = new Interval<>(10, 30);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void joinNonIntersectingIntervals() {
        Interval<Integer> first = new Interval<>(10, 20);
        Interval<Integer> second = new Interval<>(40, 50);
        Interval<Integer> actual = Interval.join(first, second);
        Assert.assertNull(actual);
    }
}
