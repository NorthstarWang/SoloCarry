package com.example.solocarry.controller;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.solocarry.model.Chat;
import com.example.solocarry.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class ChatController {

    public ChatController() {}

    public static void addChat(Chat chat) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String cid = chat.getOwnerID();
        db.collection("chats").document(cid)
                .set(chat)
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

    public static void deleteChat(Chat chat) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String cid = chat.getOwnerID();

        db.collection("chats").document(cid)
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

    // since the only modifiable field in chat class is message, the function has been
    // integrated into the Chat Class, direct set the modified chat to replace previous document.
    public static void updateChat(Chat chat) {
        addChat(chat);
    }

    public static DocumentReference getChat(User userOne, User userTwo) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String cid = userOne.getUid() + " " + userTwo.getUid();
        return db.collection("chats").document(cid);
    }
}
