package com.example.solocarry.controller;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.solocarry.model.Chat;
import com.example.solocarry.model.Code;
import com.example.solocarry.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
     * @param code the Code to be added
     * @param uid the User ID of code Owner
     */
    public static void addCode(Code code, String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String codeHash = code.getHashCode();

        db.collection("codes").document(uid+codeHash)
                .set(code)
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
     *  This deleteCode method deletes a code object from the Firestore "Code" collection, it first
     *  asks user to provide a Code object, then it extracts the hash code of the given
     *  Code object, Using the extracted Hash code, the method can execute the deletion
     *  operation in the Firebase. Noticeably, once a code disappears, it also disappears
     *  from all users who own it.
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
     *  This updateUser method updates a user object from the Firestore "User" collection, it first
     *  asks user to provide a User object, then it calls the addUser method in above,
     *  to directly replace the matched User object in Firestore. Two user objects are matched if
     *  they share the same user id.
     * @param code the Code to be updated
     * @param uid the User id of code owner
     */
    public static void updateCode(Code code, String uid) {
        addCode(code, uid);
    }

    /**
     * This getCode method gets a code object from the Firestore "User" collection,
     * if first asks user to provide the hash code of one particular treasure code,
     * then it uses this hash code to query the Firestore, and then the Firestore will
     * return a document reference indicates the document that stored the code object.
     * @param codeHash the codeHash is an identifier of code
     * @return DocumentReference
     */
    public static DocumentReference getCode(int codeHash) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("codes").document(Integer.toString(codeHash));
    }
}