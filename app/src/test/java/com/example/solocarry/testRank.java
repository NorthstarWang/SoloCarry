package com.example.solocarry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import android.net.Uri;

import com.example.solocarry.model.Rank;
import com.example.solocarry.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

public class testRank {
    User user1;
    User user2;
    User user3;
    User user4;
    Rank mockRank;
    ArrayList<User> userList;
    @BeforeEach
    public void setUp(){
        user1 = new User("a","mock1@exampl.com","aa","p1",0);
        user2 = new User("d","mock2@exampl.com","bb","p2",0);
        user3 = new User("b","mock3@exampl.com","cc","p3",0);
        user4 = new User("c","mock4@exampl.com","dd","p4",0);
        user1.setScore(10);
        user2.setScore(50);
        user3.setScore(20);
        user4.setScore(30);
        mockRank = new Rank();
        userList = new ArrayList<>();
    }

    @Test
    public void testGetUserArrayList(){
        assertEquals(mockRank.getUserArrayList(),null);
    }

    @Test
    public void testSetUserArrayList(){
        userList.add(user1);
        mockRank.setUserArrayList(userList);
        assertEquals(mockRank.getUserArrayList(),userList);
    }

    @Test
    public void testSortUserArrayList() {
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);

        mockRank.setUserArrayList(userList);
        mockRank.sortUserArrayList();

        ArrayList<User> expectedList = new ArrayList<>();
        expectedList.add(user1);
        expectedList.add(user3);
        expectedList.add(user4);
        expectedList.add(user2);
        assertEquals(mockRank.getUserArrayList(),expectedList);
    }
}
