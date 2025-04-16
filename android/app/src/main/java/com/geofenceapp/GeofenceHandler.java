package com.geofenceapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.facebook.react.bridge.ReactApplicationContext;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

public class GeofenceHandler {

    private static final String TAG = "GeofenceHandler";
    private final GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;
    private final ReactApplicationContext reactContext;

    public GeofenceHandler(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
        this.geofencingClient = LocationServices.getGeofencingClient(reactContext);
    }

    public void addGeofence(double latitude, double longitude, float radius) {
        if (ActivityCompat.checkSelfPermission(reactContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Missing ACCESS_FINE_LOCATION permission.");
            return;
        }

        if (ActivityCompat.checkSelfPermission(reactContext, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Missing ACCESS_BACKGROUND_LOCATION permission.");
            return;
        }

        Geofence geofence = new Geofence.Builder()
                .setRequestId("MyGeofence")
                .setCircularRegion(latitude, longitude, radius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(5000)
                .build();

        GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
               .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_DWELL)
                .addGeofence(geofence)
                .build();

        geofencingClient.addGeofences(geofencingRequest, getGeofencePendingIntent())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Geofence added successfully."))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to add geofence: " + e.getMessage()));
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(reactContext, GeofenceBroadcastReceiver.class);
        intent.setAction("com.geofenceapp.ACTION_GEOFENCE_EVENT");
        return PendingIntent.getBroadcast(
                reactContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE 
        );
    }
}
