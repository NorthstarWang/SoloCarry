package com.example.solocarry.controller;

import static android.content.ContentValues.TAG;

import android.database.DatabaseUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.solocarry.model.Chat;
import com.example.solocarry.model.Code;
import com.example.solocarry.model.User;
import com.example.solocarry.util.DatabaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FriendController {

    public static User transformFirebaseUser(FirebaseUser firebaseUser) {
        return new User(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getUid(), firebaseUser.getPhotoUrl().toString(), 0);
    }

    public static void addFriend(User user) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = user.getUid();
        getUser(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());
                    db.collection("users").document(uid)
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                }else{
                    db.collection("users").document(uid)
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                }
            }
        });
    }

    public static void addFriend(FirebaseUser firebaseUser){
        User user = transformFirebaseUser(firebaseUser);
        addFriend(user);
    }

    public static void deleteUser(User user) {
        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();
        String uid = user.getUid();
        db.collection("user").document(uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public static void updateFriend(User user) {
        addFriend(user);
    }

    public static DocumentReference getFriend(String uid) {
        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();
        return db.collection("users").document(uid);
    }
}
