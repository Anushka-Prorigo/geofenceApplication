package com.geofenceapp;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class GeofenceModule extends ReactContextBaseJavaModule {
 
    private static final String TAG = "GeofenceModule";
    private GeofenceHandler geofenceHandler;
    private Context context;
 
    public GeofenceModule(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
        geofenceHandler = new GeofenceHandler(context);
    }
 
    @NonNull
    @Override
    public String getName() {
        return "GeofenceModule";
    }
 
    @ReactMethod
    public void addGeofence(double latitude, double longitude, float radius, Callback callback) {
        Log.e(TAG, "addGeofence called with latitude: " + latitude + ", longitude: " + longitude + ", radius: " + radius);
 
        try {
            geofenceHandler.addGeofence(context, latitude, longitude, radius);
            callback.invoke(null, "Geofence added successfully!");
        } catch (Exception e) {
            Log.e(TAG, "Error adding geofence: " + e.getMessage());
            callback.invoke("Error: " + e.getMessage());
        }
    }
 
    public void addListener(String eventName) {
        Log.d(TAG, "addListener called for event: " + eventName);
    }
 
    public void removeListeners(Integer count) {
        Log.d(TAG, "removeListeners called. Count: " + count);
    }
}