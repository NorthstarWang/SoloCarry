package com.example.solocarry.model;

import java.sql.Timestamp;

public class Request {
    private String senderID;
    private Timestamp sendOn;

    public Request(String sender, Timestamp sendOn) {
        this.senderID = sender;
        this.sendOn = sendOn;
    }

    public String getSender() {
        return senderID;
    }

    public void setSender(String sender) {
        this.senderID = sender;
    }

    public Timestamp getSendOn() {
        return sendOn;
    }

    public void setSendOn(Timestamp sendOn) {
        this.sendOn = sendOn;
    }
}
