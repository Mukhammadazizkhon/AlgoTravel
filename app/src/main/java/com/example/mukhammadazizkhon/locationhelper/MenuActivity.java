package com.example.mukhammadazizkhon.locationhelper;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class MenuActivity extends AppCompatActivity
{
    private CardView restaurants, directions;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        restaurants = (CardView)findViewById(R.id.restaurants);
        directions = (CardView)findViewById(R.id.directions);
        restaurants.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent restaurantsIntent = new Intent(MenuActivity.this, RestaurantsActivity.class);
                startActivity(restaurantsIntent);
            }
        });
        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent directionsIntent = new Intent(MenuActivity.this, MapsActivity.class);
                startActivity(directionsIntent);
            }
        });
    }
}
