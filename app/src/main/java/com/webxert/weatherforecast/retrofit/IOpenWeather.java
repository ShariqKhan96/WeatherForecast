package com.webxert.weatherforecast.retrofit;

import com.webxert.weatherforecast.model.WeatherForecastResult;
import com.webxert.weatherforecast.model.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hp on 7/29/2018.
 */

public interface IOpenWeather {
    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLng(@Query("lat") String lat,
                                                 @Query("lon") String lng,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit);


    @GET("forecast")
    Observable<WeatherForecastResult> getForecastWeatherByLatLng(@Query("lat") String lat,
                                                                 @Query("lon") String lon,
                                                                 @Query("appid") String appid,
                                                                 @Query("units") String unit);

    @GET("weather")
    Observable<WeatherResult> getWeatherByCity(@Query("q") String cityName,
                                               @Query("appid") String appid,
                                               @Query("units") String units);
}
