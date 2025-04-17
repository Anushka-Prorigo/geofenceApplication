package com.example.geofenceApplication.Class;

import com.example.geofenceApplication.Interface.IGeofenceBuilder;
import com.google.android.gms.location.Geofence;

public class NativeGeofenceBuilder implements IGeofenceBuilder {

    @Override
    public Geofence buildGeofence(String requestId, double latitude, double longitude, float radius, long expirationDuration, int transitionTypes, int loiteringDelay) {
        return new Geofence.Builder()
                .setRequestId(requestId)
                .setCircularRegion(latitude, longitude, radius)
                .setExpirationDuration(expirationDuration)
                .setTransitionTypes(transitionTypes)
                .setLoiteringDelay(loiteringDelay)
                .build();
    }
}
