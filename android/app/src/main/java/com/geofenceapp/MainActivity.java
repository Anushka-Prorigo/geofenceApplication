package com.geofenceapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.defaults.DefaultReactActivityDelegate;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends ReactActivity {
    private double latitude = 18.565999;
    private double longitude = 73.775532;
    private float radius = 100;
    private GeofencingClient geofencingClient;
    private Context context;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private boolean isGeofencingActive = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;


    @Override
    protected String getMainComponentName() {
        return "geofenceApp";
    }

    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        return new DefaultReactActivityDelegate(
            this,
            getMainComponentName(),
            BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        geofencingClient = LocationServices.getGeofencingClient(this);
        checkAndRequestPermissions();
          Intent serviceIntent = new Intent(this, ForeGroundService.class);
          startForegroundService(serviceIntent);

    }

    private void checkAndRequestPermissions() {
        boolean fineLocationGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean backgroundLocationGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!fineLocationGranted || !backgroundLocationGranted) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            }, REQUEST_LOCATION_PERMISSION);
        } else {
            startGeofencing();
            initializeGeofence();
        }
    }

 private void initializeGeofence() {
        Log.d("GeofenceApp", "Geofence initialized successfully.");
        Toast.makeText(this, "Geofence added successfully!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            boolean fineLocationGranted = false;
            boolean backgroundLocationGranted = false;

            if (grantResults.length > 0) {
                fineLocationGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (grantResults.length > 1) {
                    backgroundLocationGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                }
            }

            if (fineLocationGranted && backgroundLocationGranted) {
                Log.d("Permissions", "Permissions granted, starting geofencing...");
                startGeofencing();
            } else {
                Log.e("Permissions", "Permissions denied. Fine Location: " + fineLocationGranted + ", Background Location: " + backgroundLocationGranted);
                Toast.makeText(this, "Both location permissions are required for geofencing.", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void startGeofencing() {
        Log.d("Geofencing", "Starting geofencing setup...");

        double latitude = 18.565999;
        double longitude = 73.775532;
        float radius = 1000;

        GeofenceHandler geofenceHandler = new GeofenceHandler(this);
        geofenceHandler.addGeofence(this, latitude, longitude, radius);
        Log.d("Geofencing", "Geofence added for location: " + latitude + ", " + longitude);
    }

    private void startForegroundService() {
        Intent serviceIntent = new Intent(this, ForeGroundService.class);
        serviceIntent.putExtra("latitude", latitude);
        serviceIntent.putExtra("longitude", longitude);
        startForegroundService(serviceIntent);
    }

  @Override
protected void onPause() {
    super.onPause();
    Log.d("LifecycleDebug", "onPause triggered.");
    Intent serviceIntent = new Intent(this, ForeGroundService.class);
    serviceIntent.putExtra("latitude", latitude);
    serviceIntent.putExtra("longitude", longitude);
    startForegroundService(serviceIntent); // Directly use `this` as the context
}


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LifecycleDebug", "Re-initializing geofences if necessary.");

        if (!isGeofencingActive) {
            startGeofencing();
            isGeofencingActive = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LifecycleDebug", "onStop triggered.");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MainActivity", "Input Channel reconstructed as Activity restarted.");
    }
}
