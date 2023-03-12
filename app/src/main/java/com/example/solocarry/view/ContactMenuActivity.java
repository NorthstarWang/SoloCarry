package com.example.solocarry.view;

import static android.content.ContentValues.TAG;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.solocarry.model.Request;
import com.example.solocarry.model.User;
import com.example.solocarry.util.AuthUtil;
import com.example.solocarry.util.CustomRequestListViewAdapter;
import com.example.solocarry.util.DatabaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.InputDialog;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.style.MIUIStyle;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        ImageView back = findViewById(R.id.button_back);

        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();

        //search user by email function
        btnSearchAdd.setOnClickListener(view -> InputDialog.build()
                .setCancelable(false)
                .setOkButton("Search")
                .setCancelButton("Cancel")
                .setOkButtonClickListener((dialog, v, inputStr) -> {
                    WaitDialog.show("Searching...");
                    db.collection("users").whereEqualTo("email", inputStr.toLowerCase(Locale.ROOT))
                            .whereNotEqualTo("email", AuthUtil.getFirebaseAuth().getCurrentUser().getEmail().toLowerCase(Locale.ROOT))
                            .get().addOnCompleteListener(task -> {
                                WaitDialog.dismiss();
                                if (task.isSuccessful()) {
                                    if (task.getResult().size() == 0) {
                                        TipDialog.show("User not found!", WaitDialog.TYPE.WARNING);
                                    } else {
                                        String searchUID = (String) task.getResult().getDocuments().get(0).get("uid");
                                        String searchPhotoUrl  = (String) task.getResult().getDocuments().get(0).get("photoUrl");
                                        String searchFullName  = (String) task.getResult().getDocuments().get(0).get("name");
                                        MessageDialog.build()
                                                .setTitle("Search result")
                                                .setMessage("Is this user the one you've been searching?")
                                                .setCancelable(false)
                                                .setCancelButton("Back")
                                                .setOkButton("Yes, send friend request!")
                                                .setOkButton((dialog1, v1) -> {
                                                    WaitDialog.show("Sending request");
                                                    //check there is no previous request from same user
                                                    db.collection("users").document(searchUID).get()
                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        DocumentSnapshot document = task.getResult();
                                                                        if (document.exists()) {
                                                                            //check user has no such friend
                                                                            User receiver = UserController.transformUser(document);
                                                                            if (receiver.getFriends().contains(AuthUtil.getFirebaseAuth().getUid())){
                                                                                WaitDialog.dismiss();
                                                                                TipDialog.show("You are already friend!", WaitDialog.TYPE.WARNING);
                                                                            }else{
                                                                                List<HashMap<String, String>> requests = (List<HashMap<String, String>>) document.get("requests");
                                                                                boolean existRequest = false;
                                                                                for (HashMap<String, String> request : requests) {
                                                                                    //load user in senders
                                                                                    String sender = request.get("sender");
                                                                                    if (sender.equals(AuthUtil.getFirebaseAuth().getUid())) {
                                                                                        existRequest = true;
                                                                                        break;
                                                                                    }
                                                                                }
                                                                                if (existRequest) {
                                                                                    WaitDialog.dismiss();
                                                                                    TipDialog.show("You already sent a request!", WaitDialog.TYPE.WARNING);
                                                                                } else {
                                                                                    // send request
                                                                                    Request request = new Request(AuthUtil.getFirebaseAuth().getUid());
                                                                                    db.collection("users").document(searchUID)
                                                                                            .update("requests", FieldValue.arrayUnion(request)).addOnSuccessListener(unused -> {
                                                                                                WaitDialog.dismiss();
                                                                                                TipDialog.show("Request sent!", WaitDialog.TYPE.SUCCESS);
                                                                                            });
                                                                                }
                                                                            }

                                                                        } else {
                                                                            Log.d(TAG, "No such document");
                                                                        }
                                                                    } else {
                                                                        Log.d(TAG, "get failed with ", task.getException());
                                                                    }
                                                                }
                                                            });
                                                    return false;
                                                }).setCustomView(new OnBindView<MessageDialog>(R.layout.custom_search_dialog) {
                                                    @Override
                                                    public void onBind(MessageDialog dialog12, View v12) {
                                                        ImageView userIcon;
                                                        userIcon = v12.findViewById(R.id.img_search_user);
                                                        DrawableCrossFadeFactory factory =
                                                                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
                                                        RequestOptions requestOptions = new RequestOptions()
                                                                .placeholder(R.drawable.ic_logo)
                                                                .error(R.drawable.ic_logo)
                                                                .fallback(R.drawable.ic_logo);
                                                        Glide.with(ContactMenuActivity.this)
                                                                .load(searchPhotoUrl)
                                                                .centerCrop()
                                                                .apply(requestOptions)
                                                                .transform(new CircleCrop())
                                                                .transition(DrawableTransitionOptions.withCrossFade(factory))
                                                                .into(userIcon);

                                                        TextView searchName = v12.findViewById(R.id.search_user_name);
                                                        searchName.setText(searchFullName);
                                                    }
                                                }).show();
                                    }

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            });
                    return false;
                })
                .setTitle("Search user by Email")
                .setMessage("Type in Email you want to search to find your friend")
                .show());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //load friend request
        loadFriendRequest(db);



    }

    private void loadFriendRequest(FirebaseFirestore db) {
        String userID = AuthUtil.getFirebaseAuth().getCurrentUser().getUid();
        ArrayList<User> senders = new ArrayList<>();
        ListView listView = findViewById(R.id.friend_request_list);
        CustomRequestListViewAdapter friendRequestAdapter = new CustomRequestListViewAdapter(ContactMenuActivity.this, senders);
        listView.setAdapter(friendRequestAdapter);
        //get all sender ID in active requests
        db.collection("users").document(userID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<HashMap<String, String>> requests = (List<HashMap<String, String>>) document.get("requests");
                                for (HashMap<String, String> request : requests) {
                                    //load user in senders
                                    String sender = request.get("sender");
                                    db.collection("users").document(sender)
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            senders.add(UserController.transformUser(document));
                                                            friendRequestAdapter.notifyDataSetChanged();
                                                        } else {
                                                            Log.d(TAG, "No such document");
                                                        }
                                                    } else {
                                                        Log.d(TAG, "get failed with ", task.getException());
                                                    }
                                                }
                                            });
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

    }
}