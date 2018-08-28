package com.rmit.geotracking.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.rmit.geotracking.R;
import com.rmit.geotracking.service.TrackingService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

public class TrackManager extends Observable {
    // search and filter
    // load and save
//    public static TrackManager INSTANCE = null;
    private String LOG_TAG = this.getClass().getName();
    private Map<Integer, Trackable> trackableMap;
    private Map<String, Tracking> trackingMap;
    private static Context context;

    //if use singleton, change this to private !!!!
    private TrackManager(){
        this.trackableMap = loadTrackable();
        this.trackingMap = new HashMap<String, Tracking>();
     //   this.trackingMap = loadTracking();
    }

    // singleton support
    private static class LazyHolder
    {
        static final TrackManager INSTANCE = new TrackManager();
    }

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
    public Map<String, Tracking> getTrackingMap() { return this.trackingMap; }

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

    public String [] generateTrackingKeyArray(){
        Set<String> keyset = trackingMap.keySet();
        String [] outputarray = new String [keyset.size()];
        int position = 0;

        for (String key : keyset) {
            outputarray[position] = key;
            position++;
            System.out.println("Checkarray!" + key);
        }

        return outputarray;
    }

    public boolean addNewTracking(Tracking tracking){
        trackingMap.put(tracking.getTrackingId(), tracking);
        return true;
    }



    //    public Map<String, Tracking> loadTracking(){
//        Map<String,Tracking> trackingMap = new HashMap<>();
//        TrackingService trackingService = TrackingService.getSingletonInstance(context);
//
//        // if stop time > 0 -> put a tracking into the map
//        List<TrackingService.TrackingInfo> trackingInfos = trackingService.getTrackingList();
//        for (TrackingService.TrackingInfo tr:trackingInfos){
//
//            if (tr.stopTime > 0){
////                Log.i(LOG_TAG,tr.toString()+ "??????????" + " i = " + i);
//                //create new Tracking object
//                String trackingId = null;
//                int trackableId = tr.trackableId;
//                String title = trackableMap.get(tr.trackableId).getName();
//                Date targetStartTime = tr.date;
//                Date targetEndTime = new Date(tr.date.getTime() + (tr.stopTime * 60000));   // check
//                String meetTime = targetStartTime.toString();        // check
//                String currLocation = null;
//                String meetLocation = tr.latitude + " , " + tr.longitude;
//
//                Tracking tracking = new SimpleTracking(trackingId,trackableId,title,targetStartTime, targetEndTime, meetTime, currLocation, meetLocation);
//                trackingMap.put(tracking.getTrackingId(), tracking);
//                Log.i(LOG_TAG,  " :???????? " + tracking.toString());
//            }
//        }
//
//        return trackingMap;
//    }
}
