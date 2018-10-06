package com.rmit.geotracking.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.rmit.geotracking.model.Reachables;
import com.rmit.geotracking.controller.LocationMonitorListener;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.TrackingInfoProcessor;
import com.rmit.geotracking.notification.NotificationsGenerator;
import com.rmit.geotracking.utilities.AlarmGenerator;

import org.json.JSONException;

public class LocationService extends IntentService {

    private final String LOG_TAG = LocationService.class.getName();
    private Handler handler;

    public LocationService() {
        super("Location Service");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        handler = new Handler(Looper.getMainLooper());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        // check network connection
        if(!isNetworkConnected()){

            Log.i(LOG_TAG, "INTERNET IS NOT CONNECTED");
            handler.post(() -> Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_LONG).show());
            return;
        }



        //get gps location - LocationManagere
        Location currLocation = requestLocationUpdate();



        Reachables reachableClass = Reachables.getSingletonInstance();

        try {

            // set all current reachables to Reachables Class
            reachableClass.setReachables(TrackManager.getSingletonInstance(this).getAllReachables(currLocation));


            TrackingInfoProcessor.Pair<Integer,Integer> closest = reachableClass.suggestClosestTrackable();


            // start the first notification
            NotificationsGenerator.getSingletonInstance(this).buildSuggestionNotification(closest);

            // if only suggest once
            if(intent != null && intent.getBooleanExtra("suggest_now", false)){
                return;
            }

            AlarmGenerator.getSingletonInstance(this).setAlarm();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private Location requestLocationUpdate() {
        Location location = null;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i(LOG_TAG, "listner?");
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationMonitorListener(this), null);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation!= null) {
                location = lastKnownLocation;
                Log.i(LOG_TAG, "CURRENT LOCATION="+ location.getLatitude()+","+location.getLongitude());
            }
        }
        return location;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

}
