package com.rmit.geotracking;

import android.util.Log;

import com.rmit.geotracking.model.TrackingInfoProcessor;

import java.util.List;

public class Reachables {

    private static class LazyHolder
    {
        static final Reachables INSTANCE = new Reachables();
    }

    // singleton
    public static Reachables getSingletonInstance()
    {
        return LazyHolder.INSTANCE;
    }

    private final String LOG_TAG = this.getClass().getName();

    private static List<TrackingInfoProcessor.Pair<Integer, Integer>> reachables;

    public void setReachables(List<TrackingInfoProcessor.Pair<Integer, Integer>> allReachables){
        reachables = allReachables;
        Log.i(LOG_TAG, "reachables added size="+ reachables.size());
    }


    public TrackingInfoProcessor.Pair<Integer, Integer> suggestClosestTrackable(){

        TrackingInfoProcessor.Pair closest = null;

        int smallestDuration = Integer.MAX_VALUE;
        for(TrackingInfoProcessor.Pair<Integer, Integer> p: reachables){
            if(p.getSecondAttribute() < smallestDuration){
                closest = p;
                smallestDuration = p.getSecondAttribute();
            }
        }
        Log.i(LOG_TAG, "SUGGEST CLOSEST TRACKABLE=" + closest.getFirstAttribute());
        return closest;
    }

    public void removeSuggestedReachable(TrackingInfoProcessor.Pair suggested){
        reachables.remove(suggested);
    }

    public List<TrackingInfoProcessor.Pair<Integer, Integer>> getReachables(){
        return reachables;
    }

}
