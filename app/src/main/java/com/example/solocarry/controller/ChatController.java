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

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addChat(Chat chat) {
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

    public void deleteChat(Chat chat) {

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
    public void updateChat(Chat chat) {
        addChat(chat);
    }

    public Chat getChat(User userOne, User userTwo) {

        String cid = userOne.getUid() + " " + userTwo.getUid();
        ArrayList<Chat> chatList = new ArrayList<> ();
        DocumentReference docRef = db.collection("chats").document(cid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Chat chatReconstruct = documentSnapshot.toObject(Chat.class);
                chatList.add(chatReconstruct);
            }
        });
        return chatList.get(0);
    }
}
