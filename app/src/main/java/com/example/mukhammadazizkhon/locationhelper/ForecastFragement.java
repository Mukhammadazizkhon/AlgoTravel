package com.example.mukhammadazizkhon.locationhelper;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mukhammadazizkhon.locationhelper.Adapter.WeatherForecastAdapter;
import com.example.mukhammadazizkhon.locationhelper.Common.Common;
import com.example.mukhammadazizkhon.locationhelper.Model.WeatherForecastResult;
import com.example.mukhammadazizkhon.locationhelper.Retrofit.IOpenWeatherMap;
import com.example.mukhammadazizkhon.locationhelper.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragement extends Fragment {

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mForecastService;

    TextView textCityName,textGeoCoord;
    RecyclerView forecastRecycler;



    static ForecastFragement instance;

    public static ForecastFragement getInstance(){
        if(instance == null){
            instance = new ForecastFragement();
        }
        return instance;
    }

    public ForecastFragement() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mForecastService = retrofit.create(IOpenWeatherMap.class);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_forecast_fragement, container, false);
        textCityName = (TextView)itemView.findViewById(R.id.textCityName);
        textGeoCoord = (TextView)itemView.findViewById(R.id.textGeoCoords);
        forecastRecycler = (RecyclerView)itemView.findViewById(R.id.recyclerForcast);
        forecastRecycler.setHasFixedSize(true);
        forecastRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        getForecastedWeatherDetails();
        return itemView;
    }

    private void getForecastedWeatherDetails() {
        compositeDisposable.add(mForecastService.getWeatherForecastByLatitudeAndLongitude(
                String.valueOf(Common.currentLocation.getLatitude()),
                String.valueOf(Common.currentLocation.getLongitude()),
                Common.API_ID, "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResult>() {
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult)
                            throws Exception {
                        displayForecastWeather(weatherForecastResult);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("ERROR", ""+ throwable.getMessage());
                    }
                })
        );
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    private void displayForecastWeather(WeatherForecastResult weatherForecastResult) {
        textCityName.setText(new StringBuilder(weatherForecastResult.city.name));
        textGeoCoord.setText(new StringBuilder(weatherForecastResult.city.coord.toString()));
        WeatherForecastAdapter adapter = new WeatherForecastAdapter(getContext(), weatherForecastResult);
        forecastRecycler.setAdapter(adapter);
    }
}
