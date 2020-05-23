package com.example.livelydarkness;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeEntry {
    final int SUNRISE_HOUR = 7;
    final int SUNSET_HOUR = 18;

    private long seconds;
    private boolean io; // in or out boolean

    public TimeEntry(boolean io, long seconds) {
        this.io = io;
        this.seconds = seconds;
    }

    public boolean showIO() {
        return io;
    }

    public long showSeconds() {
        return seconds;
    }

    @RequiresApi(api = Build.VERSION_CODES.O) // because LccalDateTime requires API level 26
    private LocalDateTime getLDT() {
        return LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC);
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
        return SUNRISE_HOUR <= hour && hour <= SUNSET_HOUR;
    }

}
