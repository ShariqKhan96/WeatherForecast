package com.webxert.weatherforecast.common;

import android.location.Location;

import com.webxert.weatherforecast.model.WeatherForecastResult;
import com.webxert.weatherforecast.model.WeatherResult;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hp on 7/29/2018.
 */

public class Common {
    public static Location current_location = null;
    public static String APPID = "30037c4d99af56362246cad89a8f2e7e";
    public static WeatherResult weatherResult = null;
    public static WeatherForecastResult weatherForecastResult= null;

    public static String convertUnitToDate(long dt) {
        Date date = new Date(dt * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd EEE MM yyyy");
        String formated = sdf.format(date);
        return formated;

    }

    public static String convertUnixToHour(long sunrise) {
        Date date = new Date(sunrise * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formated = sdf.format(date);
        return formated;
    }
}
