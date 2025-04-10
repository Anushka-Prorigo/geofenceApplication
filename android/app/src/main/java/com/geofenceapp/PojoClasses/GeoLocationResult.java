package com.geofenceapp;
public class GeoLocationResult {
    private double latitude;
    private double longitude;
    private float accuracy;
    private boolean withinGeoFence;
 
    public GeoLocationResult(double latitude, double longitude, float accuracy, boolean withinGeoFence) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.withinGeoFence = withinGeoFence;
    }
 
    public double getLatitude() {
        return latitude;
    }
 
    public double getLongitude() {
        return longitude;
    }
 
    public float getAccuracy() {
        return accuracy;
    }
 
    public boolean isWithinGeoFence() {
        return withinGeoFence;
    }
}