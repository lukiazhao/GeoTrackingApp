package com.rmit.geotracking.model;

import android.content.Context;
import android.util.Log;

import com.rmit.geotracking.R;
import com.rmit.geotracking.service.TrackingService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
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
    TrackingService trackingService = TrackingService.getSingletonInstance(context);

    //if use singleton, change this to private !!!!
    private TrackManager(){
        this.trackableMap = loadTrackable();
        this.trackingMap = new HashMap<String, Tracking>();

        //Test
//        this.trackingMap = new HashMap<String, Tracking>();
//        this.trackingMap = loadTracking();
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

    public boolean addNewTracking(Tracking tracking) {
        trackingMap.put(tracking.getTrackingId(), tracking);
        return true;
    }

    public void removeTracking(Tracking tracking) {
     //   System.out.println("[remove]: beforetrackingsize   " + trackableMap.size());
        trackingMap.remove(tracking.getTrackingId());
    }

//    public Map<String, Tracking> loadTracking(){
//        Map<String,Tracking> trackingMap = new HashMap<>();
//        TrackingService trackingService = TrackingService.getSingletonInstance(context);
//
//        // if stop time > 0 -> put a tracking into the map
//        List<TrackingService.TrackingInfo> trackingInfos = trackingService.getTrackingInfoList();
//        for (TrackingService.TrackingInfo tr:trackingInfos){
//
//            if (tr.stopTime > 0){
//                //create new Tracking object
//                String trackingId = null;
//                int trackableId = tr.trackableId;
//                String title = trackableMap.get(tr.trackableId).getName();
//                Date targetStartTime = tr.date;
//                Date targetEndTime = new Date(tr.date.getTime() + (tr.stopTime * 60000));   // check
//                Date meetTime = targetStartTime;        // check
//                String currLocation = null;
//                String meetLocation = tr.latitude + " , " + tr.longitude;
//
//                Tracking tracking = new SimpleTracking(trackingId,trackableId,title,targetStartTime, targetEndTime, meetTime, currLocation, meetLocation);
//                trackingMap.put(tracking.getTrackingId(), tracking);
//                Log.i(LOG_TAG,  " :???????? " + tracking.toString());
//            }
//        }
//        return trackingMap;
//    }


    public String [] generateTrackingAdapterArray(){
    //    System.out.println("[remove]: aftertrackingsize   " + trackableMap.size());

        ArrayList<Tracking> sortedtrackings = sortTrackingMap(trackingMap);
     //   Set<String> keyset = sortedtrackings.keySet();
        String [] outputarray = new String [sortedtrackings.size()];
        int position = 0;

        for (Tracking tracking : sortedtrackings) {
      //      outputarray[position] = key;
            outputarray[position] = tracking.getTrackingId();
            position++;
     //       System.out.println("Checkarray!" + tracking.getTrackingId());
        }

        //test
        for(Tracking tracking : trackingMap.values()) {
            System.out.println("Test size: " + tracking.getTitle() + trackingMap.size());
        }

        return outputarray;
    }

    public ArrayList<Tracking> sortTrackingMap(Map<String, Tracking> trackingmap) {
        Collection<Tracking> trackingCollection =  trackingmap.values();
        ArrayList<Tracking> trackings = new ArrayList<>();

        for(Tracking tracking : trackingCollection) {
            trackings.add(tracking);
        }

        Collections.sort(trackings);

        return trackings;
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

    public List<Pair> getStartEndPairs(int selectedTrackableId) {
        List<Pair> startEndPairs = new ArrayList<>();
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        for (TrackingService.TrackingInfo info:trackingService.getTrackingInfoList()) {
            if (info.trackableId == selectedTrackableId && info.stopTime > 0) {
                endCal.setTime(info.date);
                endCal.set(Calendar.MINUTE, endCal.get(Calendar.MINUTE) + info.stopTime);
                startEndPairs.add(new Pair(info.date, endCal.getTime()));

            }
        }
        return startEndPairs;
    }

    public List<Date> getStartTimes(int selectedTrackableId) {
        List<Date> startTimes = new ArrayList<>();
        for(Pair<Date> pair: getStartEndPairs(selectedTrackableId)){
            startTimes.add(pair.getFirstAttribute());
        }

        return startTimes;
    }



    public List<Date> getMeetTimeList(Date startTime, Date endTime) {

        List<Date> meetTimes = new ArrayList<>();
        Calendar targetStartCal = Calendar.getInstance();
        Calendar targetEndCal = Calendar.getInstance();

        targetStartCal.setTime(startTime);
        targetEndCal.setTime(endTime);
        while (targetStartCal.before(targetEndCal)) {
            meetTimes.add(targetStartCal.getTime());
            targetStartCal.set(Calendar.MINUTE, targetStartCal.get(Calendar.MINUTE) + 1);
        }
        return meetTimes;
    }


    public static class Pair<T> {
        T firstAttribute;
        T secondAttribute;
        Pair(T firstAttribute, T secondAttribute) {
            this.firstAttribute = firstAttribute;
            this.secondAttribute = secondAttribute;
        }

        public T getFirstAttribute() {
            return firstAttribute;
        }

        public T getSecondAttribute() {
            return secondAttribute;
        }
    }

}
