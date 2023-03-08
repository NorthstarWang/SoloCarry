package com.example.solocarry.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseUtil {

    private FirebaseFirestore db;

    public static FirebaseFirestore getFirebaseFirestoreInstance(){
        return FirebaseFirestore.getInstance();
    }
}
