package com.example.solocarry.model;

import android.net.Uri;

public class Friend {

    private String name;
    private String email;
    private String uid;
    private Uri photoUrl;
    private int score;
    private User associatedUser;

    public Friend(User associatedUser, String name, String email, String uid, Uri photoUrl, int score) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.score = score;
        this.associatedUser = associatedUser;
    }

    public void updateAssociatedUser(User newUser) {
        this.associatedUser = newUser;
    }

    public User getAssociatedUser(){
        return this.associatedUser;
    }


    public Uri getFriendPhotoUrl() {
        return photoUrl;
    }

    public int getFriendScore() {
        return score;
    }

    public void updateFriendScore(int newVal) {
        this.score = newVal;
    }

    public String getFriendName() {
        return name;
    }

    public void setFriendName(String name){
        this.name = name;
    }


    public String getFriendEmail() {
        return email;
    }


}
