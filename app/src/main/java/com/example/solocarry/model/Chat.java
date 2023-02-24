package com.example.solocarry.model;

import android.util.Pair;

import java.util.ArrayList;

public class Chat {

    private ArrayList<Message> messageList = new ArrayList<Message>();
    private final String uidOne;
    private final String uidTwo;    // the owners' uids

    public Chat(String uidOne, String uidTwo) {
        this.uidOne = uidOne;
        this.uidTwo = uidTwo;
    }

    public void addMessage(Message message) {
        messageList.add(message);
    }

    public ArrayList<Message> showMessages() {
        return messageList;
    }

    public void deleteMessage(Message message) {
        messageList.remove(message);
    }

    public ArrayList<String> getOwnerID() {
        ArrayList<String> uidPair = new ArrayList<>();
        uidPair.add(uidOne);
        uidPair.add(uidTwo);
        return uidPair;
    }
}
