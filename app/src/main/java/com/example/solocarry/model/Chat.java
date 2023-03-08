package com.example.solocarry.model;

import android.util.Pair;

import java.util.ArrayList;

public class Chat {

    private ArrayList<Message> messageList = new ArrayList<Message>();
    private String uidOne;
    private String uidTwo;    // the owners' uids

    public Chat() {
        uidOne = null;
        uidTwo = null;
    }

    public Chat(String uidOne, String uidTwo) {
        this.uidOne = uidOne;
        this.uidTwo = uidTwo;
    }

    public void addMessage(Message message) {
        messageList.add(message);
    }

    public ArrayList<Message> getMessageList() {
        return messageList;
    }

    public void deleteMessage(Message message) {
        messageList.remove(message);
    }

    public String getUidOne() {return uidOne;}
    public String getUidTwo() {return uidTwo;}

    public String getOwnerID() {return uidOne + " " + uidTwo;}
}
