package com.example.solocarry.model;

/**
 * This is User class that models the behaviour of a User. Each User is associated with
 * a name, an email address, an User id, an photo, and a current score.
 */
public class User implements Comparable<User> {
    private String name;
    private String email;
    private String uid;
    private String photoUrl;
    private int score;

    public User() {}

    /**
     * The constructor of a User object. It accepts a name, an email address
     * , an User id, an photo, and a current score
     * @param name the name of the User
     * @param email the email address of the User
     * @param uid the user id of the User
     * @param photoUrl the photo of the User
     * @param score the score of the User
     */
    public User(String name, String email, String uid, String photoUrl, int score) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.photoUrl = photoUrl;
        this.score = score;
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
     * The compareTo method compares the two user objects by their score. Implement
     * this logic is required in ranking function.
     * @param user the another User we want to compare to
     * @return int
     */
    @Override
    public int compareTo(User user) {
        return Integer.compare(score, user.getScore());
    }
}
