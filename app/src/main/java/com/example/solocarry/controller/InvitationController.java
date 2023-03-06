package com.example.solocarry.controller;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.solocarry.model.Invitation;
import com.example.solocarry.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class InvitationController {

    public InvitationController() {}

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addInvitation(Invitation inv) {
        String invID = inv.getInvID();
        db.collection("invitation").document(invID)
                .set(inv)
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

    public void deleteInvitation(Invitation inv) {

        String invID = inv.getInvID();

        db.collection("invitation").document(invID)
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

    public void updateInvitation(Invitation inv) {addInvitation(inv);}

    public Invitation getInvitation(User userOne, User userTwo) {

        String invID = userOne.getUid() + " " + userTwo.getUid();
        ArrayList<Invitation> invList = new ArrayList<> ();
        DocumentReference docRef = db.collection("invitation").document(invID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Invitation invReconstruct = documentSnapshot.toObject(Invitation.class);
                invList.add(invReconstruct);
            }
        });
        return invList.get(0);
    }
}
