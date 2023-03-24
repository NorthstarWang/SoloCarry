package com.example.solocarry.model;

import java.util.ArrayList;
import java.util.List;

public class CodeInMap implements Comparable<CodeInMap> {
    private String hashCode;
    private List<String> OwnerIds;
    private int score;

    public CodeInMap() {
    }

    public CodeInMap(String hashCode, int score) {
        this.hashCode = hashCode;
        OwnerIds = new ArrayList<>();
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public List<String> getOwnerIds() {
        return OwnerIds;
    }

    public void setOwnerIds(List<String> ownerIds) {
        OwnerIds = ownerIds;
    }

    @Override
    public int compareTo(CodeInMap codeInMap) {
        return Integer.compare(score, codeInMap.getScore());
    }
}
