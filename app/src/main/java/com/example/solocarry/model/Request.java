package com.example.solocarry.model;

import java.sql.Timestamp;
/**
 * This is Request class that models an adding friend request, the Request object
 * is associates with the sender..
 */
public class Request {
    private String senderID;

    /**
     * The constructor of Request object,
     * it accepts one string as input.
     * @param sender the string represents the sender ID
     */
    public Request(String sender) {
        this.senderID = sender;
    }

    /**
     * The getSender method gets the sender of a specific Request object.
     * @return senderID the string represents the sender ID
     */
    public String getSender() {
        return senderID;
    }

    /**
     * The setSender method sets a given sender of a specific Request object.
     * @param sender the string represents the sender ID
     */
    public void setSender(String sender) {
        this.senderID = sender;
    }
}
