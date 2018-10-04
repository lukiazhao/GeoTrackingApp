package com.rmit.geotracking.controller;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.service.LocationService;

import org.json.JSONException;

import java.util.Map;

public class LocationMonitorListener implements LocationListener {
    private final String LOG_TAG = LocationMonitorListener.class.getName();

    private LocationService context;
    private TrackManager manager;
    public LocationMonitorListener(LocationService context){
        this.context = context;
        manager = TrackManager.getSingletonInstance(context);
        System.out.println("Location Service listener constructor" );
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOG_TAG, "LAT = " + location.getLatitude() + " Long=" + location.getLongitude());

//        try {
//
//            Map<Integer, Integer> allReachables = manager.getAllReachables(location);
//            Log.i(LOG_TAG, allReachables.size()+"");
//            for (Integer reac: allReachables.keySet()){
//                Log.i(LOG_TAG, "trackable id =" +reac + "; time taken to be there=" + allReachables.get(reac));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
