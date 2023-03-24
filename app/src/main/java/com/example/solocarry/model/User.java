package com.example.solocarry.model;

import android.net.Uri;

import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * This is User class that models the User. Each User contains an
 * ArrayList of requests and an ArrayList of friends, it also contains
 * name, email, uid, photoUrl, score.
 */
public class User implements Comparable<User> {
    private String name;
    private String email;
    private String uid;
    private String photoUrl;
    private int score;
    private ArrayList<Request> requests;
    private ArrayList<String> friends;

    /**
     * The constructor of a User object. It accepts a name, an email address
     * , an User id, an photo, and a current score, also initializes friends
     * and requests if not exist.
     * @param name the name of the User
     * @param email the email address of the User
     * @param uid the user id of the User
     * @param photoUrl the photo of the User
     * @param score the score of the User
     */
    public User(String name, String email, String uid, @Nullable String photoUrl, int score) {
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

    /**
     * The getRequests method returns an ArrayList of adding friend
     * requests of a specific User object.
     * @return request
     */
    public ArrayList<Request> getRequests() {
        return requests;
    }

    /**
     * The addRequests method adds a new request to the request
     * ArrayList of a specific User object.
     * @param request the new request to be added in request arraylist
     */
    public void addRequests(Request request) {
        this.requests.add(request);
    }

    /**
     * The getFriends method returns an ArrayList of current friends
     * requests of a specific User object.
     * @return friends
     */
    public ArrayList<String> getFriends() {
        return friends;
    }

    /**
     * The addFriends method adds a new friend to the friends
     * ArrayList of a specific User object.
     * @param friend the new friend to be added in request arraylist
     */
    public void addFriends(String friend) {
        this.friends.add(friend);
    }

    /**
     * The getName method returns the name of a specific User object.
     * @return name
     */
     public String getName() {
        return name;
    }

    /**
     * The setName method sets the name of a specific User object.
     * @param name the name we want to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The getEmail method returns the email of a specific User object.
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * The setEmail method sets the email of a specific User object.
     * @param email the email we want to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * The getUid method returns the user id of a specific User object.
     * @return uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * The setUid method sets the user id of a specific User object.
     * @param uid the uid we want to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * The getPhotoUrl method returns the Url of a specific User object.
     * @return photoUrl
     */
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     * The setPhotoUrl method sets the photoUrl of a specific User object.
     * @param photoUrl the photo we want to set
     */
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    /**
     * The getScore method returns the score of a specific User object.
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     * The addScore method increases the score of a specific User object.
     * @param score the score we want to increase
     */
    public void addScore(int score) {
        setScore(getScore() + score);
    }

    /**
     * The minusScore method decreases the score of a specific User object.
     * @param score the score we want to decrease
     */
    public void minusScore(int score) {
        setScore(getScore() - score);
    }

    /**
     * The setScore method sets the score of a specific User object.
     * @param score the score we want to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * The compareTo method compares the scores of two specific User objects.
     * @param user the another user we want to compare
     * @return int
     */
    @Override
    public int compareTo(User user) {
        return Integer.compare(score, user.getScore());
    }
}
