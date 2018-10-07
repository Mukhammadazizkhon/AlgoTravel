package com.example.mukhammadazizkhon.locationhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    sleep(5000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent menuIntent = new Intent(WelcomeActivity.this, MenuActivity.class);
                    startActivity(menuIntent);
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        finish();
    }
}
