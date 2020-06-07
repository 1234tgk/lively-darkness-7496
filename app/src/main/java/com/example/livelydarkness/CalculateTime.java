package com.example.livelydarkness;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CalculateTime {
    private String rawData;

    public CalculateTime(String fileFromGeofence) {
        rawData = fileFromGeofence;
    }

    /**
     * String to list of TimeEntry.
     * How to use: this.toRawList()
     *
     * @return ArrayList<TimeEntry>
     */
    private ArrayList<TimeEntry> toRawList() {
        ArrayList<TimeEntry> ret = new ArrayList<>();
        String[] rawInfo = rawData.split("\\n");
        String[] components;

        for (String entry : rawInfo) {
            components = entry.split("\\s");
            ret.add(new TimeEntry(
                    components[0].equals("ENTER"),
                    Long.parseLong(components[1]),
                    Double.parseDouble(components[2]),
                    Double.parseDouble(components[3])
            ));
        }

        return ret;
    }

    /**
     * Group all Time entries to a date with Hashmap
     * How to use: groupByDate(this.toRawList())
     *
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
     *
     * @param raw
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<TimeEntry> trimByTime(ArrayList<TimeEntry> raw) {
        ArrayList<TimeEntry> ret = new ArrayList<>();
        boolean isFirst = true; // Flag to check if the time entry is the first of the day.
        TimeEntry lastTimeEntry = null;
        for (TimeEntry entry : raw) {
            if (!entry.isDayTime()) {
                // Ignore all night time entries.
                continue;
            }
            if (isFirst && entry.showIO()) {
                // Add dummy exit time entry before the first time entry,
                // if the first time entry is enter type.
                ret.add(new TimeEntry(
                        false,
                        entry.showSunRise(),
                        entry.showLat(),
                        entry.showLong()
                ));
            }
            isFirst = false;

            // Insert day time entries.
            ret.add(entry);
            lastTimeEntry = entry;
        }

        if (lastTimeEntry != null && !lastTimeEntry.showIO()) {
            // Add dummy enter time entry after the last time entry,
            // if the last time entry is exit type.
            ret.add(new TimeEntry(
                    true,
                    lastTimeEntry.showSunSet(),
                    lastTimeEntry.showLat(),
                    lastTimeEntry.showLong()
            ));
        }

        return ret;
    }

    /**
     * Public method that utilizes all private method
     * Only calculates exit times
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public HashMap<String, Double> organizeAndCalculate() {
        HashMap<String, ArrayList<TimeEntry>> data = groupByDate(this.toRawList());
        HashMap<String, Double> ret = new HashMap<>();

        for (Map.Entry<String, ArrayList<TimeEntry>> entry : data.entrySet()) {
            ArrayList<TimeEntry> trimmed = trimByTime(entry.getValue());
            double timeOutside = 0;

            for (int i = 1; i < trimmed.size(); i++) {
                if (trimmed.get(i).showIO()) {
                    timeOutside += (trimmed.get(i).showEpochTime() - trimmed.get(i - 1).showEpochTime()) / 1000.0;
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
            System.out.println("" + entry.showIO() + " " + String.format("%.3f", entry.showEpochTime())
                    + " " + entry.showLat() + " " + entry.showLong());
        }

        HashMap<String, Double> map = testing.organizeAndCalculate();
        try {
            for (Map.Entry<String, Double> entry : map.entrySet()) {
                System.out.println(entry.getKey() + ": " + String.format("%.3f", entry.getValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        testing = new CalculateTime("ENTER 1590237260264 37.421998 -122.084000\n" +
                "EXIT 1590241769587 43.768295 -79.411784");
        arrayTest = testing.toRawList();
        testing.trimByTime(arrayTest);
        for (TimeEntry entry : arrayTest) {
            System.out.println(entry.getLDT().getHour());
        }

    }
}
