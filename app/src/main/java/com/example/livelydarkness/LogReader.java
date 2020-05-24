package com.example.livelydarkness;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LogReader extends LogIO {
    private static final String TAG = "LogReader";
    public static String getLogString(Context context) {
        File logFile = getLogFile(context);
        StringBuilder builder = new StringBuilder();

        try {
            FileReader fr = new FileReader(logFile);

            for (int c = fr.read(); c != -1; c = fr.read()) {
                builder.append((char) c);
            }
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    public static String getLastEventType(Context context) {
        File logFile = getLogFile(context);
        String ret = null;

        try {
            // Get the last line of the log file.
            String lastLine = null;
            String currentLine = null;
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            FileReader fr = new FileReader(logFile);
            BufferedReader br = new BufferedReader(fr);
            while ((currentLine = br.readLine()) != null) {
                lastLine = currentLine;
            }

            if (lastLine != null) {
                // The log file is not empty.
                // Get the first word of the last line.
                String[] words = lastLine.split(" ");
                if (words.length < 1) {
                    // Last line is empty.
                    throw new Exception("Invalid format");
                }
                if (words[0].equals(Constants.ENTER_EVENT) || words[0].equals(Constants.EXIT_EVENT)) {
                    // Last event type found.
                    ret = words[0];
                } else {
                    // The first word is neither ENTER nor EXIT.
                    throw new Exception("Invalid event type.");
                }
            }

            br.close();
            fr.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return ret;
    }
}
