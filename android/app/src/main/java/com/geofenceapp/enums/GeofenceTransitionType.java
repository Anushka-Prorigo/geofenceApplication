package com.geofenceapp.enums;

import com.google.android.gms.location.Geofence;

public enum GeofenceTransitionType {

    ENTER,
    EXIT,
    DWELL;

    public static GeofenceTransitionType fromInt(int transition) {
        switch (transition) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return ENTER;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return EXIT;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                return DWELL;
            default:
                throw new IllegalArgumentException("Unknown geofence transition: " + transition);
        }
    }
}
