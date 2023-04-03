package com.example.solocarry.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This is Rank class that models the rank list. Each Rank contains am
 * ArrayList of users, ordered from high personal score to low personal score.
 */
public class Rank {
    private ArrayList<User> userArrayList;

    /**
     * The getUserArrayList method returns the userArrayList of a specific Rank object.
     * @return userArrayList
     */
    public ArrayList<User> getUserArrayList() {
        return userArrayList;
    }

    /**
     * The setUserArrayList method sets the userArrayList of a specific Rank object.
     * @param userArrayList the new userArrayList we want to set
     */
    public void setUserArrayList(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;
    }

    /**
     * The sortUserArrayList method sorts the userArrayList of a specific Rank object.
     */
    public void sortUserArrayList(){
        if(userArrayList!=null){
            Collections.sort(userArrayList);
        }
    }
}
