package com.example.solocarry.model;

import java.util.ArrayList;
import java.util.List;

public class CodeInMap implements Comparable<CodeInMap> {
    private String hashCode;
    private List<String> OwnerIds;
    private int score;

    public CodeInMap() {
    }

    /**
     * This CodeInMap method is the constructor of CodeInMap object
     * @param hashCode the hash code of the object
     * @param score the assigned score of the object
     */
    public CodeInMap(String hashCode, int score) {
        this.hashCode = hashCode;
        OwnerIds = new ArrayList<>();
        this.score = score;
    }

    /**
     * This getScore method returns the score of a CodeInMap object
     * @return int
     */
    public int getScore() {
        return score;
    }

    /**
     * This setScore method sets a score to the CodeInMap object
     * @param score the score we want to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * This getHashCode method returns the hashcode of a CodeInMap object
     * @return string
     */
    public String getHashCode() {
        return hashCode;
    }

    /**
     * This setHashCode method sets the hash code of a CodeInMap object
     * @param hashCode the hashcode we want to set
     */
    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    /**
     * This getOwnerIds method returns all the owner ids of a CodeInMap object
     * @return List of string
     */
    public List<String> getOwnerIds() {
        return OwnerIds;
    }

    /**
     * This setOwnerIds method sets the list of owner Ids of a CodeInMap object
     * @param ownerIds a list of owner id strings
     */
    public void setOwnerIds(List<String> ownerIds) {
        OwnerIds = ownerIds;
    }

    /**
     * This compareTo method defines the comparison among different CodeInMap objects
     * @return int
     */
    @Override
    public int compareTo(CodeInMap codeInMap) {
        return Integer.compare(score, codeInMap.getScore());
    }
}
