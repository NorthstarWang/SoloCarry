package com.example.solocarry.view;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.solocarry.R;
import com.example.solocarry.controller.CodeController;
import com.example.solocarry.controller.UserController;
import com.example.solocarry.model.Code;
import com.example.solocarry.model.User;
import com.example.solocarry.util.AuthUtil;
import com.example.solocarry.util.CustomInfoWindowAdapter;
import com.example.solocarry.util.MapUtil;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.style.MIUIStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private MapUtil mapUtil;
    private AuthUtil authUtil;
    private Circle circle;
    private int radius = 1000;
    private Color circleColor;
    private com.google.android.material.floatingactionbutton.FloatingActionButton userPhoto;
    private ArrayList<Code> codeList;
    private boolean codeListChanged;
    private HashMap<String, Code> codes;
    private HashMap<String, User> owners;

    private static final int REQUEST_CAMERA_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load user info
        authUtil = new AuthUtil();

        //Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        MapStyleOptions style = new MapStyleOptions(getString(R.string.pokemon_go_map_style));
        assert mapFragment != null;
        mapUtil = new MapUtil(this,mapFragment,style);


        DialogX.init(this);
        DialogX.globalStyle = MIUIStyle.style();
        DialogX.globalTheme = DialogX.THEME.DARK;

        WaitDialog.show("Loading Map...");

        // Set up an OnPreDrawListener to the root view. (if not started drawing, stay at splash screen)
        final View content = findViewById(android.R.id.content);
        circleColor = Color.valueOf(255,232,124);
        codeListChanged = false;

        content.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public boolean onPreDraw() {
                        // Check if the initial data is ready.
                        if (mapUtil.isMapReady()) {
                            // The content is ready; start drawing.
                            content.getViewTreeObserver().removeOnPreDrawListener(this);

                            //draw circle
                            mapUtil.getLocationUtil().getFusedLocationProviderClient().requestLocationUpdates(mapUtil.getLocationUtil().getLocationRequest(), new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);
                                    if (locationResult.getLastLocation() != null) {
                                        if (circle!=null){
                                            circle.remove();
                                        }
                                        circle = mapUtil.getgMap().addCircle(new CircleOptions().center(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()))
                                                .radius(radius)
                                                .strokeColor(circleColor.toArgb())
                                                .strokeWidth(35)
                                                .strokePattern(Arrays.asList(new Dash(400), new Gap(300))));

                                        if (codeList == null) {
                                            codeList = new ArrayList<>();
                                            codes = new HashMap<>();
                                            owners = new HashMap<>();
                                            OnSuccessListener<QuerySnapshot> successListener = new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    for (DocumentSnapshot document :
                                                            queryDocumentSnapshots.getDocuments()) {
                                                        Code tempCode = document.toObject(Code.class);
                                                        codeList.add(tempCode);
                                                        codes.put(tempCode.getName(), tempCode);
                                                        UserController.getUser(Code.codesIdToUid(document.getId())[0], new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                owners.put(tempCode.getName(), UserController.transformUser(documentSnapshot));
                                                            }
                                                        }, null);
                                                        if (mapUtil.isMapReady()) {
                                                            float[] results = new float[1];
                                                            for (Code code :
                                                                    codeList) {
                                                                Location.distanceBetween(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(),
                                                                        code.getLatitude(), code.getLongitude(), results);
                                                                if (results[0] < radius) {
                                                                    LatLng markerLatLng = new LatLng(code.getLatitude(), code.getLongitude());
                                                                    UserController.getUser(Code.codesIdToUid(document.getId())[0], new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                            User tempUser = UserController.transformUser(documentSnapshot);
                                                                            mapUtil.getgMap().addMarker(new MarkerOptions()
                                                                                    .position(markerLatLng)
                                                                                    .title(code.getName())
                                                                                    .icon(BitmapDescriptorFactory.defaultMarker(Code.worthToColor(code.getScore()))));
                                                                        }
                                                                    }, null);
                                                                }
                                                            }
                                                        }
                                                    }
                                                    WaitDialog.dismiss();
                                                    //set custom marker info window
                                                    mapUtil.getgMap().setInfoWindowAdapter(new CustomInfoWindowAdapter(MainActivity.this, owners, codes));
                                                    mapUtil.getgMap().setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                                        @Override
                                                        public void onInfoWindowClick(@NonNull Marker marker) {
                                                            String title = marker.getTitle();
                                                            Intent intent = new Intent(MainActivity.this, CodeDetailActivity.class);
                                                            intent.putExtra("hashcode",codes.get(title).getHashCode());
                                                            intent.putExtra("id", owners.get(title).getUid());
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }
                                            };
                                            CodeController.getPublicCode(successListener);
                                        }else{
                                            if (codeListChanged){
                                                mapUtil.getgMap().clear();
                                                float[] results = new float[1];
                                                for (Code code:
                                                        codeList) {
                                                    Location.distanceBetween(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude(),
                                                            code.getLatitude(),code.getLongitude(), results);
                                                    if (results[0]<radius){
                                                        LatLng markerLatLng = new LatLng(code.getLatitude(),code.getLongitude());
                                                        mapUtil.getgMap().addMarker(new MarkerOptions()
                                                                .position(markerLatLng)
                                                                .title(code.getName())
                                                                .icon(BitmapDescriptorFactory.defaultMarker(Code.worthToColor(code.getScore()))));
                                                    }
                                                }
                                                //set custom marker info window
                                                mapUtil.getgMap().setInfoWindowAdapter(new CustomInfoWindowAdapter(MainActivity.this, owners, codes));

                                                codeListChanged = false;
                                            }
                                        }
                                    }
                                }
                            }, Looper.getMainLooper());

                            return true;
                        } else {
                            // The content is not ready; suspend.
                            return false;
                        }
                    }
                });

        //listen to active upload
        EventListener<QuerySnapshot> eventListener = (value, error) -> {
            if (error == null && !value.isEmpty()) {
                codeListChanged = true;
                if (codeList != null) {
                    codeList.clear();
                    owners.clear();
                    codes.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        Code code = doc.toObject(Code.class);
                        codeList.add(code);
                        codes.put(code.getName(), code);
                        UserController.getUser(Code.codesIdToUid(doc.getId())[0], new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                owners.put(code.getName(), UserController.transformUser(documentSnapshot));
                            }
                        }, null);
                    }
                }
            }
        };
        CodeController.listenToPublicCodeUpload(eventListener);

        userPhoto = findViewById(R.id.userPhoto);
        DrawableCrossFadeFactory factory =
                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .fallback(R.drawable.ic_logo)
                .override(100, 100);
        Glide.with(this)
                .load(authUtil.getUser().getPhotoUrl())
                .override(96, 96)
                .centerCrop()
                .apply(requestOptions)
                .transform(new CircleCrop())
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(userPhoto);
        userPhoto.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProfileSelfActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        FloatingActionButton signOutButton = findViewById(R.id.sign_out_dropdown_item);
        signOutButton.setOnClickListener(view -> {
            AuthUtil.SignOut();
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
        });

        com.google.android.material.floatingactionbutton.FloatingActionButton cameraButton = findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(view -> {
            // Check if camera permission is granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Request camera permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                // Camera permission already granted, proceed with camera activity
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        com.google.android.material.floatingactionbutton.FloatingActionButton chatButton = findViewById(R.id.chat_button);
        chatButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        FloatingActionButton contactButton = findViewById(R.id.contact_dropdown_item);
        contactButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ContactMenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        com.google.android.material.floatingactionbutton.FloatingActionButton rank = findViewById(R.id.ranking_button);
        rank.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RankingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        FloatingActionButton codeList = findViewById(R.id.code_list_dropdown_item);
        codeList.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CodeListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        setFloatingActionButtonTransition();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, proceed with camera activity
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            } else {
                // Camera permission denied, show error message or take alternative action
                Toast.makeText(this, "Camera permission required to use this feature", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        //stop sensor listener when pause
        if (mapUtil.getSensorManager() != null)
            mapUtil.getSensorManager().unregisterListener(mapUtil, mapUtil.getSensor());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //re-register sensor listener when resume
        if (mapUtil.getSensorManager() != null)
            mapUtil.getSensorManager().registerListener(mapUtil, mapUtil.getSensor(), SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
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
}