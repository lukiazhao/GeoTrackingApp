package com.rmit.geotracking.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import com.rmit.geotracking.R;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * This class stores all data including trackable map, tracking map and trackable ID list.
 * Use singleton to help access the data from controller without keeping the reference.
 * All methods related to filter are included in this class.
 *
 */

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
        setfilteredTrackable(null); // set default/inital filtered trackable list to whole trackables.
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
        category.add(context.getResources().getString(R.string.trackmanager_default_category));
        for (Trackable trackable: trackableMap.values()){
            if(!category.contains(trackable.getCategory())){
                category.add(trackable.getCategory());
            }
        }
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


    public List<TrackingInfoProcessor.Pair> getAllReachables(Location currLocation) throws JSONException {

        List<TrackingInfoProcessor.Pair> allReachables = new ArrayList<>();
        for (Integer trackableId: trackableMap.keySet()){
            // filter out the trackables that don't have tracking infos
            if(processor.getTrackingInfoWithId(trackableId).size() != 0) {

                List<TrackingInfoProcessor.Pair> reachables = processor.getReachablesbyId(currLocation, trackableId);
                allReachables.addAll(reachables);
            }
        }

        System.out.println("All Reachables = " + allReachables.size());
        return allReachables;
    }

    public List<Integer> getFilteredTrackableIds() {
        return this.filteredTrackableIds;
    }
}
