package com.example.livelydarkness.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntervalSet<T extends Comparable<T>> {
    private ArrayList<Interval<T>> intervals;

    @SafeVarargs
    public IntervalSet(Interval<T>... intervals) {
        buildIntervalSet(Arrays.stream(intervals));
    }

    public IntervalSet(Collection<Interval<T>> intervals) {
        buildIntervalSet(intervals.stream());
    }

    private void buildIntervalSet(Stream<Interval<T>> intervalStream) {
        intervals = intervalStream.filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
        sortIntervals();
        mergeIntersectingIntervals();
    }

    public List<Interval<T>> getIntervals() {
        return new ArrayList<>(intervals);
    }

    public static <E extends Comparable<E>> IntervalSet<E> intersection(IntervalSet<E> first, IntervalSet<E> second) {
        ArrayList<Interval<E>> ret = new ArrayList<>();
        for (Interval<E> x : first.intervals) {
            for (Interval<E> y : second.intervals) {
                ret.add(Interval.intersection(x, y));
            }
        }
        return new IntervalSet<>(ret);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IntervalSet)) {
            return false;
        }
        return intervals.equals(((IntervalSet) obj).intervals);
    }

    /**
     * Sort intervals property in ascending lower bound value order.
     */
    private void sortIntervals() {
        intervals.sort(new Comparator<Interval<T>>() {
            @Override
            public int compare(Interval<T> o1, Interval<T> o2) {
                return o1.getLowerBound().compareTo(o2.getLowerBound());
            }
        });
    }

    /**
     * Merge intersecting intervals inside the intervals list.
     * Assume the list is sorted.
     */
    private void mergeIntersectingIntervals() {
        ArrayList<Interval<T>> merged = new ArrayList<>();
        Interval<T> intervalBefore = null;
        for (Interval<T> interval : intervals) {
            // Check for intersection.
            Interval<T> intersection = Interval.intersection(intervalBefore, interval);
            if (intersection == null) {
                merged.add(intervalBefore);
                intervalBefore = interval;
            } else {
                intervalBefore = Interval.join(intervalBefore, interval);
            }
        }
        if (intervalBefore != null) {
            merged.add(intervalBefore);
        }
        intervals = merged;
    }
}
