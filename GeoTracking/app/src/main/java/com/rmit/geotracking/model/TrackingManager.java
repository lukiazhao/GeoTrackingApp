package com.rmit.geotracking.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Observable;

/**
 * A manager class store all functions related to tracking data manipulation (edit, remove, update).
 *
 */

public class TrackingManager extends Observable {

    private Map<String, Tracking> trackingMap;

    // tracking map passed from track manager
    TrackingManager(Map<String, Tracking> trackingMap){
        this.trackingMap = trackingMap;
    }

    // A key array generate tool to help adapter display tracking
    public String [] generateTrackingAdapterArray(){
        ArrayList<Tracking> sortedtrackings = sortTrackingMap(trackingMap);
        String [] outputarray = new String [sortedtrackings.size()];
        int position = 0;

        for (Tracking tracking : sortedtrackings) {
            outputarray[position] = tracking.getTrackingId();
            position++;
        }

        return outputarray;
    }

    // Tool method. All tracking implment comparable to sort the array based on meet time
    private ArrayList<Tracking> sortTrackingMap(Map<String, Tracking> trackingmap) {
        Collection<Tracking> trackingCollection =  trackingmap.values();
        ArrayList<Tracking> trackings = new ArrayList<>(trackingCollection);
        Collections.sort(trackings);

        return trackings;
    }

    // Remove a specific tracking and notify adapter to update.
    public void removeTracking(Tracking tracking) {
        trackingMap.remove(tracking.getTrackingId());
        setChanged();
        notifyObservers();
    }

    // Edit a specific tracking based on a series of attributes passed from controller,
    // notify adapter to update.
    public void editTracking(Tracking tracking, String title, Date startTime,
                             Date endTime, Date meetTime, String currentLocation,
                             String meetLocation) {
        tracking.editTrackingInfo(title, startTime, endTime,
                meetTime, currentLocation, meetLocation);
        setChanged();
        notifyObservers();
    }

    public void createTracking(String trackingID, int trackableId, String title, Date startTime,
                               Date endTime, Date meetTime,
                               String currLocation, String meetLocation) {
        if (trackingID == null) {
            Tracking tracking = new SimpleTracking(trackableId, title, startTime, endTime, meetTime,
                    currLocation, meetLocation);

            // add tracking
            trackingMap.put(tracking.getTrackingId(), tracking);
        } else {
            trackingMap.put(trackingID, new SimpleTracking(trackingID, trackableId, title, startTime, endTime, meetTime,
                    currLocation, meetLocation));
        }

        setChanged();
        notifyObservers();
    }
}
