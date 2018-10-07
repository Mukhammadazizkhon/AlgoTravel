package com.example.mukhammadazizkhon.locationhelper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener
{
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }

    private static final String TAG = "ERROR";
    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;
    private static final float DEFAULT_ZOOM = 200f;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40,-168), new LatLng(71,136));
    ArrayList<LatLng> listPoints;

    // widgets
    private AutoCompleteTextView mSearchText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listPoints = new ArrayList<>();
        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }


    public class TaskRequestDirections extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {
            String responseString = "";
            try {
                responseString = requestDirection(params[0]);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }

    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>>
    {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... params)
        {
            JSONObject jSonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try
            {
                jSonObject = new JSONObject(params[0]);
                DirectionsParser directionParser = new DirectionsParser();
                routes = directionParser.parse(jSonObject);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists)
        {
            // Get list route and display it into the map
            ArrayList points = null;
            PolylineOptions polyLineOptions = null;
            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polyLineOptions = new PolylineOptions();
                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat, lon));
                }
                polyLineOptions.addAll(points);
                polyLineOptions.width(15);
                polyLineOptions.color(Color.BLUE);
                polyLineOptions.geodesic(true);
            }
            if (polyLineOptions != null) {
                mMap.addPolyline(polyLineOptions);
            } else
            {
                Toast.makeText(getApplicationContext(), "Direction not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        init();
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                // Reset marker when already 2
                if (listPoints.size() == 2) {
                    listPoints.clear();
                    mMap.clear();
                }
                listPoints.add(latLng);
                // Create marker
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                if (listPoints.size() == 1) {
                    // Add the first marker to the map
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                } else {
                    // Add the second marker to the map
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }
                mMap.addMarker(markerOptions);
                if (listPoints.size() == 2) {
                    // Create URL to get request from first marker to the second marker
                    String url = getRequestURL(listPoints.get(0), listPoints.get(1));
                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                    taskRequestDirections.execute(url);
                }

            }
        });

    }

    private String getRequestURL(LatLng origin, LatLng destination)
    {
        // Value of origin
        String str_org = "origin=" + origin.latitude + "," + origin.longitude;
        // Value of destination
        String str_dest = "destination=" + destination.latitude + "," + destination.longitude;
        // Set value enable the sensor
        String sensor = "sensor=false";
        // Mode for finding the direction
        String mode = "mode=driving";
        // Build the full parameter
        String param = str_org + "&" + str_dest + "&" + sensor + "&" + mode;
        // Output format
        String output = "json";
        // Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException
    {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpUrlConnection = null;
        try {
            URL url = new URL(reqUrl);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.connect();
            // Get the response result
            inputStream = httpUrlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpUrlConnection.disconnect();
        }
        return responseString;
    }

    private void init()
    {
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    // executing our method for searching
                    geoLocate();
                }
                return false;
            }
        });
    }

    private void geoLocate()
    {
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> listAddress = new ArrayList<>();
        try
        {
            listAddress = geocoder.getFromLocationName(searchString, 1);

        }
        catch (IOException e)
        {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }
        if (listAddress.size() > 0) {
            Address address = listAddress.get(0);
            Log.d(TAG, "geoLocate: found a location" + address.toString());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, "My location");
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
        mMap.addMarker(markerOptions);
    }
}
