package com.rmit.geotracking.view;

import android.app.Application;

import com.rmit.geotracking.utilities.AlarmGenerator;


public class StartUp extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        System.out.println("Start up");

        // set first alarm
        AlarmGenerator.getSingletonInstance(this).setAlarm();

    }
}
