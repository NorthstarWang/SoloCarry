package com.example.solocarry.controller;
import static android.content.ContentValues.TAG;
import android.database.DatabaseUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.solocarry.model.Chat;
import com.example.solocarry.model.Code;
import com.example.solocarry.model.Request;
import com.example.solocarry.model.User;
import com.example.solocarry.util.AuthUtil;
import com.example.solocarry.util.DatabaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.Document;

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
    public static void addUser(User user) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = user.getUid();
        getUser(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()) {
                    db.collection("users").document(uid)
                            .set(user)
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
            }
        });
    }

    /**
     * This method is a pre-step before the adding process, since the given firebaseUser
     * object should be converted into the User class before adding to the Firestore,
     * so firstly, use transformFirebaseUser method to do the convertion. And then use
     * the above addUser method to execute the addition process.
     * @param firebaseUser the given firebaseUser object that should be converted to User object
     */
    public static void addUser(FirebaseUser firebaseUser){
        User user = transformFirebaseUser(firebaseUser);
        addUser(user);
    }

    /**
     *  This deleteUser method deletes a user object from the Firestore "User" collection, it first
     *  asks user to provide a User object, then it extracts the User id of given User object,
     *  Using the extracted User id, the method can execute the deletion operation in the
     *  Firebase.
     * @param user the user object we want to delete
     */
    public static void deleteUser(User user) {
        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();
        String uid = user.getUid();
        db.collection("user").document(uid)
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
     * @param user the user object we want to update
     */
    public static void updateUser(User user) {
        addUser(user);
    }

    /**
     * This getUser method gets a user object reference from the Firestore "User" collection,
     * if first asks user to provide the User id of one particular user,
     * then it uses this user id to query the Firestore, and then the Firestore will
     * return a document reference indicates the document that stored the user object.
     * @param uid the user id object we want to get from Firebase
     * @return DocumentReference
     */
    public static DocumentReference getUser(String uid) {
        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();
        return db.collection("users").document(uid);
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
}
