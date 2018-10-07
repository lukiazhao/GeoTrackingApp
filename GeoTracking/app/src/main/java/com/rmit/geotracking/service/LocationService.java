package com.rmit.geotracking.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;

import android.support.annotation.Nullable;

import com.rmit.geotracking.model.Reachables;
import com.rmit.geotracking.controller.LocationMonitorListener;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.TrackingInfoProcessor;
import com.rmit.geotracking.notification.NotificationsGenerator;
import com.rmit.geotracking.utilities.AlarmGenerator;

import org.json.JSONException;

/*
*
* This class is a backgroud service that will process Http request, location request,
* json decoding, filter reachable trackings, find the closest trackable, etc
*
* */

public class LocationService extends IntentService {

    private Reachables reachableClass;
    private final String SUGGEST_INTENT_MESSAGE;

    public LocationService() {
        super("Location Service");
        reachableClass = Reachables.getSingletonInstance();
        SUGGEST_INTENT_MESSAGE = "suggest_now";
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        // check network connection
        if(!isNetworkConnected()){
            return;
        }

        //get current location
        Location currLocation = requestLocationUpdate();


        try {
            // filter reachables from all tracking information and set to a static Reachables class
            reachableClass.setReachables(TrackManager.getSingletonInstance(this).getAllReachables(currLocation));

            // suggest the closest reachable out of the filtered reachables
            TrackingInfoProcessor.Pair<Integer,Integer> closest = reachableClass.suggestClosestTrackable();

            // start the first notification
            NotificationsGenerator.getSingletonInstance(this).buildSuggestionNotification(closest);

            // if the incomming intent is sent from suggestnow or networkReceiver, then exit early (skip set alarm)
            if(intent != null && intent.getBooleanExtra(SUGGEST_INTENT_MESSAGE, false)){
                return;
            }

            // else if intents are sent from the other situations (yes, no, cancel, network receiver),
            // set another alarm with preferred polling time
            AlarmGenerator.getSingletonInstance(this).setAlarm();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Location requestLocationUpdate() {
        Location location = null;
        Location lastKnownLocation = null;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (locationManager != null) {
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationMonitorListener(), null);
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if(lastKnownLocation!= null) {
                location = lastKnownLocation;
            }
        }

        return location;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return (cm != null ? cm.getActiveNetworkInfo() : null) != null;
    }

}
