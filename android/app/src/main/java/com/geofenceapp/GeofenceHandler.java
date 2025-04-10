package com.geofenceapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

public class GeofenceHandler {
    private static final String TAG = "GeofenceHandler";
    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;

    public GeofenceHandler(Context context) {
        geofencingClient = LocationServices.getGeofencingClient(context);
    }

    public void addGeofence(Context context, double latitude, double longitude, float radius) {
        Log.d(TAG, "addGeofence called with latitude: " + latitude +
                ", longitude: " + longitude + ", radius: " + radius);

        Geofence geofence = new Geofence.Builder()
                .setRequestId("MyGeofence")
                .setCircularRegion(latitude, longitude, radius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT |
                        Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(10000)
                .build();

        GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Missing ACCESS_FINE_LOCATION permission.");
            return;
        }

        geofencingClient.addGeofences(geofencingRequest, getGeofencePendingIntent(context))
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Geofence added successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to add geofence: " + e.getMessage()));
    }

    public PendingIntent getGeofencePendingIntent(Context context) {
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }

        Intent intent = new Intent(context.getApplicationContext(), GeofenceBroadcastReceiver.class);
        Log.d(TAG, "Creating PendingIntent for GeofenceBroadcastReceiver."+intent);
     PendingIntent geofencePendingIntent = PendingIntent.getBroadcast(
    context.getApplicationContext(),
    0,
    intent,
    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE // FLAG_MUTABLE is mandatory for Android 12+
);


        Log.d(TAG, "PendingIntent created successfully.");
        return geofencePendingIntent;
    }
}