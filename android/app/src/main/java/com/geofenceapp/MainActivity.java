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
import com.geofenceapp.GeofenceHandler;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends ReactActivity {
    private double latitude = 18.565999; // Example latitude
    private double longitude = 73.775532; // Example longitude
    private float radius = 100; // Example radius (meters)
    private GeofenceHandler geofenceHandler;
    private GeofencingClient geofencingClient;
    private Context context;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

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
        context = this;

        geofenceHandler = new GeofenceHandler(this);
        geofencingClient = LocationServices.getGeofencingClient(this);

        checkAndRequestPermissions();
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
            initializeGeofence();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                initializeGeofence();
            } else {
                Toast.makeText(this, "Location permissions are required for geofencing functionality.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initializeGeofence() {
        Log.d("GeofenceApp", "Geofence initialized successfully.");
        geofenceHandler.addGeofence(context, latitude, longitude, radius);
        Toast.makeText(this, "Geofence added successfully!", Toast.LENGTH_SHORT).show();
    }
}
