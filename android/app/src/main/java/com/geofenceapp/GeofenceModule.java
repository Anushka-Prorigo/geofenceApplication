package com.geofenceapp;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.geofenceapp.interfaces.GeofenceCallbackListener;
import com.geofenceapp.pojoclasses.GeofenceConfig;
import com.geofenceapp.pojoclasses.GeofenceError;
import com.geofenceapp.pojoclasses.GeofenceResult;


public class GeofenceModule extends ReactContextBaseJavaModule implements GeofenceCallbackListener {

    private static final String TAG = "GeofenceModule";
    private final GeofenceHandler geofenceHandler;
    private GeofenceBroadcastReceiver geofenceBroadcastReceiver;
    private Context context;
    private Callback geofenceCallback;

    public GeofenceModule(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
        geofenceHandler = new GeofenceHandler(reactContext);
        geofenceBroadcastReceiver = new GeofenceBroadcastReceiver(reactContext);
    }

    @NonNull
    @Override
    public String getName() {
        return "GeofenceModule";
    }


    @ReactMethod
    public void addGeofence(GeofenceConfig configData) {
        // Log.d(TAG, "addGeofence called with latitude: " + configData.getLatitude() + ", longitude: " + configData.getLongitude() + ", radius: " + configData.getRadius());
        // geofenceHandler.addGeofence(configData.getLatitude(), configData.getLongitude(), configData.getRadius());
    }

    @ReactMethod
    public void addGeofence(GeofenceConfig configData, Callback callback) {
        // Log.d(TAG, "addGeofence called with latitude: " + configData.getLatitude() + ", longitude: " + configData.getLongitude() + ", radius: " + configData.getRadius());
        // try {
        //     geofenceHandler.addGeofence(configData.getLatitude(), configData.getLongitude(), configData.getRadius());
        //     callback.invoke("Geofence added successfully!");
        // } catch (Exception e) {
        //     Log.e(TAG, "Error adding geofence: " + e.getMessage());
        //     callback.invoke("Error adding geofence: " + e.getMessage());
        // }
    }

    @Override
    public void onGeofenceTransition(GeofenceResult result) {
        Log.d(TAG, "onGeofenceTransition called with message: " + result.toString());

        if (geofenceCallback != null) {
            geofenceCallback.invoke(result.toString());
        } else {
            WritableMap params = Arguments.createMap();
            params.putString("message", "Geofence transition occurred");
            ReactApplicationContext reactContext = getReactApplicationContext();
            reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("GeofenceTransition", params);
        }
    }

    @Override
    public void onGeofenceError(GeofenceError error) {
        // TODO
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
                Log.w(TAG, "ReactContext is null. Could not send event to JS.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to emit event: " + e.getMessage(), e);
        }
    }

    public void addListener(String eventName) {
        Log.d(TAG, "addListener called for event: " + eventName);
    }

    public void removeListeners(Integer count) {
        Log.d(TAG, "removeListeners called. Count: " + count);
    }
}
