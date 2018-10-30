package com.webxert.weatherforecast;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.webxert.weatherforecast.adapter.WeatherForecastAdapter;
import com.webxert.weatherforecast.common.Common;
import com.webxert.weatherforecast.model.WeatherForecastResult;
import com.webxert.weatherforecast.retrofit.IOpenWeather;
import com.webxert.weatherforecast.retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.webxert.weatherforecast.common.Common.weatherForecastResult;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {

    CompositeDisposable compositeDisposable;
    IOpenWeather service;
    TextView cityName;
    TextView coord;
    RecyclerView forecastRecyclerView;


    public static ForecastFragment instance;


    public static ForecastFragment getInstance() {
        if (instance == null)
            instance = new ForecastFragment();
        return instance;
    }


    public ForecastFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        service = retrofit.create(IOpenWeather.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        cityName = view.findViewById(R.id.city_name);
        coord = view.findViewById(R.id.coords_forecast);
        forecastRecyclerView = view.findViewById(R.id.forecast_recyclerview);
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        setForecastResult();


        return view;
    }

    private void setForecastResult() {

        cityName.setText(weatherForecastResult.city.getName());
        coord.setText("[" + weatherForecastResult.city.getCoord().getLat() + "," + Common.weatherForecastResult.getCity().getCoord().getLon() + "]");
        WeatherForecastAdapter adapter = new WeatherForecastAdapter(Common.weatherForecastResult);
        forecastRecyclerView.setAdapter(adapter);

    }

}
