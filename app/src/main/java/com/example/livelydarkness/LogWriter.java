package com.example.livelydarkness;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class LogWriter extends LogIO {
    private static final String TAG = "LogWriter";
    /**
     * Record transition event to the log file.
     * @param context Android context of this event
     * @param transitionType ENTER or EXIT
     * @param timestamp timestamp as epoch time
     * @param latitude latitude of the current location
     * @param longitude longitude of the current location
     */
    public static void append(Context context, String transitionType, long timestamp, double latitude, double longitude) {
        File logFile = getLogFile(context);

        try {
            FileWriter fw = new FileWriter(logFile, true);
            PrintWriter writer = new PrintWriter(fw);
            writer.println(String.format("%s %d %f %f", transitionType, timestamp, latitude, longitude));
            writer.close();
            fw.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Override log file with dummy data.
     * @param context Android context
     */
    public static void writeDummy(Context context) {
        StringBuilder sb = new StringBuilder();

        File logFile = getLogFile(context);
        try {
            FileWriter fw = new FileWriter(logFile);
            PrintWriter writer = new PrintWriter(fw);
            writer.println("ENTER 1589989951000 43.768301 -79.411754");
            writer.println("EXIT 1589992951000 43.768301 -79.411754");
            writer.println("ENTER 1589995951000 43.768301 -79.411754");
            writer.println("EXIT 1589998951000 43.768301 -79.411754");
            writer.println("ENTER 1590001951000 43.768301 -79.411754");
            writer.println("EXIT 1590004951000 43.768301 -79.411754");
            writer.println("ENTER 1590007951000 43.768301 -79.411754");
            writer.println("EXIT 1590010951000 43.768301 -79.411754");
            writer.println("ENTER 1590020951000 43.768301 -79.411754");
            writer.println("EXIT 1590030951000 43.768301 -79.411754");
            writer.println("ENTER 1590040951000 43.768301 -79.411754");
            writer.println("EXIT 1590050951000 43.768301 -79.411754");
            writer.println("ENTER 1590060951000 43.768301 -79.411754");
            writer.println("EXIT 1590070951000 43.768301 -79.411754");
            writer.println("ENTER 1590080951000 43.768301 -79.411754");
            writer.println("EXIT 1590090951000 43.768301 -79.411754");
            writer.println("ENTER 1590100951000 43.768301 -79.411754");
            writer.println("EXIT 1590110951000 43.768301 -79.411754");
            writer.println("ENTER 1590120951000 43.768301 -79.411754");
            writer.println("EXIT 1590130951000 43.768301 -79.411754");
            writer.println("ENTER 1590140951000 43.768301 -79.411754");
            writer.println("EXIT 1590150951000 43.768301 -79.411754");
            writer.println("ENTER 1590160951000 43.768301 -79.411754");

            writer.close();
            fw.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
