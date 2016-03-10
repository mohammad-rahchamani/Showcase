package com.example.myapplication.network;

import com.example.myapplication.models.SunriseResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by neo on 3/8/16.
 */
public interface SunriseSunsetApi {

    @GET("json")
    Call<SunriseResponseModel> getData(@Query("lat") double latitude, @Query("lng") double longitude,
                                       @Query("formatted") int formatted);

}
