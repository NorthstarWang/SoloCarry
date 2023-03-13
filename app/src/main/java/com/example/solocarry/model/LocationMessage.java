package com.example.solocarry.model;

import android.location.Location;

/**
 * This is Location Message class which extends the Message class. It represents
 * the type of message that users can use to share code information.
 * Each LocationMessage contains contains a location object and a code object.
 */
public class LocationMessage extends Message{
    private Location location;
    private Code code;

    /**
     * The constructor of a LocationMessage object,it accepts a location
     * , and a code object.
     * @param location the location of an LocationMessage
     * @param code the code contained in an LocationMessage
     */
    public LocationMessage(Location location, Code code) {
        this.location = location;
        this.code = code;
    }

    /**
     * The getLocation method returns the location of a specific LocationMessage object.
     * @return Location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * The setLocation method sets the location of a specific LocationMessage object.
     * @param location the new location we want to set
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * The getCode method gets the code from a specific LocationMessage object.
     * @return  Code
     */
    public Code getCode() {
        return code;
    }

    /**
     * The setCode method sets the code in a specific LocationMessage object.
     * @param code the new code we want to set
     */
    public void setCode(Code code) {
        this.code = code;
    }
}
