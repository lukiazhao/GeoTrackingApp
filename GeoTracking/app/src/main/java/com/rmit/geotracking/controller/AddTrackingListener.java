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


     public AddTrackingListener(AddToTrackingActivity context, Integer trackableId, String trackingId)
     {
        this.context = context;
        this.trackableId = trackableId;
        this.trackingId = trackingId;

     }

    @Override
    public void onClick(View view) {

         createTracking();

         context.finish();
    }

    public void createTracking() {

        try {
            String title = context.getTrackingTitle().getText().toString();
            String meetLocation = context.getMeetLocation().getText().toString();
            Date startTime = (Date) context.getStartTimeSpinner().getSelectedItem();
            Date meetTime = (Date) context.getMeetTimeSpinner().getSelectedItem();
            Date endTime = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM)
                            .parse(context.getEndTimeTextView().getText().toString());
            String currLocation = null;

            Tracking tracking = new SimpleTracking(trackableId, title, startTime, endTime, meetTime,
                                            currLocation, meetLocation);
            // add tracking
            TrackManager.getSingletonInstance(context).getTrackingMap().put(tracking.getTrackingId(), tracking);

            System.out.println("Tracking size after adding: " + TrackManager.getSingletonInstance(context).getTrackingMap().size());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}
