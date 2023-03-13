package com.example.solocarry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import android.net.Uri;

import com.example.solocarry.model.Invitation;
import com.example.solocarry.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InvitationTest {
    Invitation mockInvitation;
    User mockUser;
    User mockUser2;
    @BeforeEach
    public void setUp(){
        mockUser = new User("mock","mock@exampl.com","aa","picture1",0);
        mockUser2 = new User("mock2","mock2@exampl.com","bb","picture2",0);
        mockInvitation = new Invitation(mockUser,mockUser2,"come");
    }

    @Test
    public void testGetInvitation(){
        assertEquals(mockInvitation.getText(),"come");
    }

    @Test
    public void testUpdateInvitation(){
        mockInvitation.updateInvitation("refuse");
        assertEquals(mockInvitation.getText(),"refuse");
    }

    @Test
    public void testGetDate(){
        assertEquals(mockInvitation.getDate(),null);
    }

    @Test
    public void testGetUserOne(){
        assertEquals(mockInvitation.getUserOne(),mockUser);
    }

    @Test
    public void testGetUserTwo(){
        assertEquals(mockInvitation.getUserTwo(),mockUser2);
    }
}
