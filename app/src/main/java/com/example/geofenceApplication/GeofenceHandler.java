package com.example.geofenceApplication;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.geofenceApplication.Class.NativeGeofenceBuilder;
import com.example.geofenceApplication.Interface.IGeofenceBuilder;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;


public class GeofenceHandler {

    private static final String TAG = "GeofenceHandler";
    private final GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;
    private final IGeofenceBuilder geofenceBuilder;

    public GeofenceHandler(Context context) {
        this.geofencingClient = LocationServices.getGeofencingClient(context);
        this.geofenceBuilder = new NativeGeofenceBuilder(); // Use NativeGeofenceBuilder
    }

    public void addGeofence(Context context, String requestId, double latitude, double longitude, float radius) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Missing ACCESS_FINE_LOCATION permission.");
            return;
        }

//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.e(TAG, "Missing ACCESS_BACKGROUND_LOCATION permission.");
//            return;
//        }

        // Use NativeGeofenceBuilder to create the geofence
        Geofence geofence = geofenceBuilder.buildGeofence(
                requestId,
                latitude,
                longitude,
                radius,
                Geofence.NEVER_EXPIRE,
                Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL,
                0
        );

        GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_DWELL)
                .addGeofence(geofence)
                .build();

        geofencePendingIntent = getGeofencePendingIntent(context);

        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Geofence added successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to add geofence: " + e.getMessage()));
    }

    private PendingIntent getGeofencePendingIntent(Context context) {
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(context, GeofenceBroadcastReceiver.class);
        geofencePendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
        );
        return geofencePendingIntent;
    }
}