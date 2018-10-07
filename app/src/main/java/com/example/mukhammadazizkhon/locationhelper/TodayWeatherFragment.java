package com.example.mukhammadazizkhon.locationhelper;


import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mukhammadazizkhon.locationhelper.Common.Common;
import com.example.mukhammadazizkhon.locationhelper.Model.WeatherResult;
import com.example.mukhammadazizkhon.locationhelper.Retrofit.IOpenWeatherMap;
import com.example.mukhammadazizkhon.locationhelper.Retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends Fragment {
    ImageView imageWeather;
    TextView textCity, textHumidity, textSunrise, textSunset, textPresure,
            textTemperature, textDescription, textDateTime, textWind,
            textGeoCoordinate;
    LinearLayout weatherPanel;
    ProgressBar loadingBar;

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mapService;


    static TodayWeatherFragment instance;

    public static TodayWeatherFragment getInstance(){
        if(instance == null){
            instance = new TodayWeatherFragment();
        }
        return instance;
    }


    public TodayWeatherFragment() {
        // Required empty public constructor
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mapService = retrofit.create(IOpenWeatherMap.class);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_today_weather, container, false);
        imageWeather = (ImageView)itemView.findViewById(R.id.imageWeather);
        textCity = (TextView)itemView.findViewById(R.id.textCityName);
        textHumidity = (TextView)itemView.findViewById(R.id.textHumidity);
        textSunrise = (TextView)itemView.findViewById(R.id.textSunrise);
        textSunset = (TextView)itemView.findViewById(R.id.textSunset);
        textPresure = (TextView)itemView.findViewById(R.id.textPressure);
        textTemperature = (TextView)itemView.findViewById(R.id.textTemperature);
        textDescription = (TextView)itemView.findViewById(R.id.textDesciption);
        textDateTime = (TextView)itemView.findViewById(R.id.textDateTime);
        textGeoCoordinate = (TextView)itemView.findViewById(R.id.textGeoCoords);
        textWind = (TextView)itemView.findViewById(R.id.textWind);

        weatherPanel = (LinearLayout)itemView.findViewById(R.id.weatherPanel);
        loadingBar = (ProgressBar)itemView.findViewById(R.id.loadingBar);
        getWeatherInformation();
        return itemView;
    }


    private void getWeatherInformation() {
        compositeDisposable.add(mapService.getWeatherByLatitudeAndLongitude(String.valueOf(Common.currentLocation.getLatitude()),
                String.valueOf(Common.currentLocation.getLongitude()),Common.API_ID, "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {
                        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                                .append(weatherResult.getWeather().get(0).getIcon()).append(".png").toString())
                                .into(imageWeather);
                        textCity.setText(weatherResult.getName());
                        textDescription.setText(new StringBuilder("Weather in ")
                                .append(weatherResult.getName()).toString());
                        textTemperature.setText(new StringBuilder(String.valueOf(weatherResult
                                .getMain().getTemp())).append("°C").toString());
                        textDateTime.setText(Common.convertUnixToDate(weatherResult.getDt()));
                        textPresure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure()))
                                .append(" hpa").toString());
                        textHumidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity()))
                                .append(" %").toString());
                        textSunrise.setText(Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
                        textSunset.setText(Common.convertUnixToHour(weatherResult.getSys().getSunset()));
                        textGeoCoordinate.setText(new StringBuilder(weatherResult.getCoord().toString()).toString());

                        // Display panel
                        weatherPanel.setVisibility(View.VISIBLE);
                        loadingBar.setVisibility(View.GONE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ));
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
