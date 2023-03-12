package com.example.solocarry.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.solocarry.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;
    private HashMap<String, String> images;

    public CustomInfoWindowAdapter(Context context, HashMap<String, String> images) {
        this.mContext = context;
        this.mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
        this.images = images;
    }

    @SuppressLint("SetTextI18n")
    private void renderWindow(Marker marker, View view){
        ProgressBar progressBar = view.findViewById(R.id.code_image_progress);
        String title = marker.getTitle();
        TextView tvName = view.findViewById(R.id.marker_name);
        String content = marker.getSnippet();
        TextView tvWorth = view.findViewById(R.id.marker_worth);
        tvName.setText("Name: "+title);
        tvWorth.setText(content);

        ImageView imageView = view.findViewById(R.id.marker_imageView);
        String imageName = images.get(title);
        StorageReference ref = DatabaseUtil.getFirebaseStorageInstance().getReference().child(imageName);

        ref.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri.toString()).resize(256,256).centerCrop().noFade().into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                if (marker.isInfoWindowShown()) {
                    progressBar.setVisibility(View.GONE);
                    marker.showInfoWindow();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        }));

    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return null;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        renderWindow(marker, mWindow);
        return mWindow;
    }
}
