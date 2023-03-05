package com.example.solocarry.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.solocarry.R;
import com.example.solocarry.util.AuthUtil;
import com.example.solocarry.util.MapUtil;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.github.clans.fab.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private MapUtil mapUtil;
    private AuthUtil authUtil;
    private com.google.android.material.floatingactionbutton.FloatingActionButton userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load user info
        authUtil = new AuthUtil();

        //Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        MapStyleOptions style = new MapStyleOptions(getString(R.string.black_golden_map_style));
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

        userPhoto = findViewById(R.id.userPhoto);
        DrawableCrossFadeFactory factory =
                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .fallback(R.drawable.ic_logo)
                .override(100,100);
        Glide.with(this)
                .load(authUtil.getUser().getPhotoUrl())
                .override(96,96)
                .centerCrop()
                .apply(requestOptions)
                .transform(new CircleCrop())
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(userPhoto);

        FloatingActionButton button = findViewById(R.id.sign_out_dropdown_item);
        button.setOnClickListener(view -> {
            AuthUtil.SignOut();
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
        });

        setFloatingActionButtonTransition();
        setCodePanel();
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
    }

    private void setFloatingActionButtonTransition(){
        final FloatingActionMenu fam = (FloatingActionMenu) findViewById(R.id.dropdown_menu);
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(fam.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(fam.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(fam.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(fam.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                fam.getMenuIconView().setImageResource(fam.isOpened()
                        ? R.drawable.ic_menu : R.drawable.ic_expand_less);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        fam.setIconToggleAnimatorSet(set);
    }

    private void setCodePanel(){
        final com.google.android.material.floatingactionbutton.FloatingActionButton filterButton = findViewById(R.id.button_filter);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterFragment dialogFrag = FilterFragment.newInstance();
                dialogFrag.setParentFab(filterButton);
                dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag());
            }
        });
    }
}