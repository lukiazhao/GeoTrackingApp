package com.rmit.geotracking.model;

import android.content.Context;
import android.widget.Toast;

import com.rmit.geotracking.service.TrackingService;

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

    private TrackingService trackingService;

    TrackingInfoProcessor(Context context){
        this.trackingService = TrackingService.getSingletonInstance(context);
    }

    /*
     * Support Function: reduce workload
     * group the tracking info for a particular trackable ID
     */
    public List<TrackingService.TrackingInfo> getTrackingInfoWithId(int selectedTrackableId) {
        List<TrackingService.TrackingInfo> infos = new ArrayList<>();
        for (TrackingService.TrackingInfo info:trackingService.getTrackingInfoList()) {
            if(info.trackableId == selectedTrackableId) {
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

    public Pair<Double> getMeetLocation(int selectedTrackableId, Date startTime){

        Pair<Double> meetLocation = null;
        for (TrackingService.TrackingInfo info:getTrackingInfoWithId(selectedTrackableId)) {
            if (info.date.equals(startTime)) {
                meetLocation = new Pair<>(info.latitude, info.longitude);
            }
        }
        return meetLocation;
    }

    // Find current location according to the system time
    public String findCurrentLocation(int trackableId) {
        // current system time:
        Date currentTime = Calendar.getInstance().getTime();
        String currentLocation = null;

        // extract current location from tracking service according to the current time
        List<TrackingService.TrackingInfo> info = getTrackingInfoWithId(trackableId);
        for (int i = 0; i < info.size() - 1; i++) {
            if(currentTime.after(info.get(i).date) && currentTime.before(info.get(i + 1).date)) {
                currentLocation = info.get(i).latitude + " , " + info.get(i).longitude;
            }
        }
        return currentLocation;
    }



    public List<String[]> createRouteList(int trackableID) {
        List<String[]> routelist = new ArrayList<>();

        for(TrackingService.TrackingInfo trackingInfo : getTrackingInfoWithId(trackableID)) {
            String[] routeDetailInfo = new String [3];
            routeDetailInfo[0] = trackingInfo.latitude + "  " + trackingInfo.longitude;
            routeDetailInfo[1] = getFormatedDate(trackingInfo.date);
            routeDetailInfo[2] = Integer.toString(trackingInfo.stopTime);
            routelist.add(routeDetailInfo);
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

    public class Pair<T> {
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
