package com.rmit.geotracking.model;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.rmit.geotracking.service.LocationService;
import com.rmit.geotracking.service.TrackingService;
import com.rmit.geotracking.utilities.JsonProcessor;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.util.Locale;

/*
 * This class maily process the tracking info extraction, including
 * search startTime - endTime Pair for certain trackable,
 * extract route information,
 * and other relavent tracking information extraction.
 *
 */
public class TrackingInfoProcessor {
    private final String LOG_TAG = LocationService.class.getName();

    private TrackingService trackingService;

    TrackingInfoProcessor(Context context){
        this.trackingService = TrackingService.getSingletonInstance(context);
    }

    /*
     * Support Function: reduce workload
     * group the tracking info for a particular trackable ID
     */
    public List<TrackingService.TrackingInfo> getTrackingInfoWithId(int selectedTrackableId)
            throws java.util.ConcurrentModificationException {
        List<TrackingService.TrackingInfo> infos = new ArrayList<>();

        for (TrackingService.TrackingInfo info:trackingService.getTrackingInfoList()) {
            if (info.trackableId == selectedTrackableId) {
                infos.add(info);
            }

        }

        return infos;
    }

    public List<Pair> getStartEndPairs(int selectedTrackableId) {
        List<Pair> startEndPairs = new ArrayList<>();
        Calendar endCal = Calendar.getInstance();

        for (TrackingService.TrackingInfo info:getTrackingInfoWithId(selectedTrackableId)) {
            if (info.stopTime > 0) {
                endCal.setTime(info.date);
                endCal.set(Calendar.MINUTE, endCal.get(Calendar.MINUTE) + info.stopTime);
                startEndPairs.add(new Pair<>(info.date, endCal.getTime()));

            }
        }
        return startEndPairs;
    }

    public List<String> getStartTimes(int selectedTrackableId) {
        List<String> startTimes = new ArrayList<>();
        for(Pair pair:getStartEndPairs(selectedTrackableId)){
            startTimes.add(getFormatedDate((Date) pair.getFirstAttribute()));
        }

        return startTimes;
    }

    public List<Date> getEndTimes(int selectedTrackableId){
        List<Date> endTimes = new ArrayList<>();
        for(Pair pair:getStartEndPairs(selectedTrackableId)){
            endTimes.add((Date) pair.getSecondAttribute());
        }
        return endTimes;
    }

    public List<String> getMeetTimeList(Date startTime, Date endTime) {

        List<String> meetTimes = new ArrayList<>();
        Calendar targetStartCal = Calendar.getInstance();
        Calendar targetEndCal = Calendar.getInstance();

        targetStartCal.setTime(startTime);
        targetEndCal.setTime(endTime);
        while (targetStartCal.before(targetEndCal)) {
            meetTimes.add(getFormatedDate(targetStartCal.getTime()));
            targetStartCal.set(Calendar.MINUTE, targetStartCal.get(Calendar.MINUTE) + 1);
        }
        return meetTimes;
    }

    public Pair<Double, Double> getMeetLocation(int selectedTrackableId, Date startTime){

        Pair<Double, Double> meetLocation = null;
        for (TrackingService.TrackingInfo info:getTrackingInfoWithId(selectedTrackableId)) {
            if (info.date.equals(startTime)) {
                meetLocation = new Pair<>(info.latitude, info.longitude);
            }
        }
        return meetLocation;
    }



    // Find current location according to the system time
    public String findCurrentLocation(int trackableId) {
//        // current system time:
//        Date currentTime = Calendar.getInstance().getTime();
        String currentLocation = null;

//
//        // extract current location from tracking service according to the current time
//        List<TrackingService.TrackingInfo> info = getTrackingInfoWithId(trackableId);
//
//        if (currentTime.getTime() < info.get(0).date.getTime()){
//            currentLocation = info.get(0).latitude + "," + info.get(0).longitude;
//        } else if (currentTime.getTime() > info.get(info.size() - 1).date.getTime()) {
//            currentLocation = info.get(info.size() - 1).latitude + "," + info.get(info.size() - 1).longitude;
//        } else {
//
//            for (int i = 0; i < info.size() - 1; i++) {
//
//                if (currentTime.after(info.get(i).date) && currentTime.before(info.get(i + 1).date)) {
//                    // check which time point is closer to current time
//                    long diffToPrev = currentTime.getTime() - info.get(i).date.getTime();
//                    long diffToNext = info.get(i + 1).date.getTime() - currentTime.getTime();
//                    if (diffToPrev < diffToNext) {
//                        currentLocation = info.get(i).latitude + "," + info.get(i).longitude;
//                    } else {
//                        currentLocation = info.get(i + 1).latitude + "," + info.get(i + 1).longitude;
//                    }
//                }
//            }
//        }

        TrackingService.TrackingInfo info = findCurrentTrackingInfo(trackableId);
        return info.latitude + "," + info.longitude;
    }

    public TrackingService.TrackingInfo findCurrentTrackingInfo(int trackableId){
        Date currentTime = Calendar.getInstance().getTime();
        List<TrackingService.TrackingInfo> info = getTrackingInfoWithId(trackableId);
        TrackingService.TrackingInfo currentInfo = null;

        if (currentTime.getTime() < info.get(0).date.getTime()){
            currentInfo =  info.get(0);
        } else if (currentTime.getTime() > info.get(info.size() - 1).date.getTime()) {
            currentInfo = info.get(info.size() - 1);
        } else {

            for (int i = 0; i < info.size() - 1; i++) {

                if (currentTime.after(info.get(i).date) && currentTime.before(info.get(i + 1).date)) {
                    // check which time point is closer to current time
                    long diffToPrev = currentTime.getTime() - info.get(i).date.getTime();
                    long diffToNext = info.get(i + 1).date.getTime() - currentTime.getTime();
                    if (diffToPrev < diffToNext) {
                        currentInfo = info.get(i);
                    } else {
                        currentInfo = info.get(i + 1);
                    }
                }
            }
        }
        return currentInfo;
    }

    public List<TrackingService.TrackingInfo> findStationaryTrackingInfo(int trackableId) {
        List<TrackingService.TrackingInfo> stationaryInfo = new ArrayList<>();
        for (TrackingService.TrackingInfo info:getTrackingInfoWithId(trackableId)) {
            if (info.stopTime > 0) {
                stationaryInfo.add(info);
            }
        }

        return stationaryInfo;
    }


    public List<Pair> createRouteList(int trackableID) {
        List<Pair> routelist = new ArrayList<>();

        for(TrackingService.TrackingInfo trackingInfo : getTrackingInfoWithId(trackableID)) {
            routelist.add(new Pair<>(trackingInfo.latitude, trackingInfo.longitude));

        }
        return routelist;
    }

    public String getFormatedDate(Date date){
        return DateFormat.getDateTimeInstance(
                DateFormat.SHORT, DateFormat.MEDIUM).format(date);
    }

    public Date parseStringToDate(String date) throws ParseException {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse(date);
    }


    public List<Pair<Integer, Integer>> getReachablesbyId(Location currLocation, Integer trackableId) throws JSONException {
        Log.i(LOG_TAG, "GET Reachables for id = "+ trackableId);
        List<Pair<Integer, Integer>> reachables = new ArrayList<>();

        JsonProcessor jsonProcessor = new JsonProcessor();
        // obtain stationary info (lines)
        List<TrackingService.TrackingInfo> stationaryTrackingInfo = findStationaryTrackingInfo(trackableId);

        Log.i(LOG_TAG, "stationary tracking information: id:" + trackableId + "size = " + stationaryTrackingInfo.size());

        Date now = Calendar.getInstance().getTime();

        for (TrackingService.TrackingInfo info: stationaryTrackingInfo){

            Log.i(LOG_TAG, "Tracking info "+ info.date + " now: "+ now);

            if(info.date.after(now)){ // in the future
                // find stationary location
                String stationaryLocation = info.latitude + "," + info.longitude;
                Log.i(LOG_TAG, "The Trackable is still in the future at" + stationaryLocation);
                // get json (duration)

                JSONObject json = jsonProcessor.getJson(currLocation, stationaryLocation);
                String durationString = jsonProcessor.parseJson(json);      // time to get there
                int duration = Integer.parseInt(durationString.replaceAll("[^0-9]", ""));
                Log.i(LOG_TAG, "DURATION=" + duration);
                // duration + current time <= end time
                Calendar plannedArriveTime = Calendar.getInstance();
                Log.i(LOG_TAG, "NOW ?" + plannedArriveTime.getTime());


                plannedArriveTime.set(Calendar.MINUTE, plannedArriveTime.get(Calendar.MINUTE) + duration);
                Log.i(LOG_TAG, "planned arrive time : " + plannedArriveTime.getTime());

                Calendar stationaryEndTime = Calendar.getInstance();
                stationaryEndTime.setTime(info.date);
                stationaryEndTime.set(Calendar.MINUTE, stationaryEndTime.get(Calendar.MINUTE) + info.stopTime);

                Log.i(LOG_TAG, "stationary end time = "+ stationaryEndTime.getTime());

                if(plannedArriveTime.getTime().before(stationaryEndTime.getTime())){        // before stationary time finish
                    reachables.add(new Pair<>(trackableId, duration));
                    Log.i(LOG_TAG, "BEFORE stationary time finish; size = " + reachables.size());
                }

            }
        }

        Log.i(LOG_TAG, "Reachable size =" + reachables.size());

        for(Pair p: reachables){
            Log.i(LOG_TAG, "reachable id"+ p.getFirstAttribute() + "time taken=" + p.getSecondAttribute());
        }
        return reachables;
    }




    public static class Pair<T, S> {
        T firstAttribute;
        S secondAttribute;
        Pair(T firstAttribute, S secondAttribute) {
            this.firstAttribute = firstAttribute;
            this.secondAttribute = secondAttribute;
        }

        public T getFirstAttribute() {
            return firstAttribute;
        }

        public S getSecondAttribute() {
            return secondAttribute;
        }
        @Override
        public String toString() {
            return firstAttribute.toString() + " , " + secondAttribute.toString();
        }
    }

    //test date parsing
    public Date parseStringToDateDatabase(String dateText) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = sdf.parse(dateText);
        } catch  (Exception e){
            e.printStackTrace();
        }

        return date;
    }
}
