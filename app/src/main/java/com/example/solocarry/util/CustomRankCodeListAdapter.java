package com.example.solocarry.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.example.solocarry.model.CodeInMap;
import com.example.solocarry.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
/**
 * This CustomRankCodeListAdapter class is an adapter for rank of codeInMap lists
 */
public class CustomRankCodeListAdapter extends BaseAdapter {

    private ArrayList<CodeInMap> dataSet;
    private Context mContext;
    private TextView userName, rank, rank_score;
    private ImageView image;

    /**
     * This constructor for CustomRankCodeListAdapter class
     * @param context the app context
     * @param dataSet the array list of CodeInMap objects
     */
    public CustomRankCodeListAdapter(Context context, ArrayList<CodeInMap> dataSet) {
        this.dataSet = dataSet;
        this.mContext = context;
    }

    /**
     * This getCount method returns the number of in map codes included
     * @return int
     */
    @Override
    public int getCount() {
        return dataSet.size();
    }

    /**
     * This getItem method returns the codeInMap object at given position
     * @param i the given position at arraylist
     * @return CodeInMap object
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
     * This getView method constructs the view for this CustomRankCodeList adapter
     * @return view
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.rank_content, viewGroup, false);
        rank = view.findViewById(R.id.textView_rank);
        userName = view.findViewById(R.id.rank_user_name);
        image = view.findViewById(R.id.rank_user_photo);
        rank_score = view.findViewById(R.id.rank_score);

        CodeInMap code = dataSet.get(i);
        DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .fallback(R.drawable.ic_logo)
                .override(64,64);
        Glide.with(mContext)
                .load("https://robohash.org/"+code.getHashCode())
                .override(64,64)
                .centerCrop()
                .apply(requestOptions)
                .transform(new CircleCrop())
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(image);

        userName.setText(code.getOwnerIds().size() + " owners");
        rank.setText(String.valueOf(4 + i));
        rank_score.setText(String.valueOf(code.getScore()));

        return view;
    }
}