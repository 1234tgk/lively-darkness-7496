package com.example.livelydarkness;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogWriter {
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

    private static File getLogFile(Context context) {
        File logFile = new File(context.getFilesDir(), Constants.LOG_FILE_NAME);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Failed to create logfile.");
            }
        }
        return logFile;
    }
}
