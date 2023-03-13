package com.example.solocarry.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CustomFriendListAdapter extends BaseAdapter {

    private ArrayList<String> dataSet;
    private Context mContext;
    private TextView userName;
    private ImageView image;

    public CustomFriendListAdapter(Context context, ArrayList<String> dataSet) {
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

        view = LayoutInflater.from(mContext).inflate(R.layout.friend_list_content, viewGroup, false);
        userName = view.findViewById(R.id.friend_name);
        image = view.findViewById(R.id.friend_photo);

        db.collection("users").document(dataSet.get(i)).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            User user = UserController.transformUser(task.getResult());
                            DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
                            RequestOptions requestOptions = new RequestOptions()
                                    .placeholder(R.drawable.ic_logo)
                                    .error(R.drawable.ic_logo)
                                    .fallback(R.drawable.ic_logo)
                                    .override(64,64);
                            Glide.with(mContext)
                                    .load(user.getPhotoUrl())
                                    .override(64,64)
                                    .centerCrop()
                                    .apply(requestOptions)
                                    .transform(new CircleCrop())
                                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                                    .into(image);

                            userName.setText(user.getName());
                        }
                    }
                });

        return view;
    }
}
