package com.webxert.weatherforecast;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.label305.asynctask.SimpleAsyncTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.webxert.weatherforecast.common.Common;
import com.webxert.weatherforecast.model.WeatherResult;
import com.webxert.weatherforecast.retrofit.IOpenWeather;
import com.webxert.weatherforecast.retrofit.RetrofitClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityFragment extends Fragment {

    public List<String> cityNames;
    MaterialSearchBar searchBar;

    TextView cityName, pressure, humidity, sunset, sunrise, coords, description, date_time, wind, temperature;
    ImageView cityImage;
    LinearLayout linearLayout;
    ProgressBar loading;

    CompositeDisposable compositeDisposable;
    IOpenWeather service;


    public static CityFragment instance = null;

    public static CityFragment getInstance() {
        if (instance == null)
            instance = new CityFragment();
        return instance;

    }

    public CityFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        service = retrofit.create(IOpenWeather.class);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city, container, false);
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

        searchBar = view.findViewById(R.id.searchBar);
        searchBar.setEnabled(false);

        new LoadCities().execute();


        return view;

    }


    private class LoadCities extends SimpleAsyncTask<List<String>> {
        @Override
        protected List<String> doInBackgroundSimple() {
            cityNames = new ArrayList<>();

            try {
                StringBuilder builder = new StringBuilder();
                InputStream is = getResources().openRawResource(R.raw.city_list);
                GZIPInputStream gzipInputStream = new GZIPInputStream(is);

                InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String readed;

                while ((readed = bufferedReader.readLine()) != null)
                    builder.append(readed);
                cityNames = new Gson().fromJson(builder.toString(), new TypeToken<List<String>>() {
                }.getType());


            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }

            return cityNames;
        }

        @Override
        protected void onSuccess(final List<String> listCity) {
            super.onSuccess(listCity);

            searchBar.setEnabled(true);
            searchBar.addTextChangeListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    List<String> suggest = new ArrayList<>();
                    for (String suggestWord : suggest) {

                        if (suggestWord.toLowerCase().contains(searchBar.getText().toLowerCase())) {
                            suggest.add(suggestWord);

                        }
                        searchBar.setLastSuggestions(suggest);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                @Override
                public void onSearchStateChanged(boolean enabled) {

                }

                @Override
                public void onSearchConfirmed(CharSequence text) {

                    getWeatherInformation(text.toString());
                    searchBar.setLastSuggestions(listCity);

                }

                @Override
                public void onButtonClicked(int buttonCode) {

                }
            });

            searchBar.setLastSuggestions(listCity);

            loading.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);

        }
    }

    private void getWeatherInformation(String city) {

        compositeDisposable.add(service.getWeatherByCity(city, getString(R.string.app_key), "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {

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
                }));
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
