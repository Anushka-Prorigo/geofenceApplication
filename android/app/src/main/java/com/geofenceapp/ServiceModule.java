package com.geofenceapp;

import android.content.Intent;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class ServiceModule extends ReactContextBaseJavaModule {
    private static final String TAG = "ServiceModule";

    public ServiceModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "ServiceModule";
    }

    @ReactMethod
    public void startForegroundService(double latitude, double longitude) {
        ReactApplicationContext context = getReactApplicationContext();
        Intent serviceIntent = new Intent(context, ForeGroundService.class);
        serviceIntent.putExtra("latitude", latitude);
        serviceIntent.putExtra("longitude", longitude);
        context.startForegroundService(serviceIntent);
    }

    @ReactMethod
    public void stopService() {
        ReactApplicationContext context = getReactApplicationContext();
        Intent serviceIntent = new Intent(context, ForeGroundService.class);
        context.stopService(serviceIntent);
    }
}
