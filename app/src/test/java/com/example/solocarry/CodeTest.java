package com.example.solocarry;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

import android.net.Uri;

import com.example.solocarry.model.Code;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CodeTest {
    float mockLatitude;
    float mockLongitude;
    Uri mockUri;
    Code mockCode;
    @BeforeEach
    public void setUp(){
        mockCode = new Code(233);
        mockLatitude = (float) 53.523221;
        mockLongitude = (float) -113.526319;
    }

    @Test
    public void getScore(){
        assertEquals(mockCode.getScore(),0);
    }

    @Test
    public void testUpdateScore(){
        mockCode.updateScore(123);
        assertEquals(mockCode.getScore(),123);
    }

    @Test
    public void testSetLocation(){
        assertEquals(mockCode.getLongitude(),0.0F);
        assertEquals(mockCode.getLatitude(),0.0F);
        mockCode.setLocation(mockLatitude,mockLongitude);
        assertEquals(mockCode.getLatitude(),mockLatitude);
        assertEquals(mockCode.getLongitude(),mockLongitude);
    }

    @Test
    public void testChangeLongitude(){
        mockCode.changeLongitude(0.3F);
        assertEquals(mockCode.getLongitude(),0.3F);
    }

    @Test
    public void testChangeLatitude(){
        mockCode.changeLatitude(0.5F);
        assertEquals(mockCode.getLatitude(),0.5F);
    }

    @Test
    public void testGetPhoto(){
        assertEquals(mockCode.getPhoto(),null);
    }

    @Test
    public void testSetPhoto(){
        Uri mockUri = mock(Uri.class);
        mockCode.setPhoto(mockUri);
        assertEquals(mockCode.getPhoto(),mockUri);
    }
}
