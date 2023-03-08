package com.example.solocarry.model;

import java.util.ArrayList;
import java.util.Collections;

public class Rank {
    private ArrayList<User> userArrayList;

    public ArrayList<User> getUserArrayList() {
        return userArrayList;
    }

    public void setUserArrayList(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;
    }

    public void sortUserArrayList(){
        if(this.userArrayList!=null){
            Collections.sort(this.userArrayList);
        }
    }
}
