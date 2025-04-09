package com.geofenceapp;
 
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
 
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
 
public class GeofenceBroadcastReceiver extends BroadcastReceiver {
 
    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        Log.e("GeofenceBroadcastReceiver", "onReceive called");
 
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
 
        // Send to React Native
        sendEventToReactNative(context, "GeofenceTransition", message);
 
        // Send notification
        sendNotification(context, message);
    }
 
    private void sendEventToReactNative(Context context, String eventName, String message) {
        try {
            ReactApplication reactApplication = (ReactApplication) context.getApplicationContext();
            ReactInstanceManager reactInstanceManager = reactApplication.getReactNativeHost().getReactInstanceManager();
            ReactContext reactContext = reactInstanceManager.getCurrentReactContext();
 
            if (reactContext != null) {
                reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, message);
            } else {
                Log.w("GeofenceReceiver", "ReactContext is null. Could not send event to JS.");
            }
        } catch (Exception e) {
            Log.e("GeofenceReceiver", "Failed to emit event: " + e.getMessage(), e);
        }
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
 
        Notification notification = new Notification.Builder(context, channelId)
                .setContentTitle("Geofence Transition")
                .setContentText(message)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build();
 
        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }
    }
}