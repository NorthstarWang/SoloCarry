package com.example.solocarry.util;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.solocarry.R;
import com.example.solocarry.controller.UserController;
import com.example.solocarry.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomRequestListViewAdapter extends BaseAdapter {

    private ArrayList<User> dataSet;
    private Context mContext;
    private TextView userName;
    private ImageView image;
    private ImageButton acceptButton, declineButton;

    public CustomRequestListViewAdapter(Context context, ArrayList<User> dataSet) {
        this.dataSet = dataSet;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int i) {
        return dataSet.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();

        view = LayoutInflater.from(mContext).inflate(R.layout.friend_request_content, viewGroup, false);
        userName = view.findViewById(R.id.friend_request_name);
        image = view.findViewById(R.id.friend_request_photo);
        acceptButton = view.findViewById(R.id.request_acceptButton);
        declineButton = view.findViewById(R.id.request_declineButton);

        DrawableCrossFadeFactory factory =
                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .fallback(R.drawable.ic_logo)
                .override(64,64);
        Glide.with(mContext)
                .load(dataSet.get(i).getPhotoUrl())
                .override(64,64)
                .centerCrop()
                .apply(requestOptions)
                .transform(new CircleCrop())
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(image);

        userName.setText(dataSet.get(i).getName());

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WaitDialog.show("Accepting...");
                db.collection("users").document(AuthUtil.getFirebaseAuth().getCurrentUser().getUid())
                        .update("friends", FieldValue.arrayUnion(dataSet.get(i).getUid()))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    db.collection("users").document(AuthUtil.getFirebaseAuth().getCurrentUser().getUid())
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            List<HashMap<String, Object>> requests = (List<HashMap<String, Object>>) document.get("requests");
                                                            for (HashMap<String, Object> request : requests) {
                                                                //find the specific request
                                                                String uid = dataSet.get(i).getUid();
                                                                if(request.get("sender").toString().equals(uid)){
                                                                    db.collection("users").document(AuthUtil.getFirebaseAuth().getCurrentUser().getUid())
                                                                            .update("requests", FieldValue.arrayRemove(request))
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        WaitDialog.dismiss();
                                                                                        TipDialog.show("Request accepted", WaitDialog.TYPE.SUCCESS);
                                                                                        notifyDataSetChanged();
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        } else {
                                                            Log.d(TAG, "No such document");
                                                        }
                                                    } else {
                                                        Log.d(TAG, "get failed with ", task.getException());
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WaitDialog.show("Declining...");
                db.collection("users").document(AuthUtil.getFirebaseAuth().getCurrentUser().getUid())
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        List<HashMap<String, Object>> requests = (List<HashMap<String, Object>>) document.get("requests");
                                        for (HashMap<String, Object> request : requests) {
                                            //find the specific request
                                            String uid = dataSet.get(i).getUid();
                                            if(request.get("sender").toString().equals(uid)){
                                                db.collection("users").document(AuthUtil.getFirebaseAuth().getCurrentUser().getUid())
                                                        .update("requests", FieldValue.arrayRemove(request))
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    WaitDialog.dismiss();
                                                                    TipDialog.show("Request declined", WaitDialog.TYPE.SUCCESS);
                                                                    notifyDataSetChanged();
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
            }
        });

        return view;
    }
}
