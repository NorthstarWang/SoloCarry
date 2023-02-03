package com.example.solocarry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnticipateInterpolator;
import android.widget.Toast;

import androidx.core.splashscreen.SplashScreen;

import com.example.solocarry.util.AuthUtil;
import com.example.solocarry.util.MapUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private MapUtil mapUtil;
    private AuthUtil authUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);

        //Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        MapStyleOptions style = new MapStyleOptions(getString(R.string.map_style));
        assert mapFragment != null;
        mapUtil = new MapUtil(this,mapFragment,style);

        // Set up an OnPreDrawListener to the root view. (if not started drawing, stay at splash screen)
        final View content = findViewById(android.R.id.content);
        content.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        // Check if the initial data is ready.
                        if (mapUtil.isMapReady()) {
                            // The content is ready; start drawing.
                            content.getViewTreeObserver().removeOnPreDrawListener(this);
                            return true;
                        } else {
                            // The content is not ready; suspend.
                            return false;
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stop sensor listener when pause
        mapUtil.getSensorManager().unregisterListener(mapUtil,mapUtil.getSensor());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //re-register sensor listener when resume
        mapUtil.getSensorManager().registerListener(mapUtil,mapUtil.getSensor(), SensorManager.SENSOR_DELAY_NORMAL,SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Initialize user authentication
        authUtil = new AuthUtil();
        if (!authUtil.isUserNonNull()){
            //if user not sign in, open authentication activity
        }else{
            //load data of this user
            Toast.makeText(this,"Logged in",Toast.LENGTH_SHORT).show();
        }
    }
}