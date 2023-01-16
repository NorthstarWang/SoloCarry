package com.example.solocarry.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class LocationUtil {

    private final static int REQUEST_INTERVAL = 10000;
    private final static int FASTEST_REQUEST_INTERVAL = 5000;

    /**
     * Callback for changes in location
     */
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient fusedLocationProviderClient;

    /**
     * The current location.
     */
    private Location currentLocation;

    public LocationUtil(Context context) {
        //Setting location update
        setFusedLocationProviderClient(LocationServices.getFusedLocationProviderClient(context));
        setLocationCallback(new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                setCurrentLocation(locationResult.getLastLocation());
            }
        });

        setLocationRequest(LocationRequest.create(),REQUEST_INTERVAL,FASTEST_REQUEST_INTERVAL, Priority.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            //noinspection MissingPermission
            fusedLocationProviderClient.requestLocationUpdates(getLocationRequest(),
                    getLocationCallback(),
                    Looper.getMainLooper());
        }

    }

    public LocationCallback getLocationCallback() {
        return locationCallback;
    }

    public void setLocationCallback(LocationCallback locationCallback) {
        this.locationCallback = locationCallback;
    }

    public LocationRequest getLocationRequest() {
        return locationRequest;
    }

    public void setLocationRequest(LocationRequest locationRequest, int interval, int fInterval, int priority) {
        this.locationRequest = locationRequest;
        this.locationRequest.setInterval(interval)
                .setFastestInterval(fInterval)
                .setPriority(priority);
    }

    public FusedLocationProviderClient getFusedLocationProviderClient() {
        return fusedLocationProviderClient;
    }

    public void setFusedLocationProviderClient(FusedLocationProviderClient fusedLocationProviderClient) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
