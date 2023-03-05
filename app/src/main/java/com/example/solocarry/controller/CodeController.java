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

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addCode(Code code) {
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

    public void deleteCode(Code code) {

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
    public void updateCode(Code code) {
        addCode(code);
    }

    public Code getCode(int codeHash) {

        DocumentReference docRef = db.collection("codes").document(Integer.toString(codeHash));
        ArrayList<Code> codeList = new ArrayList<>();
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Code codeReconstruct = documentSnapshot.toObject(Code.class);
                codeList.add(codeReconstruct);
            }
        });
        return codeList.get(0);
    }
}