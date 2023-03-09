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

public class CodeController {

    public CodeController() {}

    public static void addCode(Code code) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int codeHash = code.getHashCode();

        db.collection("codes").document(Integer.toString(codeHash))
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

    public static void deleteCode(Code code) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int codeHash = code.getHashCode();

        db.collection("codes").document(Integer.toString(codeHash))
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

    // since the app is no large-scale, the update part has been integrated in the model, everytime
    // an update happens, create a new document to replace the old one.
    public static void updateCode(Code code) {
        addCode(code);
    }

    public static DocumentReference getCode(int codeHash) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("codes").document(Integer.toString(codeHash));
    }
}