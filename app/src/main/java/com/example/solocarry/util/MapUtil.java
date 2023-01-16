package com.example.solocarry.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapUtil implements OnMapsSdkInitializedCallback, OnMapReadyCallback, SensorEventListener {

    /**
     * Default setting of map UI
     */
    private static final int DEFAULT_ZOOM = 17;
    private static final int DEFAULT_TILT = 85;

    /**
     * Request code for location permission request.
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * The google map object in fragment
     */
    private GoogleMap gMap;

    private LocationUtil locationUtil;
    private Context context;

    private MapStyleOptions style;

    /**
     * Sensor matrix and object
     */
    private SensorManager sensorManager;
    private Sensor sensor;

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];

    public MapUtil(Context context, SupportMapFragment mapFragment, MapStyleOptions style) {
        MapsInitializer.initialize(context.getApplicationContext(), MapsInitializer.Renderer.LATEST, this);
        mapFragment.getMapAsync(this);
        setLocationUtil(new LocationUtil(context));
        setContext(context);
        setLocationUtil(new LocationUtil(context));
        setStyle(style);
        setSensorManager((SensorManager) context.getSystemService(Context.SENSOR_SERVICE));
        setSensor(sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR));
        getSensorManager().registerListener(this,getSensor(),10000);
    }

    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public void setSensorManager(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public MapStyleOptions getStyle() {
        return style;
    }

    public void setStyle(MapStyleOptions style) {
        this.style = style;
    }

    public GoogleMap getgMap() {
        return gMap;
    }

    public void setgMap(GoogleMap gMap) {
        this.gMap = gMap;
    }

    public void setUiSettings(UiSettings uiSettings) {
        /**
         * UI Setting of google map object
         */
        uiSettings.setZoomGesturesEnabled(false);
        uiSettings.setScrollGesturesEnabled(false);
        uiSettings.setTiltGesturesEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setCompassEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public LocationUtil getLocationUtil() {
        return locationUtil;
    }

    public void setLocationUtil(LocationUtil locationUtil) {
        this.locationUtil = locationUtil;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        setgMap(googleMap);
        //UI and interaction setting
        setUiSettings(getgMap().getUiSettings());

        //request current user location
        enableMyLocation();

        // Set the map type to normal
        getgMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Move the camera to the map coordinates and zoom in closer.
        getgMap().animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
        getgMap().setBuildingsEnabled(true);

        setSelectedStyle();
        getDeviceLocation();
    }

    @Override
    public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {
        switch (renderer) {
            case LATEST:
                Log.d("MapsDemo", "The latest version of the renderer is used.");
                break;
            case LEGACY:
                Log.d("MapsDemo", "The legacy version of the renderer is used.");
                break;
        }
    }

    /**
     * Enables the My Location layer if the fine/coarse location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            getgMap().setMyLocationEnabled(true);
            return;
        }
        // 2. Otherwise, request location permissions from the user.
        ActivityCompat.requestPermissions((Activity)getContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
    }


    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        @SuppressLint("MissingPermission") Task<Location> locationResult = getLocationUtil().getFusedLocationProviderClient().getLastLocation();
        locationResult.addOnCompleteListener((Activity) getContext(), task -> {
            if (task.isSuccessful()) {
                // Set the map's camera position to the current location of the device.
                getLocationUtil().setCurrentLocation(task.getResult());
                if (getLocationUtil().getCurrentLocation() != null) {
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(getLocationUtil().getCurrentLocation().getLatitude(),
                                    getLocationUtil().getCurrentLocation().getLongitude()), DEFAULT_ZOOM));
                }
            }
        });
    }

    private void setSelectedStyle() {
        getgMap().setMapStyle(getStyle());
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR&&getLocationUtil().getCurrentLocation()!=null) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix,sensorEvent.values);
            SensorManager.getOrientation(rotationMatrix, orientationAngles);
            getgMap().moveCamera(CameraUpdateFactory.newCameraPosition(
                    new CameraPosition(new LatLng(getLocationUtil().getCurrentLocation().getLatitude(),getLocationUtil().getCurrentLocation().getLongitude()),DEFAULT_ZOOM,DEFAULT_TILT,(float)Math.toDegrees(orientationAngles[0]))));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
