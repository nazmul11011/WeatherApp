package com.example.weatherapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {
    @SerializedName("v3-wx-observations-current")
    public Current current;

    @SerializedName("v3-location-point")
    public LocationPoint locationPoint;

    @SerializedName("v2fcstdaily7s")
    public Fcstdaily7s fcstdaily7s;

    public static class Fcstdaily7s {
        @SerializedName("forecasts")
        public List<Forecast> forecasts;
    }
    public static class Forecast {
        @SerializedName("narrative")
        public String narrative;
        @SerializedName("dow")
        public String day;
        @SerializedName("num")
        public int num;
        @SerializedName("min_temp")
        public int min_temp;
        @SerializedName("max_temp")
        public int max_temp;
    }
    public static class LocationPoint {
        @SerializedName("location")
        public Location location;
    }

    public static class Location {
        @SerializedName("displayName")
        public String displayName;

        @SerializedName("adminDistrict")
        public String adminDistrict;

        @SerializedName("displayContext")
        public String displayContext;
    }

    public static class Current {
        @SerializedName("temperature")
        public int temperature;

        @SerializedName("cloudCoverPhrase")
        public String cloudCoverPhrase;

        @SerializedName("temperatureFeelsLike")
        public int temperatureFeelsLike;

        @SerializedName("windSpeed")
        public int windSpeed;

        @SerializedName("relativeHumidity")
        public int relativeHumidity;

        @SerializedName("temperatureDewPoint")
        public int temperatureDewPoint;

        @SerializedName("pressureAltimeter")
        public double pressureAltimeter;
    }
}
