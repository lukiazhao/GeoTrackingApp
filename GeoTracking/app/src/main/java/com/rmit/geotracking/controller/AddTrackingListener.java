package com.rmit.geotracking.controller;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rmit.geotracking.model.SimpleTracking;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Tracking;
import com.rmit.geotracking.view.AddToTrackingActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class AddTrackingListener implements View.OnClickListener {

     private AddToTrackingActivity context;
     private Integer trackableId;
     private String trackingId;

     private String title, meetLocation, currLocation;
     private Date startTime, endTime, meetTime;


     public AddTrackingListener(AddToTrackingActivity context, Integer trackableId, String trackingId)
     {
        this.context = context;
        this.trackableId = trackableId;
        this.trackingId = trackingId;

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

    public void readTracking() {

        try {
            title = context.getTrackingTitle().getText().toString();
            meetLocation = context.getMeetLocation().getText().toString();
            startTime = (Date) context.getStartTimeSpinner().getSelectedItem();
            meetTime = (Date) context.getMeetTimeSpinner().getSelectedItem();
            endTime = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM)
                            .parse(context.getEndTimeTextView().getText().toString());
            currLocation = null;

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void createTracking() {

        Tracking tracking = new SimpleTracking(trackableId, title, startTime, endTime, meetTime,
                currLocation, meetLocation);

        // add tracking
        TrackManager.getSingletonInstance(context).getTrackingMap().put(tracking.getTrackingId(), tracking);

        System.out.println("Tracking size after adding: " + TrackManager.getSingletonInstance(context).getTrackingMap().size());

    }

    public void updateTracking() {
       Tracking tracking =  TrackManager.getSingletonInstance(context).getTrackingMap().get(trackingId);
       TrackManager.getSingletonInstance(context).getTrackingManager().editTracking(tracking,title, startTime, endTime, meetTime );

    }


}
