package com.example.geofenceApplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.e("GeofenceReceiver", "Error in geofence event: " + geofencingEvent.getErrorCode());
            return;
        }

        int transitionType = geofencingEvent.getGeofenceTransition();
        String message = "";

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                message = "Entered the geofence area.";
                break;

            case Geofence.GEOFENCE_TRANSITION_DWELL:
                message = "Dwelling in the geofence area.";
                break;

            case Geofence.GEOFENCE_TRANSITION_EXIT:
                message = "Exited the geofence area.";
                break;

            default:
                Log.e("GeofenceReceiver", "Unknown transition type: " + transitionType);
                return;
        }

        Log.d("GeofenceReceiver", message);

        sendNotification(context, message);
    }

    private void sendNotification(Context context, String message) {
        String channelId = "GeofenceNotificationChannel";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Geofence Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for geofence transitions.");
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context, channelId)
                    .setContentTitle("Geofence Transition")
                    .setContentText(message)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .build();
        }

        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }
    }
}
