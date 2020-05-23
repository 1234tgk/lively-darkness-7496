package com.example.livelydarkness;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.shredzone.commons.suncalc.SunTimes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

// Note: uses UTC, not local time
public class TimeEntry {
    private long seconds;
    private int milliseconds;
    private boolean io; // in or out boolean
    private double latitude;
    private double longitude;

    public TimeEntry(boolean io, long seconds, int milliseconds, double latitude, double longitude) {
        this.io = io;
        this.seconds = seconds;
        this.milliseconds = milliseconds;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean showIO() {
        return io;
    }

    public double showSeconds() {
        return seconds + milliseconds / 1000.0;
    }

    public double showLat() { return latitude; }

    public double showLong() { return longitude; }

    public int showSunRise() {
        Date date = new Date(seconds * 1000 + milliseconds);
        SunTimes times = SunTimes.compute()
                .on(date)
                .at(latitude, longitude)
                .execute();
        return times.getRise().getHours();
    }

    public int showSunSet() {
        Date date = new Date(seconds * 1000 + milliseconds);
        SunTimes times = SunTimes.compute()
                .on(date)
                .at(latitude, longitude)
                .execute();
        return times.getSet().getHours();
    }

    @RequiresApi(api = Build.VERSION_CODES.O) // because LccalDateTime requires API level 26
    private LocalDateTime getLDT() {
        return LocalDateTime.ofEpochSecond(seconds, milliseconds, ZoneOffset.UTC);
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
        return showSunRise() <= hour && hour <= showSunSet();
    }

    public static void main(String[] args) {
        TimeEntry testing = new TimeEntry(false, 1590237260 , 264, 37.421998, -122.084000);
        System.out.println(testing.showSeconds());
        System.out.println(testing.showLat());
        System.out.println(testing.showLong());
        System.out.println(testing.showSunRise());
        System.out.println(testing.showSunSet());
        System.out.println(testing.getLDT().getHour());
    }
}
