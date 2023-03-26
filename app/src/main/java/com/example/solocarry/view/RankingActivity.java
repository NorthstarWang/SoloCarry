package com.example.solocarry.view;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.solocarry.R;
import com.example.solocarry.controller.UserController;
import com.example.solocarry.databinding.ActivityRankingBinding;
import com.example.solocarry.model.CodeInMap;
import com.example.solocarry.model.User;
import com.example.solocarry.util.CustomRankCodeListAdapter;
import com.example.solocarry.util.CustomRankListAdapter;
import com.example.solocarry.util.DatabaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.style.MIUIStyle;

import java.util.ArrayList;
import java.util.Collections;


public class RankingActivity extends AppCompatActivity {

    private View view;
    private ArrayList<User> users;
    private ArrayList<CodeInMap> codes;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = ActivityRankingBinding.inflate(getLayoutInflater()).getRoot();
        setContentView(view);

        listView = findViewById(R.id.ranking_list);

        DialogX.init(this);
        DialogX.globalStyle = MIUIStyle.style();
        DialogX.globalTheme = DialogX.THEME.DARK;

        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();

        ImageView backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        clearRank();

        Switch rankSwitch = findViewById(R.id.switch_rank);
        rankSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                WaitDialog.show("Loading Data...");
                if(!b){
                    compoundButton.setText("World User Ranking");
                    db.collection("users").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        users = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            users.add(UserController.transformUser(document));
                                        }
                                        //sort user
                                        Collections.sort(users, Collections.reverseOrder());
                                        loadRank(users);
                                        WaitDialog.dismiss();
                                        TipDialog.show("Code Ranking loaded", WaitDialog.TYPE.SUCCESS);
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                        TipDialog.show("Code Ranking load failure", WaitDialog.TYPE.ERROR);
                                    }
                                }
                            });
                }else{
                    compoundButton.setText("World Code Ranking");
                    db.collection("codeMap").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        codes = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            codes.add(document.toObject(CodeInMap.class));
                                        }
                                        Collections.sort(codes, Collections.reverseOrder());
                                        loadCodeRank(codes);
                                        WaitDialog.dismiss();
                                        TipDialog.show("User Ranking loaded", WaitDialog.TYPE.SUCCESS);
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                        TipDialog.show("User Ranking load failure", WaitDialog.TYPE.ERROR);
                                    }
                                }
                            });
                }
            }
        });

        rankSwitch.setChecked(false);
        rankSwitch.setText("World User Ranking");

        WaitDialog.show("Loading Data...");

        db.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            users = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                users.add(UserController.transformUser(document));
                            }
                            //sort user
                            Collections.sort(users, Collections.reverseOrder());
                            loadRank(users);
                            WaitDialog.dismiss();
                            TipDialog.show("User Ranking loaded", WaitDialog.TYPE.SUCCESS);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            TipDialog.show("User Ranking load failure", WaitDialog.TYPE.ERROR);
                        }
                    }
                });
    }

    private void clearRank(){
        DrawableCrossFadeFactory factory =
                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.user_default)
                .error(R.mipmap.user_default)
                .fallback(R.mipmap.user_default)
                .override(100, 100);

        ImageView firstImage = findViewById(R.id.imageView_first);
        TextView firstName = findViewById(R.id.first_name);
        TextView firstScore = findViewById(R.id.first_score);
        firstName.setText("");
        Glide.with(this)
                .load(R.mipmap.user_default)
                .override(104, 104)
                .centerCrop()
                .apply(requestOptions)
                .transform(new CircleCrop())
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(firstImage);
        firstScore.setText("");

        ImageView secondImage = findViewById(R.id.imageView_second);
        TextView secondName = findViewById(R.id.second_name);
        TextView secondScore = findViewById(R.id.second_score);
        secondName.setText("");
        Glide.with(this)
                .load(R.mipmap.user_default)
                .override(88, 88)
                .centerCrop()
                .apply(requestOptions)
                .transform(new CircleCrop())
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(secondImage);
        secondScore.setText("");

        ImageView thirdImage = findViewById(R.id.imageView_third);
        TextView thirdName = findViewById(R.id.third_name);
        TextView thirdScore = findViewById(R.id.third_score);
        thirdName.setText("");
        Glide.with(this)
                .load(R.mipmap.user_default)
                .override(72, 72)
                .centerCrop()
                .apply(requestOptions)
                .transform(new CircleCrop())
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(thirdImage);
        thirdScore.setText("");

        listView.setAdapter(null);
    }

    private void loadCodeRank(ArrayList<CodeInMap> rankList){
        clearRank();

        DrawableCrossFadeFactory factory =
                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.user_default)
                .error(R.mipmap.user_default)
                .fallback(R.mipmap.user_default)
                .override(100, 100);

        if (rankList.size() > 0) {
            CodeInMap firstRank = rankList.get(0);
            ImageView firstImage = findViewById(R.id.imageView_first);
            TextView firstName = findViewById(R.id.first_name);
            TextView firstScore = findViewById(R.id.first_score);
            firstScore.setText(String.valueOf(firstRank.getScore()));
            Glide.with(this)
                    .load("https://robohash.org/"+firstRank.getHashCode())
                    .override(104, 104)
                    .centerCrop()
                    .apply(requestOptions)
                    .transform(new CircleCrop())
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .into(firstImage);
            firstName.setText(firstRank.getOwnerIds().size() + " owners");
        }


        if (rankList.size() > 1) {
            CodeInMap secondRank = rankList.get(1);
            ImageView secondImage = findViewById(R.id.imageView_second);
            TextView secondName = findViewById(R.id.second_name);
            TextView secondScore = findViewById(R.id.second_score);
            secondScore.setText(String.valueOf(secondRank.getScore()));
            Glide.with(this)
                    .load("https://robohash.org/"+secondRank.getHashCode())
                    .override(88, 88)
                    .centerCrop()
                    .apply(requestOptions)
                    .transform(new CircleCrop())
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .into(secondImage);
            secondName.setText(secondRank.getOwnerIds().size() + " owners");
        }


        if (rankList.size() > 2) {
            CodeInMap thirdRank = rankList.get(2);
            ImageView thirdImage = findViewById(R.id.imageView_third);
            TextView thirdName = findViewById(R.id.third_name);
            TextView thirdScore = findViewById(R.id.third_score);
            thirdScore.setText(String.valueOf(thirdRank.getScore()));
            Glide.with(this)
                    .load("https://robohash.org/"+thirdRank.getHashCode())
                    .override(72, 72)
                    .centerCrop()
                    .apply(requestOptions)
                    .transform(new CircleCrop())
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .into(thirdImage);
            thirdName.setText(thirdRank.getOwnerIds().size() + " owners");
        }

        if (rankList.size()>3){
            for (int i = 0; i < 3; i++) {
                rankList.remove(0);
            }
            CustomRankCodeListAdapter rankListAdapter = new CustomRankCodeListAdapter(RankingActivity.this, rankList);
            listView.setAdapter(rankListAdapter);
            rankListAdapter.notifyDataSetChanged();
        }
    }

    private void loadRank(ArrayList<User> rankList) {
        clearRank();

        DrawableCrossFadeFactory factory =
                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .fallback(R.drawable.ic_logo)
                .override(100, 100);

        if (rankList.size() > 0) {
            User firstRank = rankList.get(0);
            ImageView firstImage = findViewById(R.id.imageView_first);
            TextView firstName = findViewById(R.id.first_name);
            TextView firstScore = findViewById(R.id.first_score);
            firstScore.setText(String.valueOf(firstRank.getScore()));
            Glide.with(this)
                    .load(firstRank.getPhotoUrl())
                    .override(104, 104)
                    .centerCrop()
                    .apply(requestOptions)
                    .transform(new CircleCrop())
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .into(firstImage);
            firstName.setText(firstRank.getName());
        }


        if (rankList.size() > 1) {
            User secondRank = rankList.get(1);
            ImageView secondImage = findViewById(R.id.imageView_second);
            TextView secondName = findViewById(R.id.second_name);
            TextView secondScore = findViewById(R.id.second_score);
            secondScore.setText(String.valueOf(secondRank.getScore()));
            Glide.with(this)
                    .load(secondRank.getPhotoUrl())
                    .override(88, 88)
                    .centerCrop()
                    .apply(requestOptions)
                    .transform(new CircleCrop())
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .into(secondImage);
            secondName.setText(secondRank.getName());
        }


        if (rankList.size() > 2) {
            User thirdRank = rankList.get(2);
            ImageView thirdImage = findViewById(R.id.imageView_third);
            TextView thirdName = findViewById(R.id.third_name);
            TextView thirdScore = findViewById(R.id.third_score);
            thirdScore.setText(String.valueOf(thirdRank.getScore()));
            Glide.with(this)
                    .load(thirdRank.getPhotoUrl())
                    .override(72, 72)
                    .centerCrop()
                    .apply(requestOptions)
                    .transform(new CircleCrop())
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .into(thirdImage);
            thirdName.setText(thirdRank.getName());
        }

        if (rankList.size()>3){
            for (int i = 0; i < 3; i++) {
                rankList.remove(0);
            }
            CustomRankListAdapter rankListAdapter = new CustomRankListAdapter(RankingActivity.this, rankList);
            listView.setAdapter(rankListAdapter);
            rankListAdapter.notifyDataSetChanged();
        }
    }
}