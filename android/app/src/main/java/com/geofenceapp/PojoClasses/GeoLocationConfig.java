package com.geofenceapp;
public class GeoLocationConfig {
    private double latitude;
    private double longitude;
    private int radiusInMeters;
    private int accuracyThreshold;
    private int timeoutInSeconds;
 
    public GeoLocationConfig(double latitude, double longitude, int radiusInMeters,
                             int accuracyThreshold, int timeoutInSeconds) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radiusInMeters = radiusInMeters;
        this.accuracyThreshold = accuracyThreshold;
        this.timeoutInSeconds = timeoutInSeconds;
    }
 
    public double getLatitude() {
        return latitude;
    }
 
    public double getLongitude() {
        return longitude;
    }
 
    public int getRadiusInMeters() {
        return radiusInMeters;
    }
 
    public int getAccuracyThreshold() {
        return accuracyThreshold;
    }
 
    public int getTimeoutInSeconds() {
        return timeoutInSeconds;
    }
}