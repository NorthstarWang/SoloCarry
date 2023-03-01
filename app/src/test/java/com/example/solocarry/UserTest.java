package com.example.solocarry;

import android.net.Uri;

import com.example.solocarry.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {
    public User user;
    public Uri uri;

    @BeforeEach
    public void setUp() {
        String name = "Lawrence";
        String email = "lawrence.example@gmail.com";
        String uid = "testing";
        uri = (Uri) Uri.CREATOR;
        user = new User(name, email, uid, uri);
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
        assertEquals(user.getPhotoUrl(),uri);
    }


}
