package com.example.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private TextView tvDisplayName, tvLocation, tvTemp, tvCloudCover, tvFeelsLike, tvWindSpeed, tvRelativeHumidity, tvDewPoint, tvPressure,tvNarrative;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplayName = findViewById(R.id.tv_location);
        tvTemp = findViewById(R.id.tv_temp);
        tvCloudCover = findViewById(R.id.tv_cloudCover);
        tvFeelsLike = findViewById(R.id.tv_feelslike);
        tvWindSpeed = findViewById(R.id.tv_windspeed);
        tvRelativeHumidity = findViewById(R.id.tv_relativehumidity);
        tvLocation = findViewById(R.id.tv_corelocation);
        tvDewPoint = findViewById(R.id.tv_dewpoint);
        tvPressure = findViewById(R.id.tv_pressure);

        tvNarrative = findViewById(R.id.tv_narrative);

        checkLocationPermissions();
    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLocation();
        }
    }

    private void getLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
//        Toast.makeText(this, "Location: " + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
        fetchWeatherData(location);
    }

    private void fetchWeatherData(Location location) {
        String apiKey = "793db2b6128c4bc2bdb2b6128c0bc230"; // Replace with your actual API key
        String url = "https://api.weather.com/v2/aggcommon/v3-location-point;v3-wx-observations-current;v3-links" +
                "?par=samsung_widget&geocode=" + location.getLatitude() + "%2C" + location.getLongitude() +
                "&language=en-us&units=m&format=json&apiKey=" + apiKey;

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseWeatherResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvTemp.setText("Error: " + error.toString());
            }
        });

        queue.add(stringRequest);
    }

    @SuppressLint("SetTextI18n")
    private void parseWeatherResponse(String response) {
        Gson gson = new Gson();
        try {
            WeatherResponse weatherResponse = gson.fromJson(response, WeatherResponse.class);

            if (weatherResponse != null && weatherResponse.current != null) {
                tvDisplayName.setText(weatherResponse.locationPoint.location.displayName + ", " + weatherResponse.locationPoint.location.adminDistrict);
                tvLocation.setText(weatherResponse.locationPoint.location.displayContext);
                tvTemp.setText(weatherResponse.current.temperature + " °C");
                tvCloudCover.setText(weatherResponse.current.cloudCoverPhrase);
                tvFeelsLike.setText("Feels Like " + weatherResponse.current.temperatureFeelsLike + "°");
                tvWindSpeed.setText(weatherResponse.current.windSpeed + " KM/h");
                tvRelativeHumidity.setText(weatherResponse.current.relativeHumidity + "%");
                tvDewPoint.setText(weatherResponse.current.temperatureDewPoint + "°");
                tvPressure.setText(weatherResponse.current.pressureAltimeter + " mb");
                tvNarrative.setText(weatherResponse.fcstdaily7s.forecasts.narrative);
            } else {
                tvTemp.setText("Error: Invalid response");
            }
        } catch (Exception e) {
            tvNarrative.setText("Error parsing response");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
