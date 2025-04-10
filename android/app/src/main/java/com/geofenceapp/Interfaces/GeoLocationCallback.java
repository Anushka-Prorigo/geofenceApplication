package com.geofenceapp;
public interface GeoLocationCallback {
    void onSuccess(GeoLocationResult result);
    void onError(GeoLocationError error);
}