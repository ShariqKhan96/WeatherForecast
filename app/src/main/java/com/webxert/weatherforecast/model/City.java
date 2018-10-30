package com.webxert.weatherforecast.model;

/**
 * Created by hp on 7/30/2018.
 */

public class City {
    public int id;
    public String name;
    private Coord coord;
    private String country;

    public City() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "["+coord.getLat()+","+coord.getLon()+"]";
    }
}
