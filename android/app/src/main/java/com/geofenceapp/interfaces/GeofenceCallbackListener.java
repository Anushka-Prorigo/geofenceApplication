package com.geofenceapp.interfaces;

import com.geofenceapp.enums.GeofenceTransitionType;
import com.geofenceapp.pojoclasses.GeofenceError;
import com.geofenceapp.pojoclasses.GeofenceResult;

public interface GeofenceCallbackListener {
    void onGeofenceTransition(GeofenceResult result);
    void onGeofenceError(GeofenceError error);
}