package com.example.solocarry.util;

import android.content.Context;
import android.content.Intent;
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
import com.example.solocarry.view.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
/**
 * This CustomFriendListAdapter class is an adapter for friend list
 */
public class CustomFriendListAdapter extends BaseAdapter {

    private ArrayList<String> dataSet;
    private Context mContext;
    private TextView userName;
    private ImageView image;

    /**
     * This constructor for CustomFriendListAdapter class
     * @param context the app context
     * @param dataSet the array list of friends represented by strings
     */
    public CustomFriendListAdapter(Context context, ArrayList<String> dataSet) {
        this.dataSet = dataSet;
        this.mContext = context;
    }

    /**
     * This getCount method returns the number of friends included
     * @return int
     */
    @Override
    public int getCount() {
        return dataSet.size();
    }

    /**
     * This getItem method returns the friend uid at given position
     * @return Object
     */
    @Override
    public Object getItem(int i) {
        return dataSet.get(i);
    }

    /**
     * This getItemId method returns a specific position
     * @return long
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * This getView method constructs the view for this CustomFriendList adapter
     * @return view
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();

        view = LayoutInflater.from(mContext).inflate(R.layout.friend_list_content, viewGroup, false);
        userName = view.findViewById(R.id.friend_name);
        image = view.findViewById(R.id.friend_photo);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("id", dataSet.get(i));
                mContext.startActivity(intent);
            }
        });

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
