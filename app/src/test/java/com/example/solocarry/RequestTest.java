package com.example.solocarry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.example.solocarry.model.Request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RequestTest {
    Request mockRequest;

    @BeforeEach
    public void setUp(){
        mockRequest = new Request("request");
    }

    @Test
    public void testGetSender(){
        assertEquals(mockRequest.getSender(),"request");
    }

    @Test
    public void testSetSender(){
        mockRequest.setSender("request2");
        assertFalse(mockRequest.getSender() == "request");
        assertEquals(mockRequest.getSender(),"request2");
    }
}
