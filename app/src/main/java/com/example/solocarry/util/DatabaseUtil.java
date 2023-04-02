package com.example.solocarry.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
/**
 * This DatabaseUtil class is the database utility that access to remote firebase
 */
public class DatabaseUtil {

    private FirebaseFirestore db;
    private FirebaseStorage storage;

    /**
     * This getFirebaseFirestoreInstance method returns an instance of firebase firestore database
     * @return FirebaseFirestore
     */
    public static FirebaseFirestore getFirebaseFirestoreInstance(){
        return FirebaseFirestore.getInstance();
    }

    /**
     * This getFirebaseStorageInstance method returns an instance of FirebaseStorage
     */
    public static FirebaseStorage getFirebaseStorageInstance(){
        return FirebaseStorage.getInstance();
    }
}
