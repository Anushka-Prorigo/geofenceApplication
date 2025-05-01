package com.geofenceapp.pojoclasses;

import com.geofenceapp.enums.GeofenceType;

/**
 * Configuration for Circular Geofence.
 */
public class CircularGeofenceConfig extends GeofenceConfig {

    private double latitude;
    private double longitude;
    private float radius;

    public CircularGeofenceConfig(double latitude, double longitude, float radius,String geofenceId,
                                  int accuracyThreshold, int dwellStateCoolOffPeriod) {
        super(geofenceId, accuracyThreshold, dwellStateCoolOffPeriod);
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public GeofenceType getGeofenceType() {
        return GeofenceType.CIRCLE;
    }
}
