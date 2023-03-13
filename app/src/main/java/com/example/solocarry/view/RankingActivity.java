package com.example.solocarry.view;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.example.solocarry.model.User;
import com.example.solocarry.util.CustomRankListAdapter;
import com.example.solocarry.util.DatabaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;


public class RankingActivity extends AppCompatActivity {

    private View view;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = ActivityRankingBinding.inflate(getLayoutInflater()).getRoot();
        setContentView(view);

        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();

        ImageView backButton = findViewById(R.id.rank_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void loadRank(ArrayList<User> rankList) {
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
            ListView listView = findViewById(R.id.ranking_list);
            CustomRankListAdapter rankListAdapter = new CustomRankListAdapter(RankingActivity.this, rankList);
            listView.setAdapter(rankListAdapter);
        }
    }
}