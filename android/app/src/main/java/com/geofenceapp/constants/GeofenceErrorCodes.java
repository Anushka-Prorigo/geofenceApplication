package com.geofenceapp.constants;

public final class GeofenceErrorCodes {

    public static final int ERROR_LOCATION_PERMISSION_DENIED = 1001;
    public static final int ERROR_GEOFENCE_NOT_AVAILABLE = 1002;
    public static final int ERROR_GEOFENCE_LIMIT_EXCEEDED = 1003;
    public static final int ERROR_LOCATION_UNAVAILABLE = 1004;
    public static final int ERROR_UNKNOWN = 1099;

    private GeofenceErrorCodes() {
        // prevent instantiation
    }
}
