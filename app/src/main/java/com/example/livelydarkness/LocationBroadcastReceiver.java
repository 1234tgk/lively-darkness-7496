package com.example.livelydarkness;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

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
        }
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
}
