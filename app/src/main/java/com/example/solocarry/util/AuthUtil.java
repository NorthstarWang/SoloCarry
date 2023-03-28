package com.example.solocarry.util;

import com.example.solocarry.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthUtil {
    public static FirebaseAuth mAuth;
    public static FirebaseUser currentUser;
    private User user;

    public AuthUtil() {
        setmAuth(FirebaseAuth.getInstance());
        if (isSignedIn()){
            //if user signed in, set currentUser
            setCurrentUser(getmAuth().getCurrentUser());
            user = new User(getCurrentUser().getDisplayName(),getCurrentUser().getEmail(),getCurrentUser().getUid(),getCurrentUser().getPhotoUrl()!=null?getCurrentUser().getPhotoUrl().toString():"https://lh3.googleusercontent.com/a/AGNmyxZ1g_vU0VIZVvKSPRIfF_A1W_hZ_UAroLlv4di6lg=s96-c", 0);
        }
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
    
    public FirebaseUser getAuthCurrentUser(){
        return getmAuth().getCurrentUser();
    }

    public static boolean isSignedIn(){
        return FirebaseAuth.getInstance().getCurrentUser()!=null;
    }

    public void setCurrentUser(FirebaseUser currentUser) {
        AuthUtil.currentUser = currentUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static void SignOut(){
        if (isSignedIn()){
            FirebaseAuth.getInstance().signOut();
        }
    }

    public static FirebaseAuth getFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }
}
