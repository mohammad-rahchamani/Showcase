package com.example.myapplication.models;

/**
 * Created by neo on 3/8/16.
 */
public class SunriseResponseModel {

    SunriseData results;

    public SunriseData getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "sunrise : " + results.sunrise + " , sunset : " + results.sunset;
    }

    public static class SunriseData {

        String sunrise;
        String sunset;
        long day_length;

        public String getSunrise() {
            return sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public long getDay_length() {
            return day_length;
        }
    }
}
