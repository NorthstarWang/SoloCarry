package com.example.solocarry.model;

import android.location.Location;

public class LocationMessage extends Message{
    private Location location;
    private Code code;

    public LocationMessage(Location location, Code code) {
        this.location = location;
        this.code = code;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }
}
