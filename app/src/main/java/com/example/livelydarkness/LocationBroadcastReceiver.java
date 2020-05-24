package com.example.livelydarkness;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.LocationResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.Instant;

public class LocationBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "LocationBroadcastReceiver";
    private static final CalculateDistance calculateDistance = new CalculateDistance();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!LocationResult.hasResult(intent)) {
            // Missing location result.
            Log.e(TAG, "Missing location result.");
            return;
        }
        LocationResult result = LocationResult.extractResult(intent);
        for (Location location : result.getLocations()) {
            logCoordinates(
                    location.getLatitude(),
                    location.getLongitude()
            );
            String lastEventType = getLastEventType(context);
            String currentEventType = isIndoors(location.getLatitude(), location.getLongitude()) ? Constants.ENTER_EVENT : Constants.EXIT_EVENT;
            if (lastEventType == null || !lastEventType.equals(currentEventType)) {
                // Current event type is different from last event type.
                // Event should be logged.
                writeToLogFile(
                        context,
                        currentEventType,
                        Instant.now().toEpochMilli(),
                        location.getLatitude(),
                        location.getLongitude()
                );
            }
        }

        sendLocalBroadcast(context);
    }

    private void logCoordinates(double latitude, double longitude) {
        Log.i(TAG, String.format(
                "Location = lat:%f/long:%f",
                latitude,
                longitude
        ));
    }

    private boolean isIndoors(double latitude, double longitude) {
        // TODO: User-defined indoor boundaries.
        double originLatitude = 43.768301;
        double originLongitude = -79.411754;
        double indoorRadius = 20;

        // Indoors if within radius.
        return !(calculateDistance.distance(originLatitude, originLongitude, latitude, longitude) > indoorRadius);
    }

    private String getLastEventType(Context context) {
        File logFile = new File(context.getFilesDir(), Constants.LOG_FILE_NAME);
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

    /**
     * Record transition event to the log file.
     * @param context Android context of this event
     * @param transitionType ENTER or EXIT
     * @param timestamp timestamp as epoch time
     * @param latitude latitude of the current location
     * @param longitude longitude of the current location
     */
    private void writeToLogFile(Context context, String transitionType, long timestamp, double latitude, double longitude) {
        File logFile = new File(context.getFilesDir(), Constants.LOG_FILE_NAME);

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
     * Send local broadcast about location update.
     */
    private void sendLocalBroadcast(Context context) {
        Log.i(TAG, "Sending local broadcast.");
        Intent intent = new Intent(Constants.LOCATION_UPDATE_ACTION);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
