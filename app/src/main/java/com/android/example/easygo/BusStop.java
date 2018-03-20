package com.android.example.easygo;

/**
 * Created by Meeth on 20-Mar-18.
 */

public class BusStop {

    String name;
    double latitude;
    double longitude;

    public BusStop(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public BusStop() {
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
