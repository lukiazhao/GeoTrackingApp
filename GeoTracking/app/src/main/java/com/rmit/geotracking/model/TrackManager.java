package com.rmit.geotracking.model;

import android.content.Context;

import com.rmit.geotracking.R;
import com.rmit.geotracking.service.TrackingService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class TrackManager extends Observable {
    // search and filter
    // load and save
//    public static TrackManager INSTANCE = null;
    private String LOG_TAG = this.getClass().getName();
    private Map<Integer, Trackable> trackableMap;
    private Map<String, Tracking> trackingMap;
    private static Context context;
    private TrackingManager trackingManager;
    private TrackingInfoProcessor processor;
    //if use singleton, change this to private !!!!
    private TrackManager(){
        this.trackableMap = loadTrackable();
        this.trackingMap = new HashMap<String, Tracking>();
        this.trackingManager = new TrackingManager(trackingMap);
        this.processor = new TrackingInfoProcessor(context);
    }

    // singleton support
    private static class LazyHolder
    {
        static final TrackManager INSTANCE = new TrackManager();
    }

//    // singleton
    public static TrackManager getSingletonInstance(Context context)
    {
        TrackManager.context = context;
        return LazyHolder.INSTANCE;
    }



    public Map<Integer, Trackable> getTrackableMap(){
        return this.trackableMap;
    }
    public Map<String, Tracking> getTrackingMap() { return this.trackingMap; }

    public Map<Integer, Trackable> loadTrackable(){
        Map<Integer,Trackable> trackablesMap = new HashMap<>();

        InputStream inputStream = this.context.getResources().openRawResource(R.raw.food_truck_data);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        try {

            while ((line = bufferedReader.readLine()) != null) {
                Trackable trackable;
                String[] arrOfElement = line.split(",\"");

                int id = Integer.parseInt(arrOfElement[0]);
                String name = arrOfElement[1].replaceAll("\"","");
                String desc = arrOfElement[2].replaceAll("\"","");
                String url = arrOfElement[3].replaceAll("\"","");
                String category = arrOfElement[4].replaceAll("\"","");

                if (arrOfElement.length == 5) {
                    trackable = new SimpleTrackable(id, name, desc, url, category);
                } else {
                    trackable = new SimpleTrackable(id, name, desc, url, category, arrOfElement[5].replaceAll("\"",""));
                }

                trackablesMap.put(id, trackable);
            }

        } catch (Exception e) {

        }
        return trackablesMap;

    }



    public TrackingManager getTrackingManager() {
        return trackingManager;
    }
    public TrackingInfoProcessor getTrackingInfoProcessor() {
        return processor;
    }

    public List<String> getCategory(){
        List<String> category = new ArrayList<>();
        category.add("Select Category");
        for (Trackable trackable: trackableMap.values()){
            if(!category.contains(trackable.getCategory())){
                category.add(trackable.getCategory());
            }
        }
        System.out.println("category size: " + category.size());
        return category;
    }



}
