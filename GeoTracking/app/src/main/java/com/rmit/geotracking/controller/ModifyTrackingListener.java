package com.rmit.geotracking.controller;

import android.view.View;

import com.rmit.geotracking.model.SimpleTracking;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Tracking;
import com.rmit.geotracking.model.TrackingInfoProcessor;
import com.rmit.geotracking.view.ModifyTrackingActivity;

import java.text.ParseException;
import java.util.Date;

public class ModifyTrackingListener implements View.OnClickListener {

     private ModifyTrackingActivity context;
     private Integer trackableId;
     private String trackingId;

     private String title, meetLocation, currLocation;
     private Date startTime, endTime, meetTime;
     private TrackingInfoProcessor processor;

     public ModifyTrackingListener(ModifyTrackingActivity context, Integer trackableId, String trackingId)
     {
        this.context = context;
        this.trackableId = trackableId;
        this.trackingId = trackingId;
        this.processor = TrackManager.getSingletonInstance(context).getTrackingInfoProcessor();
     }

    @Override
    public void onClick(View view) {

        readTracking();

        if(trackingId == null) {
            // new tracking object
            createTracking();
        } else {
            // edit old tracking object
            updateTracking();
        }

         context.finish();
    }

    private void readTracking() {

        try {
            title = context.getTrackingTitle().getText().toString();
            meetLocation = context.getMeetLocation().getText().toString();
            startTime = processor.parseStringToDate((String) context.getStartTimeSpinner().getSelectedItem());
            meetTime = processor.parseStringToDate((String) context.getMeetTimeSpinner().getSelectedItem());
            endTime = processor.parseStringToDate(context.getEndTimeTextView().getText().toString());
            currLocation = TrackManager.getSingletonInstance(context).getTrackingInfoProcessor()
                                                                     .findCurrentLocation(trackableId);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void createTracking() {

        Tracking tracking = new SimpleTracking(trackableId, title, startTime, endTime, meetTime,
                currLocation, meetLocation);

        // add tracking
        TrackManager.getSingletonInstance(context)
                    .getTrackingMap()
                    .put(tracking.getTrackingId(), tracking);
    }

    private void updateTracking() {
       Tracking tracking =  TrackManager.getSingletonInstance(context).getTrackingMap().get(trackingId);
       TrackManager.getSingletonInstance(context)
                   .getTrackingManager()
                   .editTracking(tracking,title, startTime, endTime, meetTime, currLocation ,meetLocation);
     }

}
