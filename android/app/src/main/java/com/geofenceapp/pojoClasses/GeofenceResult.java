package com.geofenceapp.pojoclasses;

import com.geofenceapp.enums.GeofenceTransitionType;
import com.geofenceapp.enums.GeofenceType;

public class GeofenceResult {

    private GeofenceTransitionType transitionType;
    private CircularGeofenceConfig circularGeofenceConfig;
    private long timestamp;

    public GeofenceResult(GeofenceTransitionType transitionType,
                          CircularGeofenceConfig circularGeofenceConfig,
                          long timestamp) {

        this.transitionType = transitionType;
        this.circularGeofenceConfig = circularGeofenceConfig;
        this.timestamp = timestamp;
    }


    public GeofenceTransitionType getTransitionType() {
        return transitionType;
    }

    public CircularGeofenceConfig getCircularGeofenceConfig() {
        return circularGeofenceConfig;
    }

    public long getTimestamp() {
        return timestamp;
    }

}