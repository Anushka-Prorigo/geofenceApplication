package com.geofenceapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

public class ForeGroundService extends Service {

    private static final String TAG = "ForeGroundService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Foreground Service Created.");
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started.");
        startForeground(1, getNotification());
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Foreground Service Destroyed.");
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                "GeofenceServiceChannel",
                "Geofence Service",
                NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("This channel is used for geofencing services.");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private Notification getNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(this, "GeofenceServiceChannel")
                .setContentTitle("Geofence Service")
                .setContentText("Monitoring geofence transitions...")
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .build();
        } else {
            return new Notification.Builder(this)
                .setContentTitle("Geofence Service")
                .setContentText("Monitoring geofence transitions...")
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .build();
        }
    }
}
