package com.example.livelydarkness;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.shredzone.commons.suncalc.SunTimes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

// Note: uses UTC, not local time
public class TimeEntry {
    private final static long ONE_DAY_IN_MILLISECONDS = 24L * 3600L * 1000L;
    private long epochTime; // in milliseconds
    private boolean io; // in or out boolean
    private double latitude;
    private double longitude;
    private long sunrise;
    private long sunset;

    public TimeEntry(boolean io, long epochTime, double latitude, double longitude) {
        this.io = io;
        this.epochTime = epochTime;
        this.latitude = latitude;
        this.longitude = longitude;
        calculateSunTimes();
    }

    private void calculateSunTimes() {
        Date date = new Date(epochTime);
        SunTimes futureSuntimes = SunTimes.compute()
                .fullCycle()
                .at(latitude, longitude)
                .on(date)
                .execute();
        long pastSunset;
        long pastSunrise;
        long futureSunrise = futureSuntimes.getRise().getTime();
        long futureSunset = futureSuntimes.getSet().getTime();
        if (futureSunrise < futureSunset) {
            sunrise = futureSunrise;
            for (int i = 1; sunset == 0; i++) {
                SunTimes pastSuntimes = SunTimes.compute()
                        .at(latitude, longitude)
                        .on(date)
                        .plusDays(-i)
                        .oneDay()
                        .execute();
                Date x = pastSuntimes.getSet();
                if (x != null) {
                    sunset = x.getTime();
                }
            }
        } else {
            sunset = futureSunset;
            for (int i = 1; sunrise == 0; i++) {
                SunTimes pastSuntimes = SunTimes.compute()
                        .at(latitude, longitude)
                        .on(date)
                        .plusDays(-i)
                        .oneDay()
                        .execute();
                Date x = pastSuntimes.getRise();
                if (x != null) {
                    sunrise = x.getTime();
                }
            }
        }
    }

    public boolean showIO() {
        return io;
    }

    public long showEpochTime() {
        return this.epochTime;
    }

    public double showLat() {
        return latitude;
    }

    public double showLong() {
        return longitude;
    }

    /**
     * Calculate sunrise time.
     *
     * @return sunrise time in milliseconds.
     */
    public long showSunRise() {
        return sunrise;
    }

    public long showSunSet() {
        return sunset;
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
