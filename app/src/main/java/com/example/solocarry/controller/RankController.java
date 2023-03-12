package com.example.solocarry.controller;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.solocarry.model.Code;
import com.example.solocarry.model.Invitation;
import com.example.solocarry.model.Rank;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This is rank controller class which connects Rank model class with Firestore database,
 * the RankController class is an intermediate to execute adding, deleting, updating,
 * and getting operations of the Rank objects from the Firestore. Noticeably, each region
 * in the world will associate with one rank object.
 */
public class RankController {

    public RankController() {}

    /**
     * This setRank method sets a Rank object into the Firestore "Rank"
     * collection, it first create a reference to the database, and then queries the
     * Firestore database through the combination of rankName  and the passed in rank
     * object. The method set this rank object in the document titled by given rankName,
     * the method soly execute the adding operation and returns nothing.
     * @param rankName
     * @param rank
     */
    public static void setRank(String rankName, Rank rank) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("rankings").document(rankName)
                .set(rank)
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
     *  This deleteRank method deletes an invitation object from the Firestore
     *  "Invitation" collection, it first asks user to provide a rankName,
     *  then using the passed in rankName, the method can execute the deletion
     *  operation to delete the corresponding rank object in the Firebase.
     * @param rankName
     */
    public static void deleteRank(String rankName) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("rankings").document(rankName)
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

    public static void realTimeRefresh() {}

    /**
     * This getRank method gets a code object from the Firestore "Rank" collection,
     * if first asks user to provide the rankName of one particular Rank object,
     * then it uses this rankName to query the Firestore, and then the Firestore will
     * return a document reference indicates the document that stored corresponding Rank object.
     * @param rankName
     * @return DocumentReference
     */
    public static DocumentReference getRank(String rankName) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("rankings").document(rankName);
    }
}
