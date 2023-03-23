package com.example.solocarry.controller;

import com.example.solocarry.model.Request;
import com.example.solocarry.model.User;
import com.example.solocarry.util.DatabaseUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This is a class which connects User model with Firestore database, the UserController
 * class is an intermediate to execute adding, deleting, updating, and getting operations
 * of User objects from the Firestore. Noticeably, the authentication process passes a
 * firebaseUser class, so we have to convert it into the User class firstly
 */
public class UserController {

    public static User transformFirebaseUser(FirebaseUser firebaseUser) {
        return new User(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getUid(), firebaseUser.getPhotoUrl().toString(), 0);
    }

    /**
     * This method adds a user object into the Firestore "User" collection, it first
     * create a reference to the database, and then queries the Firestore database whether
     * the user given existed already, if Yes, the method replace the old user object with
     * the new one, if no, the method will directly store the given user object in the database
     * @param user the user object we want to add
     */
    public static void addUser(User user, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        getUser(user.getUid(), new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();
                if(documentSnapshot.exists()){
                    User tempUser = transformUser(documentSnapshot);
                    db.collection("users").document(user.getUid()).set(tempUser).addOnSuccessListener(successListener);
                }else{
                    db.collection("users").document(user.getUid()).set(user).addOnSuccessListener(successListener);
                }
            }
        }, failureListener);
    }

    /**
     * This method is a pre-step before the adding process, since the given firebaseUser
     * object should be converted into the User class before adding to the Firestore,
     * so firstly, use transformFirebaseUser method to do the convertion. And then use
     * the above addUser method to execute the addition process.
     * @param firebaseUser the given firebaseUser object that should be converted to User object
     */
    public static void createUser(FirebaseUser firebaseUser, OnSuccessListener<Void> successListener, OnFailureListener failureListener){
        User user = transformFirebaseUser(firebaseUser);
        addUser(user, successListener, failureListener);
    }

    /**
     *  This updateUser method updates a user object from the Firestore "User" collection, it first
     *  asks user to provide a User object, then it calls the addUser method in above,
     *  to directly replace the matched User object in Firestore. Two user objects are matched if
     *  they share the same user id.
     * @param user the user object we want to update
     */
    public static void updateUser(User user, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();
        db.collection("users").document(user.getUid()).set(user).addOnSuccessListener(successListener).addOnFailureListener(failureListener);
    }

    /**
     * This getUser method gets a user object reference from the Firestore "User" collection,
     * if first asks user to provide the User id of one particular user,
     * then it uses this user id to query the Firestore, and then the Firestore will
     * return a document reference indicates the document that stored the user object.
     * @param uid the user id object we want to get from Firebase
     * @return DocumentReference
     */
    public static void getUser(String uid, OnSuccessListener<DocumentSnapshot> successListener, OnFailureListener failureListener) {
        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();
        db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> successListener.onSuccess(documentSnapshot))
                .addOnFailureListener(e -> {
                    if(failureListener!=null){
                        failureListener.onFailure(e);
                    }
                });
    }

    public static void addScoreToUser(String uid, int score, OnSuccessListener<Void> successListener, OnFailureListener failureListener){
        getUser(uid, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = transformUser(documentSnapshot);
                user.addScore(score);
                updateUser(user, successListener, failureListener);
            }
        }, null);
    }

    /**
     * This transformUser method transforms a document reference from the Firestore "User"
     * collection, the method first gets all fields of a particular document to rebuild
     * the User class
     * @param document the document reference points to a Firestore document
     * @return User the reconstructed User object from Firestore document
     */
    public static User transformUser(DocumentSnapshot document){
        User user = new User((String)document.get("name"),(String)document.get("email"),(String)document.get("uid"),(String)document.get("photoUrl"), ((Long) document.get("score")).intValue());
        List<HashMap<String, String>> requests = (List<HashMap<String, String>>) document.get("requests");
        for (HashMap<String, String> request : requests) {
            //load user in senders
            String sender = request.get("sender");
            user.addRequests(new Request(sender));
        }

        List<String> friends = (List<String>) document.get("friends");
        for (String friend:friends) {
            user.addFriends(friend);
        }
        return user;
    }

    public static void loadFriend(String uid, OnSuccessListener<DocumentSnapshot> successListener){
        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();
        getUser(uid, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    successListener.onSuccess(documentSnapshot);
                }
            }
        }, null);
    }
}
