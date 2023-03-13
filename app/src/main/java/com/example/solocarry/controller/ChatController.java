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

/**
 * This is a class which connects Chat model with Firestore database, the class
 * is an intermediate to execute adding, deleting, updating, and getting operations
 * of Chat objects from the Firestore
 */
public class ChatController {

    public ChatController() {}

    /**
     * This method adds a chat object into the Firestore "Chat" collection, it first
     * create a reference to the database, and then the "chat" object will be stored into
     * the database
     * @param chat a chat should be added
     */
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

    /**
     * This deleteChat method deletes a chat object from the Firestore "Chat" collection, it first
     * asks user to provide a Chat object, then it extraccts its chat id, and uses this id to
     * execute the deletion operation.
     * @param chat a chat should be deleted
     */
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

    /**
     * This updateChat method updates a chat object in the Firestore "Chat" collection, it first
     * asks user to provide a Chat object, then it uses the addChat method above to
     * replace the existing Chat object stored in the Firestore "Chat" collection.
     * @param chat a chat should be updated
     */
    // since the only modifiable field in chat class is message, the function has been
    // integrated into the Chat Class, direct set the modified chat to replace previous document.
    public static void updateChat(Chat chat) {
        addChat(chat);
    }

    /**
     * This getChat method gets a chat object from the Firestore "Chat" collection,
     * if first asks user to provide two users, userOne and userTwo, then it combines two users'
     * name as a chat id to query the Firestore, and then the Firestore will return a document
     * reference indicates the document that stored the chat object in cloud database.
     * @param userOne a User should be provided to query the Firestore a particular document
     * @param userTwo a User should be provided to query the Firestore a particular document
     * @return DocumentReference
     */
    public static DocumentReference getChat(User userOne, User userTwo) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String cid = userOne.getUid() + " " + userTwo.getUid();
        return db.collection("chats").document(cid);
    }
}
