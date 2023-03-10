package com.example.solocarry.model;

import static java.lang.Character.isDigit;

import android.net.Uri;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Code {
    private int score;
    private float latitude;
    private float longitude;
    private String hashCode;
    private String photoUrl;

    public Code() {}

    public Code(String hashCode) {
        this.hashCode = hashCode;
        this.score = 0;
        this.latitude = 0.0F;
        this.longitude = 0.0F;
        this.photoUrl = null;
    }

    public String getHashCode () {return hashCode;}

    public void updateScore(int newVal) {
        this.score = newVal;
    }


    public int getScore(){
        return this.score;
    }
    public void setLocation(float lat, float lon) {
        this.longitude = lon;
        this.latitude = lat;
    }


    public void changeLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void changeLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLatitude() {

        return latitude;
    }

    public float getLongitude() {return longitude;}

    public void setPhoto(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhoto() {
        return photoUrl;
    }

    public static int hashCodeToScore(String hashCode){
        int total = 0;

        Pattern pattern = Pattern.compile("(.)\\1+");
        Matcher matcher = pattern.matcher(hashCode);

        while (matcher.find()){
            String group = matcher.group();
            if(isDigit(group.charAt(0))){
                if(Character.getNumericValue(group.charAt(0))==0){
                    total+=Math.pow(20,group.length());
                }else{
                    total+=Math.pow(Character.getNumericValue(group.charAt(0)),group.length());
                }
            }else{
                total+=Math.pow((int)group.charAt(0)-87,group.length());
            }
        }
        return total;

    }

    public static String stringToSHA256(String str){
        return Hashing.sha256()
                .hashString(str, StandardCharsets.UTF_8)
                .toString();
    }
}
