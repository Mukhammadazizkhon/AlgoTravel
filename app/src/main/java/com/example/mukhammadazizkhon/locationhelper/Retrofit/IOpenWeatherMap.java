package com.example.mukhammadazizkhon.locationhelper.Retrofit;

import com.example.mukhammadazizkhon.locationhelper.Model.WeatherForecastResult;
import com.example.mukhammadazizkhon.locationhelper.Model.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mukha on 9/27/2018.
 */

public interface IOpenWeatherMap {
    @GET("weather")
    Observable<WeatherResult> getWeatherByLatitudeAndLongitude(@Query("lat") String lat,
                                                               @Query("lon") String lon,
                                                               @Query("appid") String appid,
                                                               @Query("units") String unit);
    @GET("forecast")
    Observable<WeatherForecastResult> getWeatherForecastByLatitudeAndLongitude(@Query("lat") String lat,
                                                                               @Query("lon") String lon,
                                                                               @Query("appid") String appid,
                                                                               @Query("units") String unit);
}
