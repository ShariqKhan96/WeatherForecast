package com.webxert.weatherforecast.model;

/**
 * Created by hp on 7/29/2018.
 */

public class Coord {
    private double lon ;
    private double lat;

    public Coord() {
    }

    public double getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

//    @Override
//    public String toString() {
//        return new StringBuilder().append('[').append(this.lat).append(',').append(this.lon).append(']').toString();
//    }
}
