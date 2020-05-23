package com.example.livelydarkness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final long LOCATION_UPDATE_INTERVAL = 1000; // Interval in ms between location updates.
    private static final int LOCATION_PENDING_INTENT_RC = 234;
    private static final int PERMISSION_RC = 567;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private PendingIntent locationPendingIntent;
    private File logFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkPermissions()) {
            startListeningToLocationUpdates();
        }
    }

    /**
     * Function of reading the given file.
     * @return String that is "ENTER \d*...EXIT \d*..."
     */

    private String fileToStr(File file) {
        File dir = this.getFilesDir();
        logFile = new File(dir, Constants.LOG_FILE_NAME);

        StringBuilder builder = new StringBuilder();

        try {
            FileReader fr = new FileReader(file);

            for (int c = fr.read() ; c != -1 ; c = fr.read()) {
                builder.append((char) c);
            }
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    public void buttonClicked(View view) {
        Log.i(TAG, "Handling button click.");
        // TODO: Handle button click.
    }

    /**
     * Check if the user has granted required permissions.
     * @return true if permissions are already granted. false otherwise.
     */
    private boolean checkPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        }

        // Check if there is any permissions not granted.
        boolean allGranted = permissions.stream()
                .map(permission -> (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED))
                .reduce(true, (a, b) -> a && b);
        if (!allGranted) {
            // There are missing permissions.
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), PERMISSION_RC);
            return false;
        }
        // All required permissions are already granted by the user.
        Log.i(TAG, "Permission is already granted.");
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (
                requestCode == PERMISSION_RC
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is granted.
            Log.i(TAG, "User granted the permissions.");
            startListeningToLocationUpdates();
        }
    }

    private void startListeningToLocationUpdates() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(getLocationRequest(), getLocationPendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Location update enabled.");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "Failed to enable location update.");
                    }
                });
    }

    private void stopListeningToLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationPendingIntent);
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest =LocationRequest.create();
        locationRequest.setInterval(LOCATION_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private PendingIntent getLocationPendingIntent() {
        if (locationPendingIntent != null) {
            return locationPendingIntent;
        }
        Intent intent = new Intent(this, LocationBroadcastReceiver.class);
        locationPendingIntent = PendingIntent.getBroadcast(
                this,
                LOCATION_PENDING_INTENT_RC,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        return locationPendingIntent;
    }
}
