package com.example.solocarry.controller;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.solocarry.model.Code;
import com.example.solocarry.model.CodeInMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This is controller class which connects Code model class with Firestore database,
 * the CodeController class is an intermediate to execute adding, deleting, updating,
 * and getting operations of User objects from the Firestore.
 */
public class CodeController {

    public CodeController() {}

    /**
     * This addCode method adds a code object into the Firestore "Code" collection, it first
     * create a reference to the database, and then queries the Firestore database,
     * the method uses the unique combination of User id and the code's hashcode.
     *
     * @param code the Code to be added
     * @param uid  the User ID of code Owner
     */
    public static void addCode(Code code, String uid, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String codeHash = code.getHashCode();

        db.collection("codes").document(uid + codeHash)
                .set(code)
                .addOnSuccessListener(aVoid -> successListener.onSuccess(aVoid))
                .addOnFailureListener(e -> {
                    if (failureListener != null) {
                        failureListener.onFailure(e);
                    }
                });
    }

    /**
     * This getCodeFromCodeMap method gets a code object from the Firestore "CodeMap" collection
     * @param code the code object we want to derive
     * @param successListener a success Listener
     * @param failureListener a failure listener
     */
    public static void getCodeFromCodeMap(Code code, OnSuccessListener<DocumentSnapshot> successListener, OnFailureListener failureListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("codeMap").document(code.getHashCode()).get()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }


    /**
     * This addCodeToCodeMap method adds a code object to the Firestore "CodeMap" collection
     * @param code the code object we want to add
     * @param successListener a success Listener
     * @param failureListener a failure listener
     * @param uid the user id
     */
    public static void addCodeToCodeMap(Code code, String uid, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        getCodeFromCodeMap(code, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                CodeInMap codeInMap;
                if (documentSnapshot.exists()) {
                    codeInMap = documentSnapshot.toObject(CodeInMap.class);
                } else {
                    codeInMap = new CodeInMap(code.getHashCode(), code.getScore());
                }
                codeInMap.getOwnerIds().add(uid);
                codeInMap.setOwnerIds(codeInMap.getOwnerIds());
                db.collection("codeMap").document(code.getHashCode())
                        .set(codeInMap).addOnSuccessListener(successListener)
                        .addOnFailureListener(failureListener);
            }
        }, e -> {

        });
    }

    /**
     * This deleteCode method deletes a code object from the Firestore "Code" collection, it first
     * asks user to provide a Code object, then it extracts the hash code of the given
     * Code object, Using the extracted Hash code, the method can execute the deletion
     * operation in the Firebase. Noticeably, once a code disappears, it also disappears
     * from all users who own it.
     *
     * @param code the Code to be deleted
     */
    public static void deleteCode(Code code) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String codeHash = code.getHashCode();

        db.collection("codes").document(codeHash)
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
     * This updateCode method uses addCode() method to add a new code
     * @param code the Code to be updated
     * @param uid  the User id of code owner
     * @param successListener a success Listener
     * @param failureListener a failure listener
     */
    public static void updateCode(Code code, String uid, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        addCode(code, uid, successListener, failureListener);
    }

    /**
     * This getPublicCode method derives all public codes of the database
     * @param successListener the success listener of querySnapshot
     */
    public static void getPublicCode(OnSuccessListener<QuerySnapshot> successListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("codes")
                .whereEqualTo("showPublic", true)
                .get().addOnSuccessListener(successListener);
    }

    /**
     * This getUserPublicCode method derives all public codes of a particular user
     * @param uid  the User id of code owner
     * @param successListener a success Listener
     */
    public static void getUserPublicCode(String uid, OnSuccessListener<QuerySnapshot> successListener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid)
                .collection("codes")
                .whereEqualTo("showPublic", true)
                .get().addOnSuccessListener(successListener);
    }

    /**
     * This listenToPublicCodeUpload method is listener listens all uploading public code operations
     * @param querySnapshotEventListener a Event Listener
     */
    public static void listenToPublicCodeUpload(EventListener<QuerySnapshot> querySnapshotEventListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("codes").whereEqualTo("showPublic", true)
                .addSnapshotListener(querySnapshotEventListener);
    }

    /**
     * This getUserPublicCode method derives all public codes of a particular user
     * @param uid  the User id of code owner
     * @param successListener a success Listener
     */
    public static void checkUserHaveSuchCode(String uid, String SHA256, OnSuccessListener<DocumentSnapshot> successListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid)
                .collection("codes").document(SHA256)
                .get().addOnSuccessListener(successListener);
    }

    /**
     * This addCodeToUser method add a given code object to a particular user
     * @param uid  the User id of code owner
     * @param successListener a success Listener
     * @param code the given code object
     */
    public static void addCodeToUser(String uid, Code code, OnSuccessListener<Void> successListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid)
                .collection("codes").document(code.getHashCode())
                .set(code).addOnSuccessListener(successListener);
    }

    /**
     * This getExtreme method derives a extreme score code of a particular user
     * @param uid  the User id of code owner
     * @param eventListener a event Listener
     * @param dir the ranking direction
     */
    public static void getExtreme(String uid, Query.Direction dir, EventListener<QuerySnapshot> eventListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).collection("codes").orderBy("score", dir).limit(1).addSnapshotListener(eventListener);
    }

    /**
     * This getCodeCount method get the number of owned codes of a given user
     * @param uid  the User id of code owner
     * @param successListener a success Listener
     */
    public static void getCodeCount(String uid, OnSuccessListener<AggregateQuerySnapshot> successListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).collection("codes").count().get(AggregateSource.SERVER)
                .addOnSuccessListener(successListener);
    }

    /**
     * This getHighestCodeRank method derives the highest score code rank
     * @param successListener a success Listener
     */
    public static void getHighestCodeRank(OnSuccessListener<QuerySnapshot> successListener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("codeMap").orderBy("score", Query.Direction.DESCENDING).get().addOnSuccessListener(successListener);
    }
}