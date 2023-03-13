package com.example.solocarry.model;

import java.util.Date;
import java.util.UUID;
/**
 * This is Message class that models the a chatting message in a chat.
 * Each Message is associated with two Users, one sender, one receiver.
 * It also records the message send date and received date. Each message
 * is uniquely identified by the uuid.
 */
public abstract class Message {
    private Date sentDate;
    private Date readDate;
    private User sender;
    private User receiver;
    private UUID uuid;

    /**
     * The getSentDate method returns the sentDate a specific Message object.
     * @return Date
     */
    public Date getSentDate() {
        return sentDate;
    }

    /**
     * The setSentDate method sets the sentDate in a specific Message object.
     * @param sentDate the sentDate we want to set
     */
    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    /**
     * The getReadDate method returns the readDate a specific Message object.
     * @return Date
     */
    public Date getReadDate() {
        return readDate;
    }

    /**
     * The setReadDate method sets the readDate of a specific Message object.
     * @param readDate the readDate we want to set
     */
    public void setReadDate(Date readDate) {
        this.readDate = readDate;
    }

    /**
     * The getSender method returns the sender a specific Message object.
     * @return User
     */
    public User getSender() {
        return sender;
    }

    /**
     * The setSender method sets the sender a specific Message object.
     * @param sender the sender we want to set
     */
    public void setSender(User sender) {
        this.sender = sender;
    }

    /**
     * The getReceiver method returns the Receiver a specific Message object.
     * @return User
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     * The setReceiver method sets the receiver of a specific Message object.
     * @param receiver the receiver we want to set
     */
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    /**
     * The getUuid method returns the uuid a specific Message object.
     * @return UUID
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * The setUuid method sets the uuid of a specific Message object.
     * @param uuid the uuid we want to set
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
