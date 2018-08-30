package com.rmit.geotracking.model;

import android.content.Context;

import com.rmit.geotracking.service.TrackingService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TrackingInfoProcessor {
    private Context context;
    private TrackingService trackingService;

    public TrackingInfoProcessor(Context context){
        this.context = context;
        this.trackingService = TrackingService.getSingletonInstance(context);
    }

    public List<Pair> getStartEndPairs(int selectedTrackableId) {
        List<Pair> startEndPairs = new ArrayList<>();
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

    public Pair<Double> getMeetLocation(int selectedTrackableId, Date startTime){

        Pair meetLocation = null;
        for (TrackingService.TrackingInfo info:trackingService.getTrackingInfoList()) {
            if (info.trackableId == selectedTrackableId && info.date.equals(startTime)) {
                meetLocation = new Pair<Double>(info.latitude, info.longitude);
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
        List<TrackingService.TrackingInfo> info = trackingService.getTrackingInfoList();
        for (int i = 0; i < info.size() - 1; i++) {

            if(trackableId == info.get(i).trackableId
                    && currentTime.after(info.get(i).date)
                    && currentTime.before(info.get(i + 1).date)) {

                currentLocation = info.get(i).latitude + " , " + info.get(i).longitude;
            }
        }
        return currentLocation;
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

    public List<String[]> createRouteList(int trackableID) {
        List<String[]> routelist = new ArrayList<String[]>();

        for(TrackingService.TrackingInfo trackingInfo : trackingService.getTrackingInfoList()) {
            if(trackingInfo.trackableId == trackableID) {
                String[] routeDetailInfo = new String [3];
                routeDetailInfo[0] = trackingInfo.latitude + "  " + trackingInfo.longitude;
                routeDetailInfo[1] = getFormatedDate(trackingInfo.date);
                routeDetailInfo[2] = Integer.toString(trackingInfo.stopTime);
                routelist.add(routeDetailInfo);
            }
        }
        return routelist;
    }

    public String getFormatedDate(Date date){
        DateFormat formater = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String formatedDate = formater.format(date);
        return formatedDate;
    }
}