package com.example.geofenceApplication.Interface;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;

public interface IGeofenceBuilder {
    Geofence buildGeofence(String requestId, double latitude, double longitude, float radius, long expirationDuration, int transitionTypes, int loiteringDelay);

}
