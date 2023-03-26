package com.example.solocarry.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
import com.example.solocarry.databinding.ProfileBinding;
import com.example.solocarry.model.Code;
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

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = ProfileBinding.inflate(getLayoutInflater()).getRoot();
        setContentView(view);

        DialogX.init(this);
        DialogX.globalStyle = MIUIStyle.style();
        DialogX.globalTheme = DialogX.THEME.DARK;

        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();
        ImageView userPhoto = findViewById(R.id.user_photo);
        TextView profileName = findViewById(R.id.profile_name);
        TextView totalScore = findViewById(R.id.total_score_number);
        TextView totalCode = findViewById(R.id.total_code_number);
        TextView highest = findViewById(R.id.highest_score_number);
        TextView email = findViewById(R.id.email);

        WaitDialog.show("Loading...");
        UserController.getUser(AuthUtil.getFirebaseAuth().getUid(), new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = UserController.transformUser(documentSnapshot);
                CodeController.getCodeCount(user.getUid(), new OnSuccessListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onSuccess(AggregateQuerySnapshot aggregateQuerySnapshot) {
                        totalCode.setText(String.valueOf(aggregateQuerySnapshot.getCount()));
                    }
                });
                CodeController.getExtreme(user.getUid(), Query.Direction.DESCENDING, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                    }
                });
                DrawableCrossFadeFactory factory =
                        new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.ic_logo)
                        .error(R.drawable.ic_logo)
                        .fallback(R.drawable.ic_logo)
                        .override(120, 120);
                Glide.with(ProfileActivity.this)
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