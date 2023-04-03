package com.example.solocarry.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.solocarry.R;
import com.example.solocarry.model.Code;
import com.example.solocarry.model.User;

import java.util.ArrayList;
/**
 * This CustomOwnerListAdapter class is an adapter for owner list of a particular code
 */
public class CustomOwnerListAdapter extends BaseAdapter {
    private ArrayList<User> dataSet;
    private Context mContext;

    /**
     * This constructor for CustomOwnerListAdapter class
     * @param context the app context
     * @param dataSet the array list of users which represents the owners
     */
    public CustomOwnerListAdapter(Context context, ArrayList<User> dataSet) {
        this.dataSet = dataSet;
        this.mContext = context;
    }

    /**
     * This getCount method returns the number of owners included
     * @return int
     */
    @Override
    public int getCount() {
        return dataSet.size();
    }

    /**
     * This getItem method returns the owner object at given position
     * @param i the given position
     * @return User object
     */
    @Override
    public User getItem(int i) {
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
     * This getView method constructs the view for this CustomOwnerList adapter
     * @return view
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.pin_code_user_list_content, viewGroup, false);
        ImageView image = view.findViewById(R.id.user_image);
        TextView userName = view.findViewById(R.id.username);
        User user = dataSet.get(i);
        DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .fallback(R.drawable.ic_logo)
                .override(48, 48);
        Glide.with(mContext)
                .load(user.getPhotoUrl())
                .override(48, 48)
                .centerCrop()
                .apply(requestOptions)
                .transform(new CircleCrop())
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(image);
        userName.setText(dataSet.get(i).getName());

        return view;
    }
}
