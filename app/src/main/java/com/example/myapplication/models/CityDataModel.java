package com.example.myapplication.models;

/**
 * Created by neo on 3/8/16.
 */
public class CityDataModel {

    String name;
    double latitude;
    double longitude;
    int zoneMinuteDiff;

    public CityDataModel(String name, double latitude, double longitude, int zoneMinuteDiff) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.zoneMinuteDiff = zoneMinuteDiff;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public int getZoneMinuteDiff() {
        return zoneMinuteDiff;
    }
}
