package com.example.solocarry.model;

import android.net.Uri;

public class User implements Comparable<User> {
    private String name;
    private String email;
    private String uid;
    private Uri photoUrl;
    private int score;

    public User() {};

    public User(String name, String email, String uid, Uri photoUrl) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(User user) {
        return Integer.compare(score, user.getScore());
    }
}
