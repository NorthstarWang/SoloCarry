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

    public static void getCodeFromCodeMap(Code code, OnSuccessListener<DocumentSnapshot> successListener, OnFailureListener failureListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("codeMap").document(code.getHashCode()).get()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

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
     * This updateUser method updates a user object from the Firestore "User" collection, it first
     * asks user to provide a User object, then it calls the addUser method in above,
     * to directly replace the matched User object in Firestore. Two user objects are matched if
     * they share the same user id.
     *
     * @param code the Code to be updated
     * @param uid  the User id of code owner
     */
    public static void updateCode(Code code, String uid, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        addCode(code, uid, successListener, failureListener);
    }

    public static void getPublicCode(OnSuccessListener<QuerySnapshot> successListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("codes")
                .whereEqualTo("showPublic", true)
                .get().addOnSuccessListener(successListener);
    }

    public static void getUserPublicCode(String uid, OnSuccessListener<QuerySnapshot> successListener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid)
                .collection("codes")
                .whereEqualTo("showPublic", true)
                .get().addOnSuccessListener(successListener);
    }

    public static void listenToPublicCodeUpload(EventListener<QuerySnapshot> querySnapshotEventListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("codes").whereEqualTo("showPublic", true)
                .addSnapshotListener(querySnapshotEventListener);
    }

    public static void checkUserHaveSuchCode(String uid, String SHA256, OnSuccessListener<DocumentSnapshot> successListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid)
                .collection("codes").document(SHA256)
                .get().addOnSuccessListener(successListener);
    }

    public static void addCodeToUser(String uid, Code code, OnSuccessListener<Void> successListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid)
                .collection("codes").document(code.getHashCode())
                .set(code).addOnSuccessListener(successListener);
    }

    public static void getExtreme(String uid, Query.Direction dir, EventListener<QuerySnapshot> eventListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).collection("codes").orderBy("score", dir).limit(1).addSnapshotListener(eventListener);
    }

    public static void getCodeCount(String uid, OnSuccessListener<AggregateQuerySnapshot> successListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).collection("codes").count().get(AggregateSource.SERVER)
                .addOnSuccessListener(successListener);
    }

    public static void getHighestCodeRank(OnSuccessListener<QuerySnapshot> successListener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("codeMap").orderBy("score", Query.Direction.DESCENDING).get().addOnSuccessListener(successListener);
    }
}