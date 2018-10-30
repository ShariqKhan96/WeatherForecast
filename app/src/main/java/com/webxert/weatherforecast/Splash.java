package com.webxert.weatherforecast;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.ahmadnemati.wind.WindView;
import com.github.ahmadnemati.wind.enums.TrendType;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.webxert.weatherforecast.adapter.WeatherForecastAdapter;
import com.webxert.weatherforecast.common.Common;
import com.webxert.weatherforecast.model.WeatherForecastResult;
import com.webxert.weatherforecast.model.WeatherResult;
import com.webxert.weatherforecast.retrofit.IOpenWeather;
import com.webxert.weatherforecast.retrofit.RetrofitClient;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Splash extends AppCompatActivity {

    WindView progressBar;

    CompositeDisposable compositeDisposable;
    IOpenWeather service;

    RelativeLayout relativeLayout;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        relativeLayout = findViewById(R.id.main_container);
        progressBar = findViewById(R.id.progress_bar);


        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        service = retrofit.create(IOpenWeather.class);


        Dexter.withActivity(this).withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            progressBar.setTrendType(TrendType.UP);
                            progressBar.setWindSpeed(20.0f);
                            progressBar.start();
                            buildLocationRequest();
                            buildLocationCallback();


                            if (ActivityCompat.checkSelfPermission(Splash.this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(Splash.this,
                                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            //   Log.e("InsideDexter", "Hello");
                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Splash.this);
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());


                            //LocationLogging


                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        Snackbar.make(relativeLayout, "Permissions Denied", Snackbar.LENGTH_SHORT).show();
                    }
                }).check();


    }

    private void getWeatherInformation() {

        compositeDisposable.add(service.getWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APPID,
                "metric")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<WeatherResult>() {
                            @Override
                            public void accept(WeatherResult weatherResult) throws Exception {
                                Common.weatherResult = weatherResult;
//                        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
//                                .append(weatherResult.getWeather().get(0).getIcon())
//                                .append(".png").toString()).into(cityImage);
//
//                        cityName.setText(weatherResult.getName());
//                        description.setText(new StringBuilder("Weather in ")
//                                .append(weatherResult.getName()));
//                        temperature.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append(" °C"));
//                        date_time.setText(Common.convertUnitToDate(weatherResult.getDt()));
//                        pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append(" hpa").toString());
//                        humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append(" %").toString());
//                        sunrise.setText(Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
//                        sunset.setText(Common.convertUnixToHour(weatherResult.getSys().getSunset()));
//                        coords.setText(new StringBuilder("[" + weatherResult.getCoord().getLat() + "," + weatherResult.getCoord().getLon() + "]").toString());
//
//                        linearLayout.setVisibility(View.VISIBLE);
//                        loading.setVisibility(View.GONE);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                                //  Snackbar.make(linearLayout, "Something Went Wrong", Snackbar.LENGTH_SHORT).show();

                                Log.e("ExceptionFromApi", throwable.getLocalizedMessage());
                                Toast.makeText(Splash.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
        );
    }

    private void getForecastResult() {
        compositeDisposable.add(service.getForecastWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APPID,
                "metric")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<WeatherForecastResult>() {
                            @Override
                            public void accept(WeatherForecastResult weatherForecastResult) throws Exception {

                                Common.weatherForecastResult = weatherForecastResult;

                                progressBar.stop();
                                Intent intent = new Intent(Splash.this, MainActivity.class);
                                startActivity(intent);
                                finish();
//                        cityName.setText(weatherForecastResult.city.getName());
//                        coord.setText("[" + weatherForecastResult.city.getCoord().getLat() + "," + weatherForecastResult.getCity().getCoord().getLon() + "]");
//                        WeatherForecastAdapter adapter = new WeatherForecastAdapter(weatherForecastResult);
//                        forecastRecyclerView.setAdapter(adapter);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(Splash.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
        );
    }


    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10.0f);


    }

    private void buildLocationCallback() {

        Log.e("callback", "callback");

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Toast.makeText(Splash.this, "" + locationResult.getLastLocation().getLatitude() + "/" + locationResult.getLastLocation().getLongitude(), Toast.LENGTH_SHORT).show();

                Common.current_location = locationResult.getLastLocation();

                getWeatherInformation();
                getForecastResult();


            }


        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}
