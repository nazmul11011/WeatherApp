package com.example.weatherapp;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("v3-wx-observations-current")
    public Current current;

    @SerializedName("v3-location-point")
    public LocationPoint locationPoint;

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
        public double temperatureFeelsLike;

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