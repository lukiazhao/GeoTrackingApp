package com.rmit.geotracking.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.rmit.geotracking.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class TrackManager {
    // search and filter
    // load and save
//    public static TrackManager INSTANCE = null;
    private Map<Integer, Trackable> trackableMap;
    private Map<String, Tracking> trackingMap;
    private static Context context;

    //if use singleton, change this to private !!!!
    public TrackManager(){
        this.trackableMap = loadTrackable();
    }

//    // singleton support
    private static class LazyHolder
    {
        static final TrackManager INSTANCE = new TrackManager();
    }
//
//    // PUBLIC METHODS
//
//    // singleton
//    // thread safe lazy initialisation: see https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
    public static TrackManager getSingletonInstance(Context context)
    {
        TrackManager.context = context;
        return LazyHolder.INSTANCE;
    }


//    public static TrackManager getINSTANCE(Context context){
//        if (INSTANCE == null){
//            INSTANCE = new TrackManager(context);
//        }
//        return INSTANCE;
//    }


    public Map<Integer, Trackable> getTrackableMap(){
        return this.trackableMap;
    }

    public Map<Integer, Trackable> loadTrackable(){
        Map<Integer,Trackable> trackablesMap = new HashMap<>();

        InputStream inputStream = this.context.getResources().openRawResource(R.raw.food_truck_data);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        /***** JAVA BASED DEPENDENCIES*****/
//        String file = "res/raw/food_truck_data.txt"; // res/raw/test.txt also work.
//        InputStream inputStream = INSTANCE.getClass().getClassLoader().getResourceAsStream(file);
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));


        String line;
        try {

            while ((line = bufferedReader.readLine()) != null) {
                Trackable trackable;
                String[] arrOfElement = line.split(",");
                int id = Integer.parseInt(arrOfElement[0]);
                String name = arrOfElement[1];
                String desc = arrOfElement[2];
                String url = arrOfElement[3];
                String category = arrOfElement[4];

                if (arrOfElement.length == 5) {
                    trackable = new SimpleTrackable(id, name, desc, url, category);
                } else {
                    trackable = new SimpleTrackable(id, name, desc, url, category, arrOfElement[5]);
                }

                trackablesMap.put(id, trackable);
            }

        } catch (Exception e) {

        }
        return trackablesMap;

    }
}
