package com.example.solocarry;

import android.net.Uri;

import com.example.solocarry.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
public class UserTest {
    public User user;
    public Uri mockUri;

    @BeforeEach
    public void setUp() {
        String name = "Lawrence";
        String email = "lawrence.example@gmail.com";
        String uid = "testing";
        mockUri = mock(Uri.class);
        user = new User(name, email, uid, mockUri);
    }

    @Test
    void testGetName() {
        assertEquals(user.getName(),"Lawrence");
    }

    @Test
    void testSetName(){
        String expected = "Olivia";
        user.setName(expected);
        assertFalse(user.getName() == "Lawrence");
        assertEquals(user.getName(),expected);
    }

    @Test
    void testGetEmail(){
        assertEquals(user.getEmail(),"lawrence.example@gmail.com");
    }

    @Test
    void testSetEmail(){
        String expected = "Olivia.example@gmail.com";
        user.setEmail(expected);
        assertEquals(user.getEmail(),expected);
    }

    @Test
    void testGetUid(){
        assertEquals(user.getUid(),"testing");
    }

    @Test
    void testSetUid(){
        user.setUid("testing2");
        assertEquals(user.getUid(),"testing2");
    }

    @Test
    void testGetPhotoUrl(){
        assertEquals(mockUri,user.getPhotoUrl());
    }

    @Test
    void testSetPhotoUrl(){
        Uri mockUri2 = mock(Uri.class);
        user.setPhotoUrl(mockUri2);
        assertEquals(user.getPhotoUrl(),mockUri2);
    }

    @Test
    void testGetScore(){
        assertEquals(user.getScore(),0);
    }

    @Test
    void testSetScore(){
        user.setScore(100);
        assertEquals(user.getScore(),100);
    }
}
