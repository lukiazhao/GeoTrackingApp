package com.rmit.geotracking.model;

import android.util.Log;

import java.util.List;

/**
 * This class is a temporary storing place for one round of suggestions,
 * which means every suggestion polling time reaches, a new list of reachables will be stored as static
 * list here. If user choose "no", then next notification will use the next closest reachables from the stored
 * list of reachables.
 */

public class Reachables {

    private final String LOG_TAG = this.getClass().getName();
    private static List<TrackingInfoProcessor.Pair<Integer, Integer>> reachables;


    private static class LazyHolder
    {
        static final Reachables INSTANCE = new Reachables();
    }
    // singleton
    public static Reachables getSingletonInstance()
    {
        return LazyHolder.INSTANCE;
    }



    public void setReachables(List<TrackingInfoProcessor.Pair<Integer, Integer>> allReachables){
        reachables = allReachables;
        Log.i(LOG_TAG, "reachables added size="+ reachables.size());
    }

    public TrackingInfoProcessor.Pair<Integer, Integer> suggestClosestTrackable(){

        TrackingInfoProcessor.Pair<Integer, Integer> closest = null;

        int smallestDuration = Integer.MAX_VALUE;
        for(TrackingInfoProcessor.Pair<Integer, Integer> p: reachables){
            if(p.getSecondAttribute() < smallestDuration){
                closest = p;
                smallestDuration = p.getSecondAttribute();
            }
        }

        Log.i(LOG_TAG, "SUGGEST CLOSEST TRACKABLE; IS NULL? "+ (closest == null));
        return closest;
    }

    public void removeSuggestedReachable(TrackingInfoProcessor.Pair suggested){
        reachables.remove(suggested);
    }

}
