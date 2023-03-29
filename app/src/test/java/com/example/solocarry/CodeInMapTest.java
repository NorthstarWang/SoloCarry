package com.example.solocarry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.solocarry.model.CodeInMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CodeInMapTest {
    CodeInMap mockedCodeInMap;

    @BeforeEach
    public void setUp(){
        mockedCodeInMap = new CodeInMap("sample",1);
    }

    @Test
    public void testGetScore(){
        assertEquals(mockedCodeInMap.getScore(),1);
    }

    @Test
    public void testSetScore(){
        mockedCodeInMap.setScore(20);
        assertEquals(mockedCodeInMap.getScore(),20);
    }

    @Test
    public void testGetHashCode(){
        assertEquals(mockedCodeInMap.getHashCode(),"sample");
    }

    @Test
    public void testSetHashCode(){
        mockedCodeInMap.setHashCode("sample2");
        assertEquals(mockedCodeInMap.getHashCode(),"sample2");
    }

    @Test
    public void testGetOwnerIds(){
        List<String> list = new ArrayList<>();
        assertEquals(mockedCodeInMap.getOwnerIds(),list);
    }

    @Test
    public void testSetOwnerIds(){
        List<String> list = new ArrayList<>();
        list.add("Alberta");
        mockedCodeInMap.setOwnerIds(list);
        assertEquals(mockedCodeInMap.getOwnerIds(),list);
    }

    @Test
    public void testComparedTo(){
        CodeInMap mockedCodeInMap2 = new CodeInMap("sample2",30);
        // if condition equal to 0, then two number is the same
        // if condition less than 0, mockedCodeInMap is smaller
        // if condition is bigger than 0, mockedInMap is larger
        int condition = mockedCodeInMap.compareTo(mockedCodeInMap2);
        assertTrue(condition < 0);
    }
}
