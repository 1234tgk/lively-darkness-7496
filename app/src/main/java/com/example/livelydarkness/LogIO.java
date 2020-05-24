package com.example.livelydarkness;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class LogIO {
    private static final String TAG = "LogIO";
    protected static File getLogFile(Context context) {
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
