package com.example.geofenceApplication;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("GeofenceReceiver", "GeofenceBroadcastReceiver triggered.");

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e("GeofenceReceiver", "Geofence error: " + geofencingEvent.getErrorCode());
            return;
        }

        int transitionType = geofencingEvent.getGeofenceTransition();
        Log.d("GeofenceReceiver", "Transition type: " + transitionType);

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Log.d("GeofenceReceiver", "Entered geofence.");
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                Log.d("GeofenceReceiver", "Dwelling in geofence.");
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Log.d("GeofenceReceiver", "Exited geofence.");
                break;
            default:
                Log.d("GeofenceReceiver", "Unknown transition.");
        }
    }

}

