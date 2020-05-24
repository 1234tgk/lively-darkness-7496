package com.example.livelydarkness;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class ContainerActivity extends AppCompatActivity {
    private static final String TAG = "ContainerActivity";

    private AppBarConfiguration mAppBarConfiguration;

    private static final long LOCATION_UPDATE_INTERVAL = 5000; // Interval in ms between location updates.
    private static final int LOCATION_PENDING_INTENT_RC = 234;
    private static final int PERMISSION_RC = 567;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private PendingIntent locationPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_main, R.id.nav_info, R.id.nav_log)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Check permissions then start updating location if possible.
        if (checkPermissions()) {
            startListeningToLocationUpdates();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.container, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
