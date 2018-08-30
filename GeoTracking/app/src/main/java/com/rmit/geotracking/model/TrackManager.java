package com.rmit.geotracking.model;

import android.annotation.SuppressLint;
import android.content.Context;

import com.rmit.geotracking.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class TrackManager extends Observable {

    private Map<Integer, Trackable> trackableMap;
    private Map<String, Tracking> trackingMap;
    private List<Integer> filteredTrackableIds;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private TrackingManager trackingManager;
    private TrackingInfoProcessor processor;

    //use singleton, private constructor
    private TrackManager(){
        this.trackableMap = loadTrackable();
        this.trackingMap = new HashMap<>();
        this.trackingManager = new TrackingManager(trackingMap);
        this.processor = new TrackingInfoProcessor(context);
    }

    // singleton support
    private static class LazyHolder
    {
        @SuppressLint("StaticFieldLeak")
        static final TrackManager INSTANCE = new TrackManager();
    }

    // singleton
    public static TrackManager getSingletonInstance(Context context)
    {
        TrackManager.context = context;
        return LazyHolder.INSTANCE;
    }

    // getter
    public Map<Integer, Trackable> getTrackableMap(){
        return this.trackableMap;
    }
    public Map<String, Tracking> getTrackingMap() { return this.trackingMap; }
    public TrackingManager getTrackingManager() {
        return trackingManager;
    }
    public TrackingInfoProcessor getTrackingInfoProcessor() {
        return processor;
    }


    // load Trackables from txt file
    private Map<Integer, Trackable> loadTrackable(){
        @SuppressLint("UseSparseArrays")
        Map<Integer,Trackable> trackablesMap = new HashMap<>();

        InputStream inputStream = context.getResources().openRawResource(R.raw.food_truck_data);
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
            e.printStackTrace();
        }
        return trackablesMap;
    }

    // read all category types from original file
    public List<String> readAllCategories(){
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

    // filter trackable list
    public void setfilteredTrackable(String category) {
        List<Integer> selected = new ArrayList<>();
        if(category == null) {
            selected.addAll(trackableMap.keySet());
        } else {
            for(Trackable trackable: trackableMap.values()){
                if(trackable.getCategory().equals(category)){
                    selected.add(trackable.getId());
                }
            }
        }
        this.filteredTrackableIds = selected;

        setChanged();
        notifyObservers();
    }

    public List<Integer> getFilteredTrackableIds() {
        return this.filteredTrackableIds;
    }
}
