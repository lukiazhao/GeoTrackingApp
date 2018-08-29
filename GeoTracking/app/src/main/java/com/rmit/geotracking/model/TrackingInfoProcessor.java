package com.rmit.geotracking.model;

import android.content.Context;

import com.rmit.geotracking.service.TrackingService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
}
