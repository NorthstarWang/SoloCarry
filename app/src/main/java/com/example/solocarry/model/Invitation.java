package com.example.solocarry.model;

import java.util.Date;

public class Invitation {

    private String Text;
    private User userOne;
    private User userTwo;
    private Date date;

    public Invitation(User one, User two, String invitationText) {
        this.userOne = one;
        this.userTwo = two;
        this.Text = invitationText;
    }

    public User getUserOne() {return userOne;}
    public User getUserTwo() {return userTwo;}
    public String getInvID () {return userOne.getUid() + " " + userTwo.getUid();}

    public void updateInvitation(String invitationText) {
        this.Text = invitationText;
    }

<<<<<<< HEAD
    public String getInvitation(){
        return this.Text;
    }
=======
    public String getText() {return Text;}
>>>>>>> 971c8e7c76a24eea5801aa5f114c7f44ab8e590d

    public Date getDate() {
        return date;
    }
}
