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
        boolean firstio;

        if (rawData.indexOf("ENTER") < rawData.indexOf("EXIT"))
            firstio = true;
        else firstio = false;

        Pattern regex = Pattern.compile("\\d+"); // find a group of numbers
        Matcher numbers = regex.matcher(rawData);

        while (numbers.find()) {
            ret.add(new TimeEntry(firstio, Long.parseLong(numbers.group())));
            firstio = !firstio;
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
    public HashMap<String, Long> organizeAndCalculate() {
        HashMap<String, ArrayList<TimeEntry>> data = groupByDate(this.toRawList());
        HashMap<String, Long> ret = new HashMap<>();

        for (Map.Entry<String, ArrayList<TimeEntry>> entry : data.entrySet()) {
            ArrayList<TimeEntry> trimed = trimByTime(entry.getValue());
            long timeOutside = 0;

            for (int i = 1 ; i < trimed.size() ; i++) {
                if (trimed.get(i).showIO()) {
                    timeOutside += trimed.get(i).showSeconds() - trimed.get(i - 1).showSeconds();
                }
            }

            ret.put(entry.getKey(),(long) timeOutside);
        }
        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] Args) {
        CalculateTime testing = new CalculateTime("ENTER\t1589947200\n" +
                "EXIT\t1589950800\n" +
                "ENTER\t1589954400\n" +
                "EXIT\t1589958000\n" +
                "ENTER\t1589961600\n" +
                "EXIT\t1589965200\n" +
                "ENTER\t1589968800\n" +
                "EXIT\t1589972400\n" +
                "ENTER\t1589976000\n" +
                "EXIT\t1589979600\n" +
                "ENTER\t1589983200\n" +
                "EXIT\t1589986800\n" +
                "ENTER\t1589990400\n" +
                "EXIT\t1589994000\n" +
                "ENTER\t1589997600\n" +
                "EXIT\t1590001200\n" +
                "ENTER\t1590004800\n" +
                "EXIT\t1590008400\n" +
                "ENTER\t1590012000\n" +
                "EXIT\t1590015600\n" +
                "ENTER\t1590019200\n" +
                "EXIT\t1590022800\n" +
                "ENTER\t1590026400\n" +
                "EXIT\t1590030000\n" +
                "ENTER\t1590033600\n" +
                "EXIT\t1590037200\n" +
                "ENTER\t1590040800\n" +
                "EXIT\t1590044400\n" +
                "ENTER\t1590048000\n" +
                "EXIT\t1590051600\n" +
                "ENTER\t1590055200\n" +
                "EXIT\t1590058800\n" +
                "ENTER\t1590062400\n" +
                "EXIT\t1590066000\n" +
                "ENTER\t1590069600\n" +
                "EXIT\t1590073200");
        HashMap<String, Long> map = testing.organizeAndCalculate();
        try {
            for (Map.Entry<String, Long> entry : map.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
