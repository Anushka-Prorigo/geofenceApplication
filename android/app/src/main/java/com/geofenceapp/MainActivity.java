package com.geofenceapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.ReactApplicationContext;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends ReactActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private GeofencingClient geofencingClient;
    private static final int MAX_RETRY_COUNT = 20; // Increase retry limit
    private static final int RETRY_DELAY_MS = 5000; // Increase delay to 5 seconds
    private int retryCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        geofencingClient = LocationServices.getGeofencingClient(this);

        if (getReactNativeHost() != null && getReactNativeHost().hasInstance()) {
            Log.d(TAG, "React Native Host instance is ready.");
            startGeofencing();
        } else {
            Log.d(TAG, "React Native Host instance is not ready in onCreate. Checking readiness...");
            checkReactContextReadiness();
        }

        if (!isGooglePlayServicesAvailable()) {
            return;
        }

        checkAndRequestPermissions();
        startForegroundService();
    }

    @Override
    protected String getMainComponentName() {
        return "geofenceApp";
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            Log.e("MainActivity", "Google Play Services unavailable: " + status);
            Toast.makeText(this, "Google Play Services not available. Please update or enable it.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void checkReactContextReadiness() {
        if (retryCount >= MAX_RETRY_COUNT) {
            Log.e(TAG, "React Native context not ready after maximum retries. Aborting...");
            Toast.makeText(this, "Failed to initialize geofencing. Please restart the app.", Toast.LENGTH_LONG).show();
            return;
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Log.d(TAG, "Retrying ReactContext readiness check at: " + System.currentTimeMillis());
            if (getReactNativeHost() != null && getReactNativeHost().hasInstance()) {
                ReactApplicationContext reactContext = (ReactApplicationContext) getReactNativeHost()
                        .getReactInstanceManager()
                        .getCurrentReactContext();
                if (reactContext != null) {
                    Log.d(TAG, "React Native context is now ready. Proceeding with geofencing.");
                    startGeofencing();
                } else {
                    retryCount++;
                    Log.e(TAG, "React Native context still not ready. Retrying... (Attempt " + retryCount + ")");
                    checkReactContextReadiness(); // Retry if not ready
                }
            } else {
                retryCount++;
                Log.e(TAG, "React Native Host or Instance Manager is null. Retrying... (Attempt " + retryCount + ")");
                checkReactContextReadiness(); // Retry if not ready
            }
        }, RETRY_DELAY_MS);
    }

    private void checkAndRequestPermissions() {
        boolean fineLocationGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean backgroundLocationGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!fineLocationGranted) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else if (!backgroundLocationGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 101);
        } else {
            Log.d("Permissions", "All required permissions granted. Proceeding...");
            startGeofencing();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions", "Fine Location granted.");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 101);
                } else {
                    startGeofencing();
                }
            } else {
                Log.e("Permissions", "Fine Location denied.");
                Toast.makeText(this, "Fine Location is required for geofencing.", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions", "Background Location granted.");
                startGeofencing();
            } else {
                Log.e("Permissions", "Background Location denied.");
                Toast.makeText(this, "Background Location is required for geofencing.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startGeofencing() {
        Log.d(TAG, "Starting geofencing...");
        if (getReactNativeHost() != null && getReactNativeHost().getReactInstanceManager() != null) {
            ReactApplicationContext reactContext = (ReactApplicationContext) getReactNativeHost()
                    .getReactInstanceManager()
                    .getCurrentReactContext();
            if (reactContext != null) {
                Log.d(TAG, "ReactApplicationContext is ready. Adding geofence...");
                new GeofenceHandler(reactContext).addGeofence(18.565999, 73.775532, 100.0f);
            } else {
                Log.e(TAG, "ReactApplicationContext is null. Retrying...");
                // Retry after a short delay
                new Handler(Looper.getMainLooper()).postDelayed(this::startGeofencing, 1000); // Retry after 1 second
            }
        } else {
            Log.e(TAG, "React Native Host or React Instance Manager is null. Cannot start geofencing.");
        }
    }

    private void startForegroundService() {
        Intent serviceIntent = new Intent(this, ForeGroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (getReactNativeHost() != null && getReactNativeHost().hasInstance()) {
            Log.d(TAG, "React Native context is ready. Handling window focus change.");
            // Proceed with operations that depend on the React Native context
        } else {
            Log.w(TAG, "React Native context is not ready. Skipping window focus change handling.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getReactNativeHost() != null && getReactNativeHost().hasInstance()) {
            Log.d(TAG, "React Native context is ready in onResume.");
            startGeofencing();
        } else {
            Log.w(TAG, "React Native context is not ready in onResume. Retrying...");
            checkReactContextReadiness();
        }
    }
}
