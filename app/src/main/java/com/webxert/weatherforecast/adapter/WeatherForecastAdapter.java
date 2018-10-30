package com.webxert.weatherforecast.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.webxert.weatherforecast.R;
import com.webxert.weatherforecast.common.Common;
import com.webxert.weatherforecast.model.WeatherForecastResult;

import java.util.ArrayList;

/**
 * Created by hp on 7/30/2018.
 */

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.MyViewHolder> {

    private WeatherForecastResult weatherForecastResult;
    Context context;

    public WeatherForecastAdapter(WeatherForecastResult weatherForecastResult) {
        this.weatherForecastResult = weatherForecastResult;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherForecastResult.list.get(position).weather.get(0).getIcon())
                .append(".png").toString()).into(holder.weatherImage);
        holder.date.setText(new StringBuilder(Common.convertUnitToDate(weatherForecastResult.list.get(position).getDt())));
        holder.description.setText(weatherForecastResult.list.get(position).weather.get(0).description);
        holder.temperature.setText(String.valueOf(weatherForecastResult.list.get(position).getMain().getTemp()) + " °C");
    }

    @Override
    public int getItemCount() {
        return weatherForecastResult.list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView description, date, temperature;
        ImageView weatherImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.description_forecast);
            date = itemView.findViewById(R.id.date_forecast);
            weatherImage = itemView.findViewById(R.id.weather_image);
            temperature = itemView.findViewById(R.id.weather_temp);

        }
    }
}
