package com.example.solocarry.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.solocarry.R;
import com.example.solocarry.model.Code;
import com.example.solocarry.model.CodeInMap;

import java.util.ArrayList;

public class CustomCodeListAdapter extends BaseAdapter {
    private ArrayList<Code> dataSet;
    private Context mContext;

    public CustomCodeListAdapter(Context context, ArrayList<Code> dataSet) {
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
        view = LayoutInflater.from(mContext).inflate(R.layout.profile_code_list_content, viewGroup, false);
        ImageView image = view.findViewById(R.id.code_image);
        TextView codeName = view.findViewById(R.id.code_name_text);
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
}
