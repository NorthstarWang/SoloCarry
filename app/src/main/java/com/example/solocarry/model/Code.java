package com.example.solocarry.model;

import static java.lang.Character.isDigit;

import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is Code class that models the behaviour of a Treasure Code, the Code can
 * be scanned. Each code is associated with a name, a photo, geographic information,
 * a comment describes the code, and the associated score. Also, a code can be either
 * public to all users or not visible to users. The Code is associated with a hashCode
 * as unique identifier.
 */
public class Code {
    private String name;
    private int score;
    private boolean showPublic;
    private float latitude;
    private float longitude;
    private String hashCode;
    private String photoUrl;
    private String comment;

    public Code() {}

    /**
     * The constructor of a Code object,it accepts a hashCode, a code name
     * and the visibility, a specific Code is uniquely identified by hashCode.
     * @param hashCode the hashCode to construct a code object
     * @param name the name of a code object
     * @param showPublic the visibility a code object
     */
    public Code(String hashCode, String name, boolean showPublic) {
        this.hashCode = hashCode;
        this.showPublic = showPublic;
        this.score = 0;
        this.latitude = 0.0F;
        this.longitude = 0.0F;
        this.photoUrl = null;
        this.name = name;
    }

    /**
     * The getHashCode method returns the hashCode of a specific Code object.
     * @return string
     */
    public String getHashCode () {return hashCode;}

    /**
     * The getComment method returns the Comment of a specific Code object.
     * @return string
     */
    public String getComment() {
        return comment;
    }

    /**
     * The setComment method sets the comment of a specific Code object.
     * @param comment the string we want to set as comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * The getName method returns the name of a specific Code object.
     * @return string
     */
    public String getName() {
        return name;
    }

    /**
     * The setName method sets the name of a specific Code object.
     * @param name the string we want to set as name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The isShowPublic method returns the visibility of a specific Code object.
     * @return boolean
     */
    public boolean isShowPublic() {
        return showPublic;
    }

    /**
     * The setShowPublic method sets the visibility of a specific Code object.
     * @param showPublic the visibility we want to set in a code object
     */
    public void setShowPublic(boolean showPublic) {
        this.showPublic = showPublic;
    }

    /**
     * The updateScore method updates the score of a specific Code object.
     * @param newVal the new value of score we want to update in a code object
     */
    public void updateScore(int newVal) {
        this.score = newVal;
    }

    /**
     * The getScore method returns the score of a specific Code object.
     * @return int
     */
    public int getScore(){
        return this.score;
    }

    /**
     * The setLocation method sets the location of a specific Code object.
     * @param lat the float latitude we want to set in a code object
     * @param lon the float longitude we want to set in a code object
     */
    public void setLocation(float lat, float lon) {
        this.longitude = lon;
        this.latitude = lat;
    }

    /**
     * The changeLongitude method changes the longitude of a specific Code object.
     * @param longitude the float longitude we want to change in a code object
     */
    public void changeLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * The changeLatitude method changes the latitude of a specific Code object.
     * @param latitude the float latitude we want to set in a code object
     */
    public void changeLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * The getLatitude method gets the latitude of a specific Code object.
     * @return  latitude
     */
    public float getLatitude() {

        return latitude;
    }

    /**
     * The getLongitude method gets the longitude of a specific Code object.
     * @return  longitude
     */
    public float getLongitude() {return longitude;}

    /**
     * The getLongitude method gets the longitude of a specific Code object.
     * @param photoUrl the photo  we want to set in a code object
     */
    public void setPhoto(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    /**
     * The getPhoto method gets the photoUrl of a specific Code object.
     * @return photoUrl
     */
    public String getPhoto() {
        return photoUrl;
    }

    /**
     * The hashCodeToScore method convert a hashCode string into a integer score,
     * it receives a particulat hashCode and returns a score integer.
     * @param hashCode the hashCode that we wish to be converted into score
     * @return int
     */
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

    /**
     * The stringToSHA256 method hashes a string, so that each user can only collect
     * unique code, no duplicate code is allowed in any user's code collection.
     * @param str the string will be used to generate SHA256 string
     * @return String
     */
    public static String stringToSHA256(String str){
        return Hashing.sha256()
                .hashString(str, StandardCharsets.UTF_8)
                .toString();
    }

    /**
     * The worthToColor method convert a worth integer into a specific color,
     * @param worth the worth we want to convert to color
     * @return float
     */
    public static float worthToColor(int worth){
        if (worth<500){
            return BitmapDescriptorFactory.HUE_GREEN;
        }else if(worth < 1000){
            return BitmapDescriptorFactory.HUE_AZURE;
        }else if(worth < 2000){
            return BitmapDescriptorFactory.HUE_MAGENTA;
        }else if(worth < 5000){
            return BitmapDescriptorFactory.HUE_ORANGE;
        }else{
            return BitmapDescriptorFactory.HUE_RED;
        }
    }

    public static String hashCodeToName(String hashCode){
        String[] namingDictionary = {"Bap", "Ceg", "Dim", "Fov", "Guk", "Hap", "Jom", "Kud", "Lil", "Muf", "Nix", "Pog", "Qul", "Rik", "Siv", "Taz", "Vem", "Wun", "Xol", "Yek", "Zeb", "Avo", "Epu", "Ito", "Ora", "Uda"};
        String[] greekNumbers = {"Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta", "Iota", "Kappa"};
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            if (Character.isDigit(hashCode.charAt(i))){
                name.append(greekNumbers[Character.getNumericValue(hashCode.charAt(i))]);
            }else{
                name.append(namingDictionary[(int) hashCode.toLowerCase(Locale.ROOT).charAt(i) - 97]);
            }
        }
        return name.toString();
    }

    public static String[] codesIdToUid(String cid){
        return new String[]{cid.substring(0, 28), cid.substring(28)};
    }
}
