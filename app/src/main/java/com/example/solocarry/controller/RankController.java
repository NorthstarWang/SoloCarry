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

public class RankController {

    private FirebaseFirestore db;

    public RankController() {db = FirebaseFirestore.getInstance();}

    public void setRank(String rankName, Rank rank) {

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

    public void deleteRank(String rankName) {

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

    public void realTimeRefresh() {}

    public Rank getRank(String rankName) {

        ArrayList<Rank> rankList = new ArrayList<> ();
        DocumentReference docRef = db.collection("rankings").document(rankName);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Rank rankReconstruct = documentSnapshot.toObject(Rank.class);
                rankList.add(rankReconstruct);
            }
        });
        return rankList.get(0);
    }


}
