package com.example.weatherapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplayName,tvLocation,tvTemp,tvCloudCover,tvFeelsLike,tvWindSpeed,tvRelativeHumidity,tvDewPoint,tvPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        tvDisplayName = findViewById(R.id.tv_location);
        tvTemp = findViewById(R.id.tv_temp);
        tvCloudCover = findViewById(R.id.tv_cloudCover);
        tvFeelsLike = findViewById(R.id.tv_feelslike);
        tvWindSpeed = findViewById(R.id.tv_windspeed);
        tvRelativeHumidity = findViewById(R.id.tv_relativehumidity);
        tvLocation = findViewById(R.id.tv_corelocation);
        tvDewPoint = findViewById(R.id.tv_dewpoint);
        tvPressure = findViewById(R.id.tv_pressure);

        String url = "https://api.weather.com/v2/aggcommon/v3-location-point;v3alertsHeadlines;v3-wx-observations-current;v3-links?par=samsung_widget&geocode=24.926%2C91.835&language=en-us&units=m&format=json&apiKey=793db2b6128c4bc2bdb2b6128c0bc230";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                WeatherResponse weatherResponse = gson.fromJson(response, WeatherResponse.class);

                if (weatherResponse != null && weatherResponse.current != null) {
                    String displayname = weatherResponse.locationPoint.location.displayName;
                    String district = weatherResponse.locationPoint.location.adminDistrict;
                    String location = weatherResponse.locationPoint.location.displayContext;
                    tvDisplayName.setText(displayname + ", " + district);
                    tvLocation.setText(location);

                    int temperature = weatherResponse.current.temperature;
                    tvTemp.setText(temperature + " °C");

                    String cloudCoverPhase = weatherResponse.current.cloudCoverPhrase;
                    tvCloudCover.setText(cloudCoverPhase);

                    double feelslike = weatherResponse.current.temperatureFeelsLike;
                    tvFeelsLike.setText("Feels Like " + feelslike+ "°");

                    int windspeed = weatherResponse.current.windSpeed;
                    tvWindSpeed.setText(windspeed + "KM/h");

                    int humidity = weatherResponse.current.relativeHumidity;
                    tvRelativeHumidity.setText(humidity + "%");

                    int dewpoint = weatherResponse.current.temperatureDewPoint;
                    tvDewPoint.setText(dewpoint + "°");

                    double pressure = weatherResponse.current.pressureAltimeter;
                    tvPressure.setText(pressure + " mb");
                } else {
                    tvTemp.setText("Error parsing response");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvTemp.setText(error.toString());
            }
        });

        queue.add(stringRequest);
    }
}