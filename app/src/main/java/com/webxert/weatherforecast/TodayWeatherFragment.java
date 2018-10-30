package com.webxert.weatherforecast;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.webxert.weatherforecast.common.Common;
import com.webxert.weatherforecast.retrofit.IOpenWeather;

import io.reactivex.disposables.CompositeDisposable;


/**
 * A simple {@link Fragment} subclass.
 */


public class TodayWeatherFragment extends Fragment {

    static TodayWeatherFragment instance;

    TextView cityName, pressure, humidity, sunset, sunrise, coords, description, date_time, wind, temperature;
    ImageView cityImage;
    LinearLayout linearLayout;
    ProgressBar loading;

    public static TodayWeatherFragment getInstance() {
        if (instance == null)
            instance = new TodayWeatherFragment();

        return instance;
    }

    public TodayWeatherFragment() {
//        compositeDisposable = new CompositeDisposable();
//        Retrofit retrofit = RetrofitClient.getInstance();
//        service = retrofit.create(IOpenWeather.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today_weather, container, false);
        cityImage = view.findViewById(R.id.weather_image);
        loading = view.findViewById(R.id.progress_bar);
        linearLayout = view.findViewById(R.id.weather_panel);

        date_time = view.findViewById(R.id.weather_date_time);
        description = view.findViewById(R.id.weather_description);
        sunrise = view.findViewById(R.id.sunrise);
        wind = view.findViewById(R.id.speed);
        sunset = view.findViewById(R.id.sunset);
        pressure = view.findViewById(R.id.pressure);
        humidity = view.findViewById(R.id.humidity);
        cityName = view.findViewById(R.id.city_name);
        coords = view.findViewById(R.id.coords);
        temperature = view.findViewById(R.id.weather_temp);

        setWeatherImformation();

        return view;

    }

    private void setWeatherImformation() {

        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(Common.weatherResult.getWeather().get(0).getIcon())
                .append(".png").toString()).into(cityImage);

        cityName.setText(Common.weatherResult.getName());
        description.setText(new StringBuilder("Weather in ")
                .append(Common.weatherResult.getName()));
        temperature.setText(new StringBuilder(String.valueOf(Common.weatherResult.getMain().getTemp())).append(" °C"));
        date_time.setText(Common.convertUnitToDate(Common.weatherResult.getDt()));
        pressure.setText(new StringBuilder(String.valueOf(Common.weatherResult.getMain().getPressure())).append(" hpa").toString());
        humidity.setText(new StringBuilder(String.valueOf(Common.weatherResult.getMain().getHumidity())).append(" %").toString());
        sunrise.setText(Common.convertUnixToHour(Common.weatherResult.getSys().getSunrise()));
        sunset.setText(Common.convertUnixToHour(Common.weatherResult.getSys().getSunset()));
        coords.setText(new StringBuilder("[" + Common.weatherResult.getCoord().getLat() + "," + Common.weatherResult.getCoord().getLon() + "]").toString());

        linearLayout.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);

    }

}
