package com.rmit.geotracking.service;

import android.Manifest;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.rmit.geotracking.R;
import com.rmit.geotracking.controller.LocationMonitorListener;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Trackable;
import com.rmit.geotracking.model.TrackingInfoProcessor;
import com.rmit.geotracking.notification.NotificationsGenerator;

import org.json.JSONException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationService extends IntentService {

    private final String LOG_TAG = LocationService.class.getName();
    private TrackManager manager;

    private List<TrackingInfoProcessor.Pair<Integer, Integer>> allReachables;

    public LocationService() {
        super("Location Service");
        Log.i(LOG_TAG, "lOCATION SERVICE CONSTRUCTOR FIRST?");
        manager = TrackManager.getSingletonInstance(LocationService.this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //get gps location - LocationManagere
        Location currLocation = requestLocationUpdate();

            try {
                allReachables = manager.getAllReachables(currLocation);


                Log.i(LOG_TAG, allReachables.size() + "");
                for (TrackingInfoProcessor.Pair p : allReachables) {
                    Log.i(LOG_TAG, "trackable id =" + p.getFirstAttribute() + "; time taken to be there=" + p.getSecondAttribute());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // filter closest trackable

            NotificationsGenerator.getSingletonInstance(this).buildSuggestionNotification(1);
            //update polling time

            // schedule next alarm
//        Log.i(LOG_TAG, "INTENT = " + intent.getBooleanExtra("CancelSuggestion", false));
//
//        if(intent.getBooleanExtra("CancelSuggestion", false)){
//
//            Log.i(LOG_TAG, "Cancel suggestion");
//
//        } else {
//            Log.i(LOG_TAG, "NOT canceled, set next alarm");
//            setNextAlarm();
//        }

    }

    public TrackingInfoProcessor.Pair suggestClosest(Location currLocation){

        TrackingInfoProcessor.Pair closest = null;
        try {

            List<TrackingInfoProcessor.Pair<Integer, Integer>> allReachables = manager.getAllReachables(currLocation);


            // find shortest duration pair(trackableId, duration)
            int smallestDuration = Integer.MAX_VALUE;
            for(TrackingInfoProcessor.Pair<Integer, Integer> p: allReachables){
                if(p.getSecondAttribute() < smallestDuration){
                    closest = p;
                    smallestDuration = p.getSecondAttribute();
                }
            }


            Log.i(LOG_TAG, allReachables.size() + "");
            for (TrackingInfoProcessor.Pair p: allReachables){
                Log.i(LOG_TAG, "trackable id =" + p.getFirstAttribute() + "; time taken to be there=" + p.getSecondAttribute());
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return closest;
    }


    public void removeSuggestion(){

    }



    public void setNextAlarm(){

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + 10);      // polling time

        Intent myintent = new Intent(LocationService.this, LocationService.class);

        PendingIntent pendingIntent = PendingIntent.getService
                (LocationService.this, 0, myintent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

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

}
