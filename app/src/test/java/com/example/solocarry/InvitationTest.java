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
    @BeforeEach
    public void setUp(){
        User mockUser = new User("mock","mock@exampl.com","aa",mock(Uri.class));
        User mockUser2 = new User("mock2","mock2@exampl.com","bb",mock(Uri.class));
        mockInvitation = new Invitation(mockUser,mockUser2,"come");
    }

    @Test
    public void testGetInvitation(){
        assertEquals(mockInvitation.getInvitation(),"come");
    }

    @Test
    public void testUpdateInvitation(){
        mockInvitation.updateInvitation("refuse");
        assertEquals(mockInvitation.getInvitation(),"refuse");
    }

    @Test
    public void testGetDate(){
        assertEquals(mockInvitation.getDate(),null);
    }
}
