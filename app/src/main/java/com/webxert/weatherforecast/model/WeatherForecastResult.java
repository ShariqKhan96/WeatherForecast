package com.webxert.weatherforecast.model;

import java.util.List;

/**
 * Created by hp on 7/30/2018.
 */

public class WeatherForecastResult {
    private String cod;
    private double message;
    private int cnt;
    public List<MyList> list;
    public City city;

    public WeatherForecastResult() {
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<MyList> getList() {
        return list;
    }

    public void setList(List<MyList> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
