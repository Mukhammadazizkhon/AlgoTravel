package com.example.mukhammadazizkhon.locationhelper.Common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mukha on 9/27/2018.
 */

public class Common {
    public static final String API_ID = "aa08263c81bcda29c4443ef227743c15";
    public static Location currentLocation = null;

    public static String convertUnixToDate(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd EEE MM yyyy");
        String formattedDate = simpleDateFormat.format(date);
        return formattedDate;
    }

    public static String convertUnixToHour(long sunrise) {
        Date date = new Date(sunrise*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String formattedDate = simpleDateFormat.format(date);
        return formattedDate;
    }
}
