package com.example.geofenceApplication.Interface;

import androidx.annotation.NonNull;

public interface IGeofenceCallbackListener {
    void onReceiveGeofenceResult(@NonNull String TransitionType);

    void onReceiveError(@NonNull String error);
}
