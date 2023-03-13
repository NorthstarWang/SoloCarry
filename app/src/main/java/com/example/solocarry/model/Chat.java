package com.example.solocarry.model;

import android.util.Pair;

import java.util.ArrayList;

/**
 * This is Chat class that models the behaviour of a Chat dialogue, the chat
 * can be held between two users and each chat is uniquely identified by
 * the combination of two users' id. The Chat object also contains an
 * ArrayList of Message objects to represent chat messages.
 */
public class Chat {

    private ArrayList<Message> messageList = new ArrayList<Message>();
    private String uidOne;
    private String uidTwo;    // the owners' uids

    public Chat() {
        uidOne = null;
        uidTwo = null;
    }

    /**
     * The constructor of a chat object,it accepts two user ids and constructs
     * a specific Chat associated with these two users.
     * @param uidOne the user ID of the first User
     * @param uidTwo the user ID of the second User
     */
    public Chat(String uidOne, String uidTwo) {
        this.uidOne = uidOne;
        this.uidTwo = uidTwo;
    }

    /**
     * The addMessage method adds a given message into a specific Chat object,
     * it accepts one Message as input.
     * @param message the message object to be added
     */
    public void addMessage(Message message) {
        messageList.add(message);
    }

    /**
     * The getMessageList method returns an ArrayList of all chatting messages
     * corresponding to a specific Chat object.
     * @return messageList
     */
    public ArrayList<Message> getMessageList() {
        return messageList;
    }

    /**
     * The deleteMessage method deletes a given message object from a specific
     * Chat object, it accepts one Message object as input.
     * @param message the message object to be deleted
     */
    public void deleteMessage(Message message) {
        messageList.remove(message);
    }

    /**
     * The getUidOne method returns the first User of a specific Chat object.
     * @return uidOne
     */
    public String getUidOne() {return uidOne;}

    /**
     * The getUidTwo method returns the second User of a specific Chat object.
     * @return uidTwo
     */
    public String getUidTwo() {return uidTwo;}

    /**
     * The getOwnerID method returns the combination of the user ID of two users
     * associated with a specific Chat object.
     * @return uidTwo
     */
    public String getOwnerID() {return uidOne + " " + uidTwo;}
}
