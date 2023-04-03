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
    public String mockUri;

    @BeforeEach
    public void setUp() {
        String name = "Lawrence";
        String email = "lawrence.example@gmail.com";
        String uid = "testing";
        mockUri = "picture1";
        user = new User(name, email, uid, mockUri,0);
    }

    @Test
    public void testGetName() {
        assertEquals(user.getName(),"Lawrence");
    }

    @Test
    public void testSetName(){
        String expected = "Olivia";
        user.setName(expected);
        assertFalse(user.getName() == "Lawrence");
        assertEquals(user.getName(),expected);
    }

    @Test
    public void testGetEmail(){
        assertEquals(user.getEmail(),"lawrence.example@gmail.com");
    }

    @Test
    public void testSetEmail(){
        String expected = "Olivia.example@gmail.com";
        user.setEmail(expected);
        assertEquals(user.getEmail(),expected);
    }

    @Test
    public void testGetUid(){
        assertEquals(user.getUid(),"testing");
    }

    @Test
    public void testSetUid(){
        user.setUid("testing2");
        assertEquals(user.getUid(),"testing2");
    }

    @Test
    public void testGetPhotoUrl(){
        assertEquals(mockUri,user.getPhotoUrl());
    }

    @Test
    public void testSetPhotoUrl(){
        String mockUri2 = "picture2";
        user.setPhotoUrl(mockUri2);
        assertEquals(user.getPhotoUrl(),mockUri2);
    }

    @Test
    public void testGetScore(){
        assertEquals(user.getScore(),0);
    }

    @Test
    public void testSetScore(){
        user.setScore(100);
        assertEquals(user.getScore(),100);
    }

    @Test
    public void testAddScore(){
        user.addScore(20);
        user.addScore(30);
        assertEquals(user.getScore(),50);
    }

    @Test
    public void testMinusScore(){
        user.addScore(30);
        assertEquals(user.getScore(),30);
        user.minusScore(10);
        assertEquals(user.getScore(),20);
    }
}
