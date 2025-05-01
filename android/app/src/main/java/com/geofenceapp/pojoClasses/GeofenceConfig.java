package com.geofenceapp.pojoclasses;


import com.geofenceapp.enums.GeofenceType;

/**
 * Base class for all types of Geofence Configurations.
 */
public abstract class GeofenceConfig {

    private String geofenceId;
    private int accuracyThreshold;
    private int dwellStateCoolOffPeriod;


    public GeofenceConfig(String geofenceId,int accuracyThreshold, int dwellStateCoolOffPeriod) {
        this.geofenceId = geofenceId;
        this.accuracyThreshold = accuracyThreshold;
        this.dwellStateCoolOffPeriod = dwellStateCoolOffPeriod;

    }

    public String getGeofenceId() {
        return geofenceId;
    }
    public int getAccuracyThreshold() {
        return accuracyThreshold;
    }

    public int getDwellStateCoolOffPeriod() {
        return dwellStateCoolOffPeriod;
    }
    /**
     * Every subclass must define its geofence type.
     */
    public abstract GeofenceType getGeofenceType();

}
