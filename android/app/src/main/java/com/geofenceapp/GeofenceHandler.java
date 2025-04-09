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
    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;

    public GeofenceHandler(Context context) {
        geofencingClient = LocationServices.getGeofencingClient(context);
    }

    public void addGeofence(Context context, double latitude, double longitude, float radius) {
        Log.d("GeofenceHandler", "addGeofence called with latitude: " + latitude +
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

        geofencingClient.addGeofences(geofencingRequest, getGeofencePendingIntent(context))
                .addOnSuccessListener(aVoid -> Log.d("GeofenceHandler", "Geofence added successfully"))
                .addOnFailureListener(e -> Log.e("GeofenceHandler", "Failed to add geofence: " + e.getMessage()));
    }

    public PendingIntent getGeofencePendingIntent(Context context) {
        Context applicationContext = context.getApplicationContext();
        Log.d("GeofenceHandler", "PendingIntent created successfully. ApplicationContext: " + applicationContext);

        Intent intent = new Intent(applicationContext, GeofenceBroadcastReceiver.class);

        PendingIntent geofencePendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE 
        );

        Log.d("GeofenceHandler", "PendingIntent created successfully.");
        return geofencePendingIntent;
    }
}
