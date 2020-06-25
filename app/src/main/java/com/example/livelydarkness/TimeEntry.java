package com.example.livelydarkness;

// Note: uses UTC, not local time
public class TimeEntry {
    private final static long ONE_DAY_IN_MILLISECONDS = 24L * 3600L * 1000L;
    private long epochTime; // in milliseconds
    private boolean io; // in or out boolean
    private double latitude;
    private double longitude;

    public TimeEntry(boolean io, long epochTime, double latitude, double longitude) {
        this.io = io;
        this.epochTime = epochTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getEpochTime() {
        return epochTime;
    }

    public boolean getIo() {
        return io;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
