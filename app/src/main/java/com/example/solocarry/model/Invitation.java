package com.example.solocarry.model;

import java.util.Date;

/**
 * This is Invitation class that models the behaviour of a Invitation, the invitation
 * is held by two users. Each invitation is associated with two users, a text invitation
 * string, and the invitation date, The Invitation object is associated with the combination
 * of two users' ids as unique identifier.
 */
public class Invitation {

    private String Text;
    private User userOne;
    private User userTwo;
    private Date date;

    /**
     * The constructor of a Invitation object,it accepts two users, and a invitationText.
     * @param one the first user of an invitation
     * @param two the second user of an invitation
     * @param invitationText the text of an invitation
     */
    public Invitation(User one, User two, String invitationText) {
        this.userOne = one;
        this.userTwo = two;
        this.Text = invitationText;
    }

    /**
     * The getUserOne method returns the first user of a specific Invitation object.
     * @return User
     */
    public User getUserOne() {return userOne;}

    /**
     * The getUserTwo method returns the second user of a specific Invitation object.
     * @return User
     */
    public User getUserTwo() {return userTwo;}

    /**
     * The getInvID method returns the invitation ID of a specific Invitation object.
     * @return string
     */
    public String getInvID () {return userOne.getUid() + " " + userTwo.getUid();}

    /**
     * The updateInvitation method updates the invitation text message
     * of a specific Invitation object.
     * @param invitationText the text of an invitation
     */
    public void updateInvitation(String invitationText) {
        this.Text = invitationText;
    }

    /**
     * The getText method returns the invitation Text of a specific Invitation object.
     * @return string
     */
    public String getText() {return Text;}

    /**
     * The getDate method gets the Date of a specific Invitation object.
     * @return Date
     */
    public Date getDate() {
        return date;
    }
}
