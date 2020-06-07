package com.example.livelydarkness.model;

import androidx.annotation.Nullable;

public class Interval<T extends Comparable<T>> {
    private T lowerBound;
    private T upperBound;

    public Interval(T lowerBound, T upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public T getLowerBound() {
        return lowerBound;
    }

    public T getUpperBound() {
        return upperBound;
    }

    public static <E extends Comparable<E>> Interval<E> intersection(Interval<E> first, Interval<E> second) {
        E low = null;
        E hi = null;
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        }
        low = first.lowerBound.compareTo(second.lowerBound) > 0 ? first.lowerBound : second.lowerBound;
        hi = first.upperBound.compareTo(second.upperBound) < 0 ? first.upperBound : second.upperBound;
        if (low.compareTo(hi) > 0) {
            return null;
        }
        return new Interval<>(low, hi);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Interval)) {
            return false;
        }
        return lowerBound.equals(((Interval) obj).lowerBound) && upperBound.equals(((Interval) obj).upperBound);
    }
}
