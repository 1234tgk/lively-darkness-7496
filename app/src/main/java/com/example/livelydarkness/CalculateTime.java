package com.example.livelydarkness;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.livelydarkness.model.Interval;
import com.example.livelydarkness.model.IntervalSet;

import org.shredzone.commons.suncalc.SunTimes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class CalculateTime {
    private String rawData;
    private GregorianCalendar today;

    public CalculateTime(String fileFromGeofence) {
        rawData = fileFromGeofence;
        today = new GregorianCalendar();
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
    private SortedMap<GregorianCalendar, ArrayList<TimeEntry>> groupByDate(ArrayList<TimeEntry> raw) {
        SortedMap<GregorianCalendar, ArrayList<TimeEntry>> ret = new TreeMap<>();

        for (TimeEntry entry : raw) {
            // Convert epoch time to gregorian calendar.
            GregorianCalendar entryCalendar = new GregorianCalendar();
            entryCalendar.setTimeInMillis(entry.getEpochTime());

            // Round calendar to date.
            // Discard hour, minute, second and millisecond.
            int entryYear = entryCalendar.get(Calendar.YEAR);
            int entryMonth = entryCalendar.get(Calendar.MONTH);
            int entryDate = entryCalendar.get(Calendar.DAY_OF_MONTH);
            GregorianCalendar roundedEntryCalendar = new GregorianCalendar(entryYear, entryMonth, entryDate);

            // Add entry to map.
            if (!ret.containsKey(roundedEntryCalendar)) {
                ArrayList<TimeEntry> newList = new ArrayList<>();
                newList.add(entry);
                ret.put(roundedEntryCalendar, newList);
            } else {
                ret.get(roundedEntryCalendar).add(entry);
            }
        }

        return ret;
    }


    /**
     * Calculate sun exposure in 1 day.
     *
     * @param dayStart Starting time of the day (midnight).
     * @param entries  Time entries
     * @return exposure time in milliseconds
     */
    private long calculateExposure(GregorianCalendar dayStart, ArrayList<TimeEntry> entries) {
        if (entries.size() == 0) {
            return 0;
        }
        // Calculate day end.
        GregorianCalendar dayEnd = new GregorianCalendar();
        dayEnd.setTimeInMillis(dayStart.getTimeInMillis());
        dayEnd.add(Calendar.DATE, 1);
        SunTimes sun = SunTimes.compute()
                .on(dayStart)
                .fullCycle()
                .execute();
        long sunrise = sun.getRise().getTime();
        long sunset = sun.getSet().getTime();

        // Calculate sun intervals.
        ArrayList<Interval<Long>> sunIntervals = new ArrayList<>();
        if (sunrise < sunset) {
            // Case I. sunrise before sunset.
            sunIntervals.add(new Interval<>(sunrise, sunset));
        } else {
            // Case II. sunset before sunrise.
            sunIntervals.add(new Interval<>(dayStart.getTimeInMillis(), sunset));
            if (sunrise < dayEnd.getTimeInMillis()) {
                sunIntervals.add(new Interval<>(sunrise, dayEnd.getTimeInMillis()));
            }
        }

        // Calculate outdoor intervals.
        ArrayList<Interval<Long>> outdoorIntervals = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            if (i == 0 && entries.get(i).getIo()) {
                // First entry is ENTER event.
                outdoorIntervals.add(new Interval<>(dayStart.getTimeInMillis(), entries.get(i).getEpochTime()));
            } else if (i == entries.size() - 1 && !entries.get(i).getIo()) {
                // Last entry is EXIT event.
                outdoorIntervals.add(new Interval<>(entries.get(i).getEpochTime(), dayEnd.getTimeInMillis()));
            } else if (entries.get(i).getIo()) {
                outdoorIntervals.add(new Interval<>(entries.get(i - 1).getEpochTime(), entries.get(i).getEpochTime()));
            }
        }

        List<Interval<Long>> exposedInterval = IntervalSet.intersection(
                new IntervalSet<>(sunIntervals),
                new IntervalSet<>(outdoorIntervals)
        ).getIntervals();

        return exposedInterval.stream()
                .map((interval) -> interval.getUpperBound() - interval.getLowerBound())
                .reduce(0L, Long::sum);
    }

    /**
     * Public method that utilizes all private method
     * Only calculates exit times
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public SortedMap<GregorianCalendar, Long> organizeAndCalculate() {
        SortedMap<GregorianCalendar, ArrayList<TimeEntry>> data = groupByDate(this.toRawList());
        SortedMap<GregorianCalendar, Long> ret = new TreeMap<>();

        for (Map.Entry<GregorianCalendar, ArrayList<TimeEntry>> each : data.entrySet()) {
            ret.put(each.getKey(), calculateExposure(each.getKey(), each.getValue()));
        }

        return ret;
    }
}
