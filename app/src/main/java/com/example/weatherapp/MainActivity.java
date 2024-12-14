package com.example.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private TextView tvDisplayName, tvLocation, tvTemp, tvCloudCover, tvFeelsLike, tvWindSpeed, tvRelativeHumidity, tvDewPoint, tvPressure,tvNarrative,tvForecastHigh,tvForecastLow,tvForecastDay2,tvForecastDay3,tvForecastDay4,tvForecastDay5,tvForecastDay6,tvForecastDay7;
    private TextView tvForecastHigh2,tvForecastHigh3,tvForecastHigh4,tvForecastHigh5,tvForecastHigh6,tvForecastHigh7,tvForecastLow2,tvForecastLow3,tvForecastLow4,tvForecastLow5,tvForecastLow6,tvForecastLow7;
    private TextView tvHourlyTemp1,tvHourlyTemp2,tvHourlyTemp3,tvHourlyTemp4,tvPerc1Rain,tvPerc2Rain,tvPerc3Rain,tvPerc4Rain,tvTime1,tvTime2,tvTime3,tvTime4;
    private LocationManager locationManager;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] { Color.parseColor("#131122"), Color.parseColor("#040622") }
        );

        RelativeLayout layout = findViewById(R.id.main);
        layout.setBackground(gradient);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
        }
        getWindow().setStatusBarColor(getColor(R.color.background));
        getWindow().setNavigationBarColor(getColor(R.color.background));

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
        tvForecastHigh = findViewById(R.id.tv_forecasthigh);
        tvForecastHigh2 = findViewById(R.id.tv_forecasthigh2);
        tvForecastHigh3 = findViewById(R.id.tv_forecasthigh3);
        tvForecastHigh4 = findViewById(R.id.tv_forecasthigh4);
        tvForecastHigh5 = findViewById(R.id.tv_forecasthigh5);
        tvForecastHigh6 = findViewById(R.id.tv_forecasthigh6);
        tvForecastHigh7 = findViewById(R.id.tv_forecasthigh7);
        tvForecastLow = findViewById(R.id.tv_forecastlow);
        tvForecastLow2 = findViewById(R.id.tv_forecastlow2);
        tvForecastLow3 = findViewById(R.id.tv_forecastlow3);
        tvForecastLow4 = findViewById(R.id.tv_forecastlow4);
        tvForecastLow5 = findViewById(R.id.tv_forecastlow5);
        tvForecastLow6 = findViewById(R.id.tv_forecastlow6);
        tvForecastLow7 = findViewById(R.id.tv_forecastlow7);
        tvForecastDay2 = findViewById(R.id.tv_forecastday2);
        tvForecastDay3 = findViewById(R.id.tv_forecastday3);
        tvForecastDay4 = findViewById(R.id.tv_forecastday4);
        tvForecastDay5 = findViewById(R.id.tv_forecastday5);
        tvForecastDay6 = findViewById(R.id.tv_forecastday6);
        tvForecastDay7 = findViewById(R.id.tv_forecastday7);

        lottieAnimationView = findViewById(R.id.lottieAnimationView);

        tvTime1 = findViewById(R.id.tv_hourlytime1);
        tvTime2 = findViewById(R.id.tv_hourlytime2);
        tvTime3 = findViewById(R.id.tv_hourlytime3);
        tvTime4 = findViewById(R.id.tv_hourlytime4);
        tvHourlyTemp1 = findViewById(R.id.tv_hourly1sttemp);
        tvHourlyTemp2 = findViewById(R.id.tv_hourly2ndtemp);
        tvHourlyTemp3 = findViewById(R.id.tv_hourly3rdtemp);
        tvHourlyTemp4 = findViewById(R.id.tv_hourly4thtemp);
        tvPerc1Rain = findViewById(R.id.tv_hourlyperc1rain);
        tvPerc2Rain = findViewById(R.id.tv_hourlyperc2rain);
        tvPerc3Rain = findViewById(R.id.tv_hourlyperc3rain);
        tvPerc4Rain = findViewById(R.id.tv_hourlyperc4rain);

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
        String url = "https://api.weather.com/v2/aggcommon/v3-location-point;v3-wx-observations-current;v2fcstdaily7s;v3-wx-forecast-hourly-1day;v3-links" +
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
                tvNarrative.setText(weatherResponse.fcstdaily7s.forecasts.get(0).narrative);

                if(Objects.equals(weatherResponse.current.dayornight, "N")){
                    if (weatherResponse.current.iconCode == 31){
                        lottieAnimationView.setAnimation(R.raw.clearnight);
                    }else if(weatherResponse.current.iconCode == 32){
                        lottieAnimationView.setAnimation(R.raw.clearnight);
                    }else if(weatherResponse.current.iconCode == 33){
                        lottieAnimationView.setAnimation(R.raw.clearnight);
                    }
                }else{
                    if (weatherResponse.current.iconCode == 31){
                        lottieAnimationView.setAnimation(R.raw.clear);
                    }
                }

                if (weatherResponse.fcstdaily7s.forecasts.get(0).num == 1) {
                    String maxTemp = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(0).max_temp);
                    String minTemp = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(0).min_temp);
                    tvForecastHigh.setText(maxTemp+"°");
                    tvForecastLow.setText(minTemp+"°");
                }
                if (weatherResponse.fcstdaily7s.forecasts.get(1).num == 2) {
                    String maxTemp2 = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(1).max_temp);
                    String minTemp2 = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(1).min_temp);
                    String day2 = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(1).day);
                    tvForecastHigh2.setText(maxTemp2+"°");
                    tvForecastLow2.setText(minTemp2+"°");
                    tvForecastDay2.setText(day2);
                } if (weatherResponse.fcstdaily7s.forecasts.get(2).num == 3) {
                    String maxTemp = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(2).max_temp);
                    String minTemp = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(2).min_temp);
                    String day3 = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(2).day);
                    tvForecastHigh3.setText(maxTemp+"°");
                    tvForecastLow3.setText(minTemp+"°");
                    tvForecastDay3.setText(day3);
                } if (weatherResponse.fcstdaily7s.forecasts.get(3).num == 4) {
                    String maxTemp = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(3).max_temp);
                    String minTemp = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(3).min_temp);
                    String day4 = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(3).day);
                    tvForecastHigh4.setText(maxTemp+"°");
                    tvForecastLow4.setText(minTemp+"°");
                    tvForecastDay4.setText(day4);
                } if (weatherResponse.fcstdaily7s.forecasts.get(4).num == 5) {
                    String maxTemp = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(4).max_temp);
                    String minTemp = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(4).min_temp);
                    String day5 = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(4).day);
                    tvForecastHigh5.setText(maxTemp+"°");
                    tvForecastLow5.setText(minTemp+"°");
                    tvForecastDay5.setText(day5);
                } if (weatherResponse.fcstdaily7s.forecasts.get(5).num == 6) {
                    String maxTemp = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(5).max_temp);
                    String minTemp = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(5).min_temp);
                    String day6 = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(5).day);
                    tvForecastHigh6.setText(maxTemp+"°");
                    tvForecastLow6.setText(minTemp+"°");
                    tvForecastDay6.setText(day6);
                } if (weatherResponse.fcstdaily7s.forecasts.get(6).num == 7) {
                    String maxTemp = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(6).max_temp);
                    String minTemp = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(6).min_temp);
                    String day7 = String.valueOf(weatherResponse.fcstdaily7s.forecasts.get(6).day);
                    tvForecastHigh7.setText(maxTemp+"°");
                    tvForecastLow7.setText(minTemp+"°");
                    tvForecastDay7.setText(day7);
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h a"); // Only hour and AM/PM

                // Correct DateTimeFormatter to parse the time including offset
                DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

                tvTime1.setText(formatTime(weatherResponse.hourly1day.validTimeLocal.get(0), fullFormatter, formatter));
                tvTime2.setText(formatTime(weatherResponse.hourly1day.validTimeLocal.get(1), fullFormatter, formatter));
                tvTime3.setText(formatTime(weatherResponse.hourly1day.validTimeLocal.get(2), fullFormatter, formatter));
                tvTime4.setText(formatTime(weatherResponse.hourly1day.validTimeLocal.get(3), fullFormatter, formatter));


                tvPerc1Rain.setText(weatherResponse.hourly1day.precipChance.get(0) +" %");
                tvPerc2Rain.setText(weatherResponse.hourly1day.precipChance.get(1) +" %");
                tvPerc3Rain.setText(weatherResponse.hourly1day.precipChance.get(2) +" %");
                tvPerc4Rain.setText(weatherResponse.hourly1day.precipChance.get(3) +" %");
                tvHourlyTemp1.setText(weatherResponse.hourly1day.temperature.get(0) +"°");
                tvHourlyTemp2.setText(weatherResponse.hourly1day.temperature.get(1) +"°");
                tvHourlyTemp3.setText(weatherResponse.hourly1day.temperature.get(2) +"°");
                tvHourlyTemp4.setText(weatherResponse.hourly1day.temperature.get(3) +"°");

            } else {
                tvTemp.setText("Error: Invalid response");
            }
        } catch (Exception e) {
            tvNarrative.setText(e.toString());
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
    private String formatTime(String time, DateTimeFormatter fullFormatter, DateTimeFormatter formatter) {
        OffsetDateTime dateTime = OffsetDateTime.parse(time, fullFormatter); // Parse using the full formatter
        return dateTime.format(formatter); // Format to only show hour with AM/PM
    }
}
