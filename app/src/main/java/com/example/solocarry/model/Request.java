package com.example.solocarry.model;

import java.sql.Timestamp;

public class Request {
    private String senderID;

    public Request(String sender) {
        this.senderID = sender;
    }

    public String getSender() {
        return senderID;
    }

    public void setSender(String sender) {
        this.senderID = sender;
    }
}
