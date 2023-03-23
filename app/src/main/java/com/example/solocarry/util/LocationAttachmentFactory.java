package com.example.solocarry.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.android.volley.toolbox.ImageRequest;
import com.example.solocarry.R;
import com.example.solocarry.databinding.CustomLocationAttachmentBinding;
import com.example.solocarry.databinding.CustomMapViewAttachmentBinding;
import com.getstream.sdk.chat.images.RoundedCornersTransformation;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.jetbrains.annotations.NotNull;

import io.getstream.chat.android.client.models.Attachment;
import io.getstream.chat.android.client.models.Message;
import io.getstream.chat.android.ui.message.list.adapter.MessageListListenerContainer;
import io.getstream.chat.android.ui.message.list.adapter.viewholder.attachment.AttachmentFactory;
import io.getstream.chat.android.ui.message.list.adapter.viewholder.attachment.InnerAttachmentViewHolder;

public class LocationAttachmentFactory implements AttachmentFactory {

    @Override
    public boolean canHandle(@NonNull Message message) {
        return containsLocationAttachments(message)!=null;
    }

    @NonNull
    @Override
    public InnerAttachmentViewHolder createViewHolder(@NonNull Message message, @Nullable MessageListListenerContainer messageListListenerContainer, @NonNull ViewGroup viewGroup) {
        Attachment attachment = containsLocationAttachments(message);
        CustomMapViewAttachmentBinding customLocationAttachmentBinding = CustomMapViewAttachmentBinding.inflate(LayoutInflater.from(viewGroup.getContext()), null, false);
        return new LocationAttachmentViewHolder(customLocationAttachmentBinding, attachment, viewGroup.getContext());
    }

    private Attachment containsLocationAttachments(@NotNull Message message) {
        for (int i = 0; i < message.getAttachments().size(); i++) {

            if (message.getAttachments().get(i).getExtraData().get("lat")!=null) {
                return message.getAttachments().get(i);
            }
        }

        return null;
    }

    private class LocationAttachmentViewHolder extends InnerAttachmentViewHolder {

        public LocationAttachmentViewHolder(CustomMapViewAttachmentBinding binding,
                                            @Nullable Attachment locationAttachment, Context context) {
            super(binding.getRoot());

            ImageView mapView = binding.mapView;

            Picasso.get().load("https://maps.googleapis.com/maps/api/staticmap?center="+ locationAttachment.getExtraData().get("lat").toString() +"%2c%20"
                    + (locationAttachment.getExtraData().get("lon").toString()) + "&zoom=14&size=200x200&markers=color:red%7Clabel:%7C"
                    + (locationAttachment.getExtraData().get("lat").toString()) +","
                    + (locationAttachment.getExtraData().get("lon").toString()) +"&key=AIzaSyCHas4KicdbU0WtSpdP-lY8b5p9-lFNnOU&map_id=80dc890679be7bda")
                    .transform(new RoundedCornersTransform()).into(mapView);

            mapView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String googleMapsUrl = "geo:"+ (locationAttachment.getExtraData().get("lat").toString()) +","+locationAttachment.getExtraData().get("lon").toString();
                    Uri gmmIntentUri = Uri.parse(googleMapsUrl);
                    // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    // Make the Intent explicit by setting the Google Maps package
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(mapIntent);
                    }
                }
            });
        }


    }

    public class RoundedCornersTransform implements Transformation {
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
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            canvas.drawRoundRect(new RectF(0, 0, source.getWidth(), source.getHeight()), 8, 8, paint);
            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "rounded_corners";
        }
    }

}
