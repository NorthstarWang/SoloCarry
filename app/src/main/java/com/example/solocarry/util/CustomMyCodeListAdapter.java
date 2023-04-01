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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/**
 * This CustomMyCodeListAdapter class is an adapter for my code list
 */
public class CustomMyCodeListAdapter extends BaseAdapter {
    private ArrayList<Code> dataSet;
    private Context mContext;
    private boolean mAscending;

    /**
     * This constructor for CustomMyCodeListAdapter class
     * @param context the app context
     * @param dataSet the array list of codes
     */
    public CustomMyCodeListAdapter(Context context, ArrayList<Code> dataSet) {
        this.dataSet = dataSet;
        this.mContext = context;
    }

    /**
     * This getCount method returns the number of codes included in my code list
     * @return int
     */
    @Override
    public int getCount() {
        return dataSet.size();
    }

    /**
     * This getItem method returns the code object at given position
     * @return code
     */
    @Override
    public Code getItem(int i) {
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
     * This getView method constructs the view for this CustomMyFriendList adapter
     * @return view
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.my_code_list_content, viewGroup, false);
        ImageView image = view.findViewById(R.id.code_image);
        TextView codeName = view.findViewById(R.id.code_name);
        TextView rank_score = view.findViewById(R.id.rank_score);
        Code code = dataSet.get(i);
        DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .fallback(R.drawable.ic_logo)
                .override(48,48);
        Glide.with(mContext)
                .load("https://robohash.org/"+code.getHashCode())
                .override(48,48)
                .centerCrop()
                .apply(requestOptions)
                .transform(new CircleCrop())
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(image);
        codeName.setText(dataSet.get(i).getName());
        rank_score.setText(String.valueOf(dataSet.get(i).getScore()));

        return view;
    }

    /**
     * This sort method sort codes by specific order using their score
     */
    public void sort(){
        if (mAscending) {
            Collections.sort(dataSet, new Comparator<Code>() {
                @Override
                public int compare(Code data1, Code data2) {
                    return data1.getScore() - data2.getScore();
                }
            });
        } else {
            Collections.sort(dataSet, new Comparator<Code>() {
                @Override
                public int compare(Code data1, Code data2) {
                    return data2.getScore() - data1.getScore();
                }
            });
        }
        mAscending = !mAscending;
        notifyDataSetChanged();
    }
}
