package com.example.livelydarkness.model;

import androidx.annotation.Nullable;

import java.util.Collection;

public class IntervalSet<T extends Comparable<T>> {
    @SafeVarargs
    public IntervalSet(Interval<T> ...intervals) {
        // TODO: Implement.
    }

    public IntervalSet(Collection<Interval<T>> intervals) {
        // TODO: Implement.
    }

    public static <E extends Comparable<E>> IntervalSet<E> intersection(IntervalSet<E> first, IntervalSet<E> second) {
        // TODO: Implement.
        return null;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        // TODO: Implement.
        return false;
    }
}
