package com.example.solocarry.view;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.solocarry.R;
import com.example.solocarry.controller.CodeController;
import com.example.solocarry.controller.UserController;
import com.example.solocarry.databinding.ActivityProfilesSelfBinding;
import com.example.solocarry.model.Code;
import com.example.solocarry.model.CodeInMap;
import com.example.solocarry.model.User;
import com.example.solocarry.util.AuthUtil;
import com.example.solocarry.util.DatabaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.style.MIUIStyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.IntStream;
/**
 * This Profile self activity reflect the user's own personal page, when a user want to know current
 * information about himself, the activity shows all code scores information, email, picture,
 * and ranking information about himself.
 */
public class ProfileSelfActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = ActivityProfilesSelfBinding.inflate(getLayoutInflater()).getRoot();
        setContentView(view);

        DialogX.init(this);
        DialogX.globalStyle = MIUIStyle.style();
        DialogX.globalTheme = DialogX.THEME.DARK;

        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();
        ImageView userPhoto = findViewById(R.id.profile_pic2);
        TextView profileName = findViewById(R.id.username_info);
        TextView totalScore = findViewById(R.id.sum_score_number);
        TextView totalCode = findViewById(R.id.total_code_number);
        TextView highest = findViewById(R.id.highest_code_score_number);
        TextView lowest = findViewById(R.id.lowest_code_score_number);
        TextView highestName = findViewById(R.id.highest_code_score_name_text);
        TextView lowestName = findViewById(R.id.lowest_code_score_name_text);
        TextView email = findViewById(R.id.email_info);
        TextView worldRank = findViewById(R.id.total_score_rank);
        TextView codeRank = findViewById(R.id.best_code_rank);
        Button backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        WaitDialog.show("Loading...");
        UserController.getUser(AuthUtil.getFirebaseAuth().getUid(), new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = UserController.transformUser(documentSnapshot);

                ArrayList<User> users = new ArrayList<>();

                db.collection("users").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        users.add(UserController.transformUser(document));
                                    }
                                    //sort user
                                    Collections.sort(users, Collections.reverseOrder());
                                    int index = IntStream.range(0, users.size())
                                            .filter(i -> Objects.equals(users.get(i).getUid(), AuthUtil.getFirebaseAuth().getUid()))
                                            .findFirst()
                                            .orElse(-1);
                                    worldRank.setText(String.valueOf(index+1));
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

                CodeController.getHighestCodeRank(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<CodeInMap> codeInMaps = new ArrayList<>();
                        for (DocumentSnapshot document:
                                queryDocumentSnapshots) {
                            codeInMaps.add(document.toObject(CodeInMap.class));
                        }
                        for (int i = 0; i < codeInMaps.size(); i++) {
                            if(codeInMaps.get(i).getOwnerIds().contains(AuthUtil.getFirebaseAuth().getUid())){
                                codeRank.setText(String.valueOf(i+1));
                                return;
                            }
                        }
                        codeRank.setText("N/A");
                    }
                });

                CodeController.getCodeCount(user.getUid(), new OnSuccessListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onSuccess(AggregateQuerySnapshot aggregateQuerySnapshot) {
                        totalCode.setText(String.valueOf(aggregateQuerySnapshot.getCount()));
                    }
                });
                CodeController.getExtreme(user.getUid(), Query.Direction.DESCENDING, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value!=null&&!value.getDocuments().isEmpty()){
                            Code codeHigh = value.getDocuments().get(0).toObject(Code.class);
                            highest.setText(String.valueOf(codeHigh.getScore()));
                            highestName.setText(codeHigh.getName());
                        }
                    }
                });
                CodeController.getExtreme(user.getUid(), Query.Direction.ASCENDING, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value!=null&&!value.getDocuments().isEmpty()){
                            Code codeLow = value.getDocuments().get(0).toObject(Code.class);
                            lowest.setText(String.valueOf(codeLow.getScore()));
                            lowestName.setText(codeLow.getName());
                        }
                    }
                });
                DrawableCrossFadeFactory factory =
                        new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.ic_logo)
                        .error(R.drawable.ic_logo)
                        .fallback(R.drawable.ic_logo)
                        .override(120, 120);
                Glide.with(ProfileSelfActivity.this)
                        .load(user.getPhotoUrl())
                        .override(120, 120)
                        .centerCrop()
                        .apply(requestOptions)
                        .transform(new CircleCrop())
                        .transition(DrawableTransitionOptions.withCrossFade(factory))
                        .into(userPhoto);
                profileName.setText(user.getName());
                totalScore.setText(String.valueOf(user.getScore()));
                email.setText(user.getEmail());
                db.collection("users").document(AuthUtil.getFirebaseAuth().getUid()).collection("codes")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    WaitDialog.dismiss();
                                    for (QueryDocumentSnapshot document:task.getResult()){

                                    }
                                }
                            }
                        });

            }
        }, null);
    }
}
