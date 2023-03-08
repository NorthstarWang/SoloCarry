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
        User user1 = new User("mock1","mock1@exampl.com","aa",mock(Uri.class));
        User user2 = new User("mock2","mock2@exampl.com","bb",mock(Uri.class));
        User user3 = new User("mock3","mock3@exampl.com","cc",mock(Uri.class));
        User user4 = new User("mock4","mock4@exampl.com","dd",mock(Uri.class));

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

//    @Test
//    public void testSortUserArrayList() {
//        userList.add(user1);
//        userList.add(user2);
//        userList.add(user3);
//        userList.add(user4);
//        mockRank.setUserArrayList(userList);
//        mockRank.sortUserArrayList();
//        Collections.sort(userList);
//        assertEquals(mockRank.getUserArrayList(),userList);
//    }
}
