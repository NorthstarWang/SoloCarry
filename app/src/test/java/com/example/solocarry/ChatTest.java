package com.example.solocarry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ChatTest {
    Chat mockChat;
    ArrayList<Message> defaultMessage;
    Message mockMessage;
    String mockUid1;
    String mockUid2;
    @BeforeEach
    public void setUp(){
        mockUid1 = "UC0079";
        mockUid2 = "UC0080";
        mockChat = new Chat(mockUid1, mockUid2);
    }

    @Test
    public void testShowMessage(){
        defaultMessage = new ArrayList<>();
        assertEquals(mockChat.getMessageList(),defaultMessage);
    }

    @Test
    public void testAddMessage(){
        mockMessage = mock(Message.class);
        mockChat.addMessage(mockMessage);
        ArrayList<Message> mockMessageList = new ArrayList<>();
        mockMessageList.add(mockMessage);
        assertEquals(mockChat.getMessageList(),mockMessageList);
    }

    @Test
    public void testDeleteMessage(){
        mockMessage = mock(Message.class);
        mockChat.addMessage(mockMessage);
        ArrayList<Message> mockMessageList = new ArrayList<>();
        mockMessageList.add(mockMessage);
        assertEquals(mockChat.getMessageList(),mockMessageList);

        ArrayList<Message> emptyList = new ArrayList<>();
        mockChat.deleteMessage(mockMessage);
        assertEquals(mockChat.getMessageList(),emptyList);
    }

    @Test
    public void testGetOwnerID(){
        String message = mockUid1 + " " + mockUid2;
        assertEquals(mockChat.getOwnerID(),message);
    }

    @Test
    public void testGetUidOne(){
        assertEquals(mockChat.getUidOne(),mockUid1);
    }

    @Test
    public void testGetUidTwo(){
        assertEquals(mockChat.getUidTwo(),mockUid2);
    }

}
