package com.example.solocarry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

import android.net.Uri;

import com.example.solocarry.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FriendTest {

    User mockUser;
    Friend mockFriend;

    Uri mockUri;
    @BeforeEach
    public void setUp(){
        mockUser = new User("mock","mock@exampl.com","aa","picture",0);
        String name = "Lawrence";
        String email = "lawrence@example.com";
        String uid = "bb";
        mockUri = mock(Uri.class);
        int score = 10;
        mockFriend = new Friend(mockUser, name, email, uid, mockUri, score);
    }

    @Test
    public void testUpdateAssociatedUser(){
        assertEquals(mockFriend.getAssociatedUser(),mockUser);
        User mockUser2 = new User("mock2","mock2@exampl.com","cc","picture2",0);
        mockFriend.updateAssociatedUser(mockUser2);
        assertFalse(mockUser == mockUser2);
        assertEquals(mockFriend.getAssociatedUser(),mockUser2);
    }

    @Test
    public void testGetFriendPhotoUrl(){
        assertEquals(mockFriend.getFriendPhotoUrl(),mockUri);
    }

    @Test
    public void testGetFriendScore(){
        assertEquals(mockFriend.getFriendScore(),10);
    }

    @Test
    public void testUpdateFriendScore(){
        mockFriend.updateFriendScore(30);
        assertEquals(mockFriend.getFriendScore(),30);
    }

    @Test
    public void testGetFriendName(){
        assertEquals(mockFriend.getFriendName(),"Lawrence");
    }

    @Test
    public void testGetFriendEmail(){
        assertEquals(mockFriend.getFriendEmail(),"lawrence@example.com");
    }
}
