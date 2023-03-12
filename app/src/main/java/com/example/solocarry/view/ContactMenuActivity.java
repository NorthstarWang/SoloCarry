package com.example.solocarry.view;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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
import com.example.solocarry.model.Request;
import com.example.solocarry.model.User;
import com.example.solocarry.util.AuthUtil;
import com.example.solocarry.util.DatabaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.BottomMenu;
import com.kongzue.dialogx.dialogs.CustomDialog;
import com.kongzue.dialogx.dialogs.InputDialog;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialogx.style.MIUIStyle;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Locale;

public class ContactMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        DialogX.init(this);
        DialogX.globalStyle = MIUIStyle.style();
        DialogX.globalTheme = DialogX.THEME.DARK;

        Button btnSearchAdd = findViewById(R.id.bt_search_add);

        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();

        btnSearchAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputDialog.build()
                        .setCancelable(false)
                        .setOkButton("Search")
                        .setCancelButton("Cancel")
                        .setOkButtonClickListener(new OnInputDialogButtonClickListener<InputDialog>() {
                            @Override
                            public boolean onClick(InputDialog dialog, View v, String inputStr) {
                                WaitDialog.show("Searching...");
                                db.collection("users").whereEqualTo("email",inputStr.toLowerCase(Locale.ROOT))
                                        .whereNotEqualTo("email",AuthUtil.getFirebaseAuth().getCurrentUser().getEmail().toLowerCase(Locale.ROOT))
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                WaitDialog.dismiss();
                                                if (task.isSuccessful()) {
                                                    ArrayList<User> searchNames = new ArrayList<>();
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        searchNames.add(document.toObject(User.class));
                                                    }
                                                    if(searchNames.size()==0){
                                                        TipDialog.show("User not found!", WaitDialog.TYPE.WARNING);
                                                    }else{
                                                        MessageDialog.build()
                                                                .setTitle("Search result")
                                                                .setMessage("Is this user the one you've been searching?")
                                                                .setCancelable(false)
                                                                .setCancelButton("Back")
                                                                .setOkButton("Yes, send friend request!")
                                                                .setOkButton((dialog1, v1) -> {
                                                                    WaitDialog.show("Sending request");
                                                                    User receiver = searchNames.get(0);

                                                                    //get current user
                                                                    db.collection("users").document(AuthUtil.getFirebaseAuth().getCurrentUser().getUid())
                                                                            .get().addOnSuccessListener(documentSnapshot -> {
                                                                                // send request
                                                                                receiver.addRequests(new Request(documentSnapshot.toObject(User.class).getUid(), new Timestamp(System.currentTimeMillis())));
                                                                                db.collection("users").document(receiver.getUid())
                                                                                        .set(receiver).addOnSuccessListener(unused -> {
                                                                                            WaitDialog.dismiss();
                                                                                            TipDialog.show("Request sent!", WaitDialog.TYPE.SUCCESS);
                                                                                        });
                                                                            });
                                                                    return false;
                                                                }).setCustomView(new OnBindView<MessageDialog>(R.layout.custom_search_dialog) {
                                                                            @Override
                                                                            public void onBind(MessageDialog dialog, View v) {
                                                                                ImageView userIcon;
                                                                                userIcon = v.findViewById(R.id.img_search_user);
                                                                                DrawableCrossFadeFactory factory =
                                                                                        new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
                                                                                RequestOptions requestOptions = new RequestOptions()
                                                                                        .placeholder(R.drawable.ic_logo)
                                                                                        .error(R.drawable.ic_logo)
                                                                                        .fallback(R.drawable.ic_logo)
                                                                                        .override(48,48);
                                                                                Glide.with(ContactMenuActivity.this)
                                                                                        .load(searchNames.get(0).getPhotoUrl())
                                                                                        .override(48,48)
                                                                                        .centerCrop()
                                                                                        .apply(requestOptions)
                                                                                        .transform(new CircleCrop())
                                                                                        .transition(DrawableTransitionOptions.withCrossFade(factory))
                                                                                        .into(userIcon);

                                                                                TextView searchName = v.findViewById(R.id.search_user_name);
                                                                                searchName.setText(searchNames.get(0).getName());
                                                                            }
                                                                        }).show();
                                                    }

                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });
                                return false;
                            }
                        })
                        .setTitle("Search user by Email")
                        .setMessage("Type in Email you want to search to find your friend")
                        .show();
            }
        });
    }
}