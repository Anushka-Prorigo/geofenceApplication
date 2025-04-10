package com.geofenceapp;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class GeofenceModule extends ReactContextBaseJavaModule implements GeofenceCallbackListener {

    private static final String TAG = "GeofenceModule";
    private GeofenceBroadcastReceiver geofenceBroadcastReceiver;
    private GeofenceHandler geofenceHandler;
    private Context context;
    private Callback geofenceCallback;

    public GeofenceModule(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
        geofenceHandler = new GeofenceHandler(context);
        geofenceBroadcastReceiver = new GeofenceBroadcastReceiver(reactContext);
    }

    @NonNull
    @Override
    public String getName() {
        return "GeofenceModule";
    }

    @ReactMethod
    public void addGeofence(double latitude, double longitude, float radius, Callback callback) {
        Log.e(TAG, "addGeofence called with latitude: " + latitude + ", longitude: " + longitude + ", radius: " + radius);
        this.geofenceCallback = callback;
        try {
            geofenceHandler.addGeofence(context, latitude, longitude, radius);
            geofenceBroadcastReceiver = new GeofenceBroadcastReceiver(getReactApplicationContext());
            geofenceBroadcastReceiver.setListener(this);
            callback.invoke("Geofence added successfully!");
        } catch (Exception e) {
            Log.e(TAG, "Error adding geofence: " + e.getMessage());
            callback.invoke("Error: " + e.getMessage());
        }
    }

    @Override
    public void onGeofenceTransition(String message) {
        Log.d(TAG, "onGeofenceTransition called with message: " + message);

        if (geofenceCallback != null) {
            geofenceCallback.invoke(message);
        } else {
            getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("GeofenceTransition", message);
        }
    }

    public void addListener(String eventName) {
        Log.d(TAG, "addListener called for event: " + eventName);
    }

    public void removeListeners(Integer count) {
        Log.d(TAG, "removeListeners called. Count: " + count);
    }
}
