package com.example.livelydarkness;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.shredzone.commons.suncalc.SunTimes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

// Note: uses UTC, not local time
public class TimeEntry {
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

    public boolean showIO() {
        return io;
    }

    public long showEpochTime() { return this.epochTime; }

    public double showLat() { return latitude; }

    public double showLong() { return longitude; }

    /**
     * Calculate sunrise time.
     * @return sunrise time in milliseconds.
     */
    public long showSunRise() {
        Date date = new Date(epochTime);
        SunTimes times = SunTimes.compute()
                .on(date)
                .at(latitude, longitude)
                .execute();
        return times.getRise().getTime();
    }

    public long showSunSet() {
        Date date = new Date(epochTime);
        SunTimes times = SunTimes.compute()
                .on(date)
                .at(latitude, longitude)
                .execute();
        return times.getSet().getTime();
    }

    @RequiresApi(api = Build.VERSION_CODES.O) // because LccalDateTime requires API level 26
    public LocalDateTime getLDT() {
        return Instant.ofEpochMilli(epochTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDate() {
        LocalDateTime ldt = this.getLDT();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return ldt.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean isDayTime() {
        int hour = this.getLDT().getHour();
        return showSunRise() <= showEpochTime() && showEpochTime() <= showSunSet();
    }

    public static void main(String[] args) {
        TimeEntry testing = new TimeEntry(false, 1590237260264L, 37.421998, -122.084000);
        System.out.println(testing.showEpochTime());
        System.out.println(testing.showLat());
        System.out.println(testing.showLong());
        System.out.println(testing.showSunRise());
        System.out.println(testing.showSunSet());
        System.out.println(testing.getLDT().getHour());
    }
}
