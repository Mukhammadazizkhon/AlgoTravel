package com.example.mukhammadazizkhon.locationhelper.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mukhammadazizkhon.locationhelper.Common.Common;
import com.example.mukhammadazizkhon.locationhelper.Model.WeatherForecastResult;
import com.example.mukhammadazizkhon.locationhelper.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Mukha on 9/30/2018.
 */

public class WeatherForecastAdapter
        extends RecyclerView.Adapter<WeatherForecastAdapter.MyViewHolder>{
    Context context;
    WeatherForecastResult weatherForecastResult;


    public WeatherForecastAdapter(Context context, WeatherForecastResult weatherForecastResult){
        this.context = context;
        this.weatherForecastResult = weatherForecastResult;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.weather_forcast_items,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Load icon
        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherForecastResult.list.get(position).weather.get(0).getIcon())
                .append(".png").toString()).into(holder.imageWeather);

        holder.textDateAndTime.setText(new StringBuilder(Common.convertUnixToDate(weatherForecastResult
                .list.get(position).dt)));
        holder.textDescription.setText(new StringBuilder(weatherForecastResult
                .list.get(position).weather.get(0).getDescription()));
        holder.textTemperature.setText(new StringBuilder(String.valueOf(weatherForecastResult
                .list.get(position).main.getTemp())).append(" °C"));
    }

    @Override
    public int getItemCount() {
        return weatherForecastResult.list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textDateAndTime,textDescription,textTemperature;
        ImageView imageWeather;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageWeather = (ImageView)itemView.findViewById(R.id.image_Weather);
            textDateAndTime = (TextView)itemView.findViewById(R.id.text_Date);
            textDescription = (TextView)itemView.findViewById(R.id.text_Desciption);
            textTemperature = (TextView)itemView.findViewById(R.id.textTemperature);
        }
    }
}
