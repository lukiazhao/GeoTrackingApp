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

     private ModifyTrackingActivity modifyTrackingActivity;
     private Integer trackableId;
     private String trackingId;

     private String title, meetLocation, currLocation;
     private Date startTime, endTime, meetTime;
     private TrackingInfoProcessor processor;

     public ModifyTrackingListener(ModifyTrackingActivity modifyTrackingActivity, Integer trackableId, String trackingId)
     {
        this.modifyTrackingActivity = modifyTrackingActivity;
        this.trackableId = trackableId;
        this.trackingId = trackingId;
        this.processor = TrackManager.getSingletonInstance(modifyTrackingActivity).getTrackingInfoProcessor();
     }

    @Override
    public void onClick(View view) {

        readTracking();

        if (title.equals("")) {
            modifyTrackingActivity.noTitleTrackingAlert();
        } else {
            if (trackingId == null) {
                // new tracking object
                createTracking();

            } else {
                // edit old tracking object
                updateTracking();
            }
        }

        modifyTrackingActivity.finish();
    }

    private void readTracking() {

        try {
            title = modifyTrackingActivity.getTrackingTitle().getText().toString();
            meetLocation = modifyTrackingActivity.getMeetLocation().getText().toString();
            startTime = processor.parseStringToDate((String) modifyTrackingActivity.getStartTimeSpinner().getSelectedItem());
            meetTime = processor.parseStringToDate((String) modifyTrackingActivity.getMeetTimeSpinner().getSelectedItem());
            endTime = processor.parseStringToDate(modifyTrackingActivity.getEndTimeTextView().getText().toString());
            currLocation = TrackManager.getSingletonInstance(modifyTrackingActivity).getTrackingInfoProcessor().findCurrentLocation(trackableId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void createTracking() {

        Tracking tracking = new SimpleTracking(trackableId, title, startTime, endTime, meetTime,
                currLocation, meetLocation);

        // add tracking
        TrackManager.getSingletonInstance(modifyTrackingActivity).getTrackingMap().put(tracking.getTrackingId(), tracking);
    }

    public void updateTracking() {
       Tracking tracking =  TrackManager.getSingletonInstance(modifyTrackingActivity).getTrackingMap().get(trackingId);
       TrackManager.getSingletonInstance(modifyTrackingActivity).getTrackingManager().editTracking(tracking,title, startTime, endTime, meetTime, currLocation ,meetLocation);
    }
}
