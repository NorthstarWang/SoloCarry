package com.example.solocarry.model;

import android.net.Uri;

import java.util.ArrayList;

public class User implements Comparable<User> {
    private String name;
    private String email;
    private String uid;
    private String photoUrl;
    private int score;
    private ArrayList<Request> requests;
    private ArrayList<String> friends;

    public User(String name, String email, String uid, String photoUrl, int score) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.photoUrl = photoUrl;
        this.score = score;
        if(requests==null){
            requests = new ArrayList<>();
        }
        if(friends==null){
            friends = new ArrayList<>();
        }
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public void addRequests(Request request) {
        this.requests.add(request);
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void addFriends(String friend) {
        this.friends.add(friend);
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        setScore(getScore() + score);
    }

    public void minusScore(int score) {
        setScore(getScore() - score);
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(User user) {
        return Integer.compare(score, user.getScore());
    }
}
