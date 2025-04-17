package com.example.geofenceApplication;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private GeofenceHandler geofenceHandler;
    double latitude = 18.565999;
    double longitude = 73.775532;
    float radius = 1000;
    private boolean isGeofencingActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startForegroundService();
        geofenceHandler = new GeofenceHandler(this);
        checkAndRequestPermissions();
    }
    private void checkAndRequestPermissions() {
        boolean fineLocationGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean backgroundLocationGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!fineLocationGranted && !backgroundLocationGranted) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            }, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            Log.d("Permissions", "All permissions granted, starting geofencing...");
            startGeofencing();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            boolean fineLocationGranted = false;
            boolean backgroundLocationGranted = false;

            if (grantResults.length > 0) {
                fineLocationGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (grantResults.length > 1) {
                    backgroundLocationGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                }
            }

            if (fineLocationGranted || backgroundLocationGranted) {
                Log.d("Permissions", "Permissions granted, starting geofencing...");
                startGeofencing();
            } else {
                Log.e("Permissions", "Permissions denied. Fine Location: " + fineLocationGranted + ", Background Location: " + backgroundLocationGranted);
                Toast.makeText(this, "Both location permissions are required for geofencing.", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void startGeofencing() {
        Log.d("Geofencing", "Starting geofencing setup...");
        double latitude = 18.565999;
        double longitude = 73.775532;
        float radius = 1000;

        GeofenceHandler geofenceHandler = new GeofenceHandler(this);
        geofenceHandler.addGeofence(this,"requestId", latitude, longitude, radius);
        Log.d("Geofencing", "Geofence added for location: " + latitude + ", " + longitude);
    }

    private void startForegroundService() {
        Intent serviceIntent = new Intent(this, ForeGroundService.class);
        serviceIntent.putExtra("latitude", latitude);
        serviceIntent.putExtra("longitude", longitude);
        startService(serviceIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LifecycleDebug", "onPause triggered.");
        Intent serviceIntent = new Intent(this, ForeGroundService.class);
        startService(serviceIntent);
        startForegroundService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LifecycleDebug", "Re-initializing geofences if necessary.");

        if (!isGeofencingActive) {
            startGeofencing();
            isGeofencingActive = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LifecycleDebug", "onStop triggered.");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MainActivity", "Input Channel reconstructed as Activity restarted.");
    }
}
