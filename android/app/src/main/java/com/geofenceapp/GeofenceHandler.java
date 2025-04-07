package com.geofenceapplication;
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
    Log.d("GeofenceHandler", "addGeofence called with latitude: " + latitude + ", longitude: " + longitude + ", radius: " + radius);

    Geofence geofence = new Geofence.Builder()
            .setRequestId("MyGeofence")
            .setCircularRegion(latitude, longitude, radius)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                    Geofence.GEOFENCE_TRANSITION_EXIT |
                    Geofence.GEOFENCE_TRANSITION_DWELL)
            .setLoiteringDelay(10000)
            .build();

    geofencePendingIntent = getGeofencePendingIntent(context);
    Log.d("GeofenceHandler", "PendingIntent created: " + geofencePendingIntent);

    GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build();

    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        Log.e("GeofenceHandler", "Location permission not granted");
        return;
    }

    geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)
            .addOnSuccessListener(aVoid -> Log.d("GeofenceHandler", "Geofence added successfully"))
            .addOnFailureListener(e -> Log.e("GeofenceHandler", "Failed to add geofence: " + e.getMessage()));
}


    private PendingIntent getGeofencePendingIntent(Context context) {
        Log.d("GeofenceHandler", "getGeofencePendingIntent called");
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
