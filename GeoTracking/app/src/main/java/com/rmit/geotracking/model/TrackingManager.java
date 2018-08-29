package com.rmit.geotracking.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Observable;

public class TrackingManager extends Observable{

    private Map<String, Tracking> trackingMap;

    public TrackingManager(Map<String, Tracking> trackingMap){
        this.trackingMap = trackingMap;
    }

    public String [] generateTrackingAdapterArray(){
        ArrayList<Tracking> sortedtrackings = sortTrackingMap(trackingMap);
        //   Set<String> keyset = sortedtrackings.keySet();
        String [] outputarray = new String [sortedtrackings.size()];
        int position = 0;

        for (Tracking tracking : sortedtrackings) {
            outputarray[position] = tracking.getTrackingId();
            position++;
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

    public void removeTracking(Tracking tracking) {
        trackingMap.remove(tracking.getTrackingId());
    }

    public void editTracking(Tracking tracking, String title, Date startTime, Date endTime, Date meetTime ) {
        tracking.editTrackingInfo(title, startTime, endTime, meetTime);
        setChanged();
        notifyObservers();
    }
}
