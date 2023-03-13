package com.example.solocarry.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class DatabaseUtil {

    private FirebaseFirestore db;
    private FirebaseStorage storage;

    public static FirebaseFirestore getFirebaseFirestoreInstance(){
        return FirebaseFirestore.getInstance();
    }

    public static FirebaseStorage getFirebaseStorageInstance(){
        return FirebaseStorage.getInstance();
    }
}
