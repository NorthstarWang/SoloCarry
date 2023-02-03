package com.example.solocarry.util;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthUtil {
    public static FirebaseAuth mAuth;
    public static FirebaseUser currentUser;

    public AuthUtil() {
        setmAuth(FirebaseAuth.getInstance());
    }

    public FirebaseAuth getmAuth() {
        return AuthUtil.mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        AuthUtil.mAuth = mAuth;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(FirebaseUser currentUser) {
        AuthUtil.currentUser = currentUser;
    }

    public boolean isUserNonNull(){
        //Check if user is signed in (non-null)
        return currentUser!=null;
    }
}
