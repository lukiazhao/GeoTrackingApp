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


    public LocationMonitorListener(){

    }


    @Override
    public void onLocationChanged(Location location) {

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
