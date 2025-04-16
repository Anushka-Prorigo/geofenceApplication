package com.geofenceapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private long lastTransitionTime = 0;
    private static final String TAG = "GeofenceBroadcastReceiver";

    private ReactApplicationContext reactContext;
    private GeofenceCallbackListener listener;

    public GeofenceBroadcastReceiver() {
        // Default constructor
    }

    public GeofenceBroadcastReceiver(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
    }

    public void setListener(GeofenceCallbackListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long currentTime = System.currentTimeMillis();

        // Ignore duplicate events within 5 seconds
        if (currentTime - lastTransitionTime < 5000) {
            Log.d(TAG, "Ignoring duplicate transition.");
            return;
        }
        lastTransitionTime = currentTime;

        Log.d(TAG, "onReceive called.");
        if (intent == null) {
            Log.e(TAG, "Received null Intent. Exiting.");
            return;
        }

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent == null || geofencingEvent.hasError()) {
            int errorCode = geofencingEvent != null ? geofencingEvent.getErrorCode() : -1;
            Log.e(TAG, "GeofencingEvent error code: " + errorCode);
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
                Log.e(TAG, "Unknown transition type: " + transitionType);
                return;
        }

        Log.d(TAG, "Geofence Transition: " + message);
        sendEventToReactNative(context, "GeofenceTransition", message);
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
                Log.d(TAG, "Event emitted to React Native: " + eventName + " with message: " + message);
            } else {
                Log.w(TAG, "ReactContext is null. Could not send event to JS.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to emit event: " + e.getMessage(), e);
        }
    }

    private void sendNotification(Context context, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "geofence_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Geofence Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);

            Notification notification = new Notification.Builder(context, channelId)
                    .setContentTitle("Geofence Event")
                    .setContentText(message)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .build();

            notificationManager.notify(1, notification);
        } else {
            // For API levels below 26, use the older Notification.Builder
            Notification notification = new Notification.Builder(context)
                    .setContentTitle("Geofence Event")
                    .setContentText(message)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .build();

            notificationManager.notify(1, notification);
        }
    }
}
