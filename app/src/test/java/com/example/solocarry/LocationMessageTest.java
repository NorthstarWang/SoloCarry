package com.example.solocarry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.location.Location;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import com.example.solocarry.model.Code;
import com.example.solocarry.model.LocationMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LocationMessageTest {

    LocationMessage mockLocationMessage;
    Location mockLocation;
    Code mockCode;
    @BeforeEach
    public void setUp(){
        mockLocation = mock(Location.class);
        mockCode = mock(Code.class);
        mockLocationMessage = new LocationMessage(mockLocation,mockCode);
    }

    @Test
    public void testGetLocation(){
        assertEquals(mockLocationMessage.getLocation(),mockLocation);
    }

    @Test
    public void testSetLocation(){
        Location mockLocation2 = mock(Location.class);
        mockLocationMessage.setLocation(mockLocation2);
        assertEquals(mockLocation2,mockLocationMessage.getLocation());
    }

    @Test
    public void testGetCode(){
        assertEquals(mockCode,mockLocationMessage.getCode());
    }

    @Test
    public void testSetCode(){
        Code mockCode2 = mock(Code.class);
        mockLocationMessage.setCode(mockCode2);
        assertFalse(mockCode2 == mockCode);
        assertEquals(mockCode2,mockLocationMessage.getCode());
    }
}
