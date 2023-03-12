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

/**
 * This is invitation controller class which connects Invitation model class with Firestore database,
 * the InvitationController class is an intermediate to execute adding, deleting, updating,
 * and getting operations of the Invitation objects from the Firestore.
 */
public class InvitationController {

    public InvitationController() {}

    /**
     * This addInvitation method adds an invitation object into the Firestore "Invitation"
     * collection, it first create a reference to the database, and then queries the
     * Firestore database through invitation ID extracted from the passed in object,
     * the method only execute the adding operation and returns nothing.
     * @param inv
     */
    public static void addInvitation(Invitation inv) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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

    /**
     *  This deleteInvitation method deletes an invitation object from the Firestore
     *  "Invitation" collection, it first asks user to provide an Invitation object,
     *  then it extracts the invitation ID of the given Invitation object, Using
     *  the extracted invitation ID, the method can execute the deletion
     *  operation in the Firebase.
     * @param inv
     */
    public static void deleteInvitation(Invitation inv) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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

    /**
     *  This updateInvitation method updates an Invitation object from the Firestore "Invitation"
     *  collection, it first asks user to provide an Invitation object, then it
     *  calls the addInvitation method in above to directly replace the matched
     *  Invitation object in Firestore. Two Invitation objects are matched if
     *  they share the same invitation id.
     * @param inv
     */
    public static void updateInvitation(Invitation inv) {addInvitation(inv);}

    /**
     * This getInvitation method gets a code object from the Firestore "User" collection,
     * if first asks user to provide the hash code of one particular treasure code,
     * then it uses this hash code to query the Firestore, and then the Firestore will
     * return a document reference indicates the document that stored the code object.
     * @param userOne
     * @param userTwo
     * @return DocumentReference
     */
    public static DocumentReference getInvitation(User userOne, User userTwo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String invID = userOne.getUid() + " " + userTwo.getUid();
        return db.collection("invitation").document(invID);
    }
}
