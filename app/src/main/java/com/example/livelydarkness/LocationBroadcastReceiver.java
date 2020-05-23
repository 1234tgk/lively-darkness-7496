package com.example.livelydarkness;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

public class LocationBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "LocationBroadcastReceiver";

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
        }
    }

    private void logCoordinates(double latitude, double longitude) {
        Log.i(TAG, String.format(
                "Location = lat:%f/long:%f",
                latitude,
                longitude
        ));
    }
}
