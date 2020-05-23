package com.example.livelydarkness;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculateTime {
    private String rawData;

    public CalculateTime(String fileFromGeofence) {
        rawData = fileFromGeofence;
    }

    /**
     * String to list of TimeEntry.
     * How to use: this.toRawList()
     * @return ArrayList<TimeEntry>
     */
    private ArrayList<TimeEntry> toRawList() {
        ArrayList<TimeEntry> ret = new ArrayList<>();
        String[] rawInfo = rawData.split("\\n");
        boolean firstIo;
        String[] components;

        if (rawInfo == null) return null; // just for safekeeping

        for (String entry : rawInfo) {
            components = entry.split("\\s");
            ret.add(new TimeEntry(components[0].equals("ENTER"), Long.parseLong(components[1]), Double.parseDouble(components[2]), Double.parseDouble(components[3]) ));
        }

        return ret;
    }

    /**
     * Group all Time entries to a date with Hashmap
     * How to use: groupByDate(this.toRawList())
     * @param raw
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private HashMap<String, ArrayList<TimeEntry>> groupByDate(ArrayList<TimeEntry> raw) {
        HashMap<String, ArrayList<TimeEntry>> ret = new HashMap<>();
        ArrayList<TimeEntry> retBuilder;

        for (TimeEntry entry : raw) {
            if (!ret.containsKey(entry.getDate())) {
                retBuilder = new ArrayList<>();
                retBuilder.add(entry);
                ret.put(entry.getDate(), retBuilder);
            } else {
                Objects.requireNonNull(ret.get(entry.getDate())).add(entry); // i don't know what this is, but it looks awesome
            }
        }

        return ret;
    }

    /**
     * Remove all nighttime Time entries
     * Need to iterate through map
     * @param raw
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<TimeEntry> trimByTime(ArrayList<TimeEntry> raw) {
        int i = 0;
        while (i < raw.size()) {
            if (raw.get(i).isDayTime()) {
                ++i;
            } else {
                raw.remove(i);
            }
        }

        return raw;
    }

    /**
     * Public method that utilizes all private method
     * Only calculates exit times
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public HashMap<String, Double> organizeAndCalculate() {
        HashMap<String, ArrayList<TimeEntry>> data = groupByDate(this.toRawList());
        HashMap<String, Double> ret = new HashMap<>();

        for (Map.Entry<String, ArrayList<TimeEntry>> entry : data.entrySet()) {
            ArrayList<TimeEntry> trimed = trimByTime(entry.getValue());
            double timeOutside = 0;

            for (int i = 1 ; i < trimed.size() ; i++) {
                if (trimed.get(i).showIO()) {
                    timeOutside += trimed.get(i).showSeconds() - trimed.get(i - 1).showSeconds();
                }
            }

            ret.put(entry.getKey(), timeOutside);
        }
        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] Args) {
        CalculateTime testing = new CalculateTime("EXIT 1590237260264 37.421998 -122.084000\n" +
                "ENTER 1590241769587 43.768295 -79.411784");
        ArrayList<TimeEntry> arrayTest = testing.toRawList();
        for (TimeEntry entry : arrayTest) {
            System.out.println("" + entry.showIO() + " " + String.format("%f2", entry.showSeconds()) + " " + entry.showLat() + " " + entry.showLong());
        }

        HashMap<String, Double> map = testing.organizeAndCalculate();
        try {
            for (Map.Entry<String, Double> entry : map.entrySet()) {
                System.out.println(entry.getKey() + ": " + String.format("%f2", entry.getValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
