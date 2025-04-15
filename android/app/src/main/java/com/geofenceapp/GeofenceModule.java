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

public class GeofenceModule extends ReactContextBaseJavaModule implements GeofenceCallbackListener {

    private static final String TAG = "GeofenceModule";
    private final GeofenceHandler geofenceHandler;
    private GeofenceBroadcastReceiver geofenceBroadcastReceiver;
    private Context context;
    private Callback geofenceCallback;

    public GeofenceModule(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
        geofenceHandler = new GeofenceHandler(reactContext); // Pass ReactApplicationContext
        geofenceBroadcastReceiver = new GeofenceBroadcastReceiver(reactContext);
    }

    @NonNull
    @Override
    public String getName() {
        return "GeofenceModule";
    }

    @ReactMethod
    public void addGeofence(double latitude, double longitude, float radius) {
        Log.d(TAG, "addGeofence called with latitude: " + latitude + ", longitude: " + longitude + ", radius: " + radius);
        geofenceHandler.addGeofence(latitude, longitude, radius);
    }

    @ReactMethod
    public void addGeofence(double latitude, double longitude, float radius, Callback callback) {
        Log.d(TAG, "addGeofence called with latitude: " + latitude + ", longitude: " + longitude + ", radius: " + radius);
        try {
            geofenceHandler.addGeofence(latitude, longitude, radius);
            callback.invoke("Geofence added successfully!");
        } catch (Exception e) {
            Log.e(TAG, "Error adding geofence: " + e.getMessage());
            callback.invoke("Error adding geofence: " + e.getMessage());
        }
    }

    @Override
    public void onGeofenceTransition(String message) {
        Log.d(TAG, "onGeofenceTransition called with message: " + message);

        if (geofenceCallback != null) {
            geofenceCallback.invoke(message);
        } else {
            WritableMap params = Arguments.createMap();
            params.putString("message", "Geofence transition occurred");
            ReactApplicationContext reactContext = getReactApplicationContext();
            reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("GeofenceTransition", params);
        }
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
