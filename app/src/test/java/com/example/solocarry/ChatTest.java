package com.example.solocarry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.example.solocarry.model.Chat;
import com.example.solocarry.model.Message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

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
        assertEquals(mockChat.showMessages(),defaultMessage);
    }

    @Test
    public void testAddMessage(){
        mockMessage = mock(Message.class);
        mockChat.addMessage(mockMessage);
        ArrayList<Message> mockMessageList = new ArrayList<>();
        mockMessageList.add(mockMessage);
        assertEquals(mockChat.showMessages(),mockMessageList);
    }

    @Test
    public void testDeleteMessage(){
        mockMessage = mock(Message.class);
        mockChat.addMessage(mockMessage);
        ArrayList<Message> mockMessageList = new ArrayList<>();
        mockMessageList.add(mockMessage);
        assertEquals(mockChat.showMessages(),mockMessageList);

        ArrayList<Message> emptyList = new ArrayList<>();
        mockChat.deleteMessage(mockMessage);
        assertEquals(mockChat.showMessages(),emptyList);
    }

    @Test
    public void getOwnerID(){
        List<String> list = Arrays.asList(mockUid1,mockUid2);
        ArrayList<String> pair = new ArrayList<>(list);
        assertEquals(mockChat.getOwnerID(),pair);
    }
}
