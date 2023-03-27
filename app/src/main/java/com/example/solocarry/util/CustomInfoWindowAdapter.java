package com.example.solocarry.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import com.example.solocarry.model.Code;
import com.example.solocarry.model.User;
import com.example.solocarry.view.CodeDetailActivity;
import com.example.solocarry.view.ProfileActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;
    private HashMap<String, User> owners;
    private HashMap<String, Code> codes;
    private String previousImageUrl = "";

    public CustomInfoWindowAdapter(Context context, HashMap<String, User> owners, HashMap<String, Code> codes) {
        this.mContext = context;
        this.mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
        this.owners = owners;
        this.codes = codes;
    }

    @SuppressLint("SetTextI18n")
    private void renderWindow(Marker marker, View view){
        String title = marker.getTitle();
        TextView tvName = view.findViewById(R.id.marker_name);
        TextView tvWorth = view.findViewById(R.id.marker_worth);
        tvName.setText("Name: "+title);
        tvWorth.setText("Worth: "+codes.get(title).getScore() + "\nOwner: "+owners.get(title).getName());

        ImageView imageView = view.findViewById(R.id.marker_imageView);
        String imageName = codes.get(title).getPhoto();

        if(!TextUtils.equals(previousImageUrl, imageName)){
            imageView.setImageResource(0);
            ProgressBar progressBar = view.findViewById(R.id.code_image_progress);
            progressBar.setVisibility(View.VISIBLE);
            StorageReference ref = DatabaseUtil.getFirebaseStorageInstance().getReference().child(imageName);
            Log.e("info","trigger");
            ref.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri.toString())
                    .resize(256,256)
                    .centerCrop().noFade()
                    .transform(new CircleTransform())
                    .into(imageView, new Callback() {
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
            previousImageUrl = imageName;
        }

    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        renderWindow(marker, mWindow);
        return mWindow;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        return null;
    }

    class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
