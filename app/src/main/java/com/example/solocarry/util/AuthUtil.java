package com.example.solocarry.util;

import com.example.solocarry.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
/**
 * This Auth Util class is the utility to handle authentication
 */
public class AuthUtil {
    public static FirebaseAuth mAuth;
    public static FirebaseUser currentUser;
    private User user;

    /**
     * This AuthUtil() is the constructor of the class
     */
    public AuthUtil() {
        setmAuth(FirebaseAuth.getInstance());
        if (isSignedIn()){
            //if user signed in, set currentUser
            setCurrentUser(getmAuth().getCurrentUser());
            user = new User(getCurrentUser().getDisplayName(),getCurrentUser().getEmail(),getCurrentUser().getUid(),getCurrentUser().getPhotoUrl()!=null?getCurrentUser().getPhotoUrl().toString():"https://lh3.googleusercontent.com/a/AGNmyxZ1g_vU0VIZVvKSPRIfF_A1W_hZ_UAroLlv4di6lg=s96-c", 0);
        }
    }

    /**
     * This getmAuth method returns the firebase mAuth instance
     * @return FirebaseAuth
     */
    public FirebaseAuth getmAuth() {
        return AuthUtil.mAuth;
    }

    /**
     * This setmAuth method returns the score of a CodeInMap object
     * @param mAuth the given FirebaseAuth object
     */
    public void setmAuth(FirebaseAuth mAuth) {
        AuthUtil.mAuth = mAuth;
    }

    /**
     * This getCurrentUser method returns the current user object
     * @return FirebaseUser
     */
    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    /**
     * This getAuthCurrentUser method returns the authorized user object
     * @return FirebaseUser
     */
    public FirebaseUser getAuthCurrentUser(){
        return getmAuth().getCurrentUser();
    }

    /**
     * This isSignedIn method returns the boolean status reflects the sign in status
     * @return boolean
     */
    public static boolean isSignedIn(){
        return FirebaseAuth.getInstance().getCurrentUser()!=null;
    }

    /**
     * This setCurrentUser method sets the current user object
     * @param currentUser the given user object we want to set
     */
    public void setCurrentUser(FirebaseUser currentUser) {
        AuthUtil.currentUser = currentUser;
    }

    /**
     * This getUser method returns the user object
     * @return User
     */
    public User getUser() {
        return user;
    }

    /**
     * This setUser method sets the user object
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * This SignOut method execute the "sign out" operation
     */
    public static void SignOut(){
        if (isSignedIn()){
            FirebaseAuth.getInstance().signOut();
        }
    }

    /**
     * This getFirebaseAuth method returns a firebaseAuth instance
     * @return FirebaseAuth
     */
    public static FirebaseAuth getFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }
}
