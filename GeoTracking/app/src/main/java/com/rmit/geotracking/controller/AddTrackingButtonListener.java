package com.rmit.geotracking.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.view.ModifyTrackingActivity;
import com.rmit.geotracking.view.TrackableActivity;

import java.util.List;

/*
 * When "Add" button is clicked, this listener will be called
 * in order to start modifyTrackingActivity
 *
 * Display alert dialog if no tracking info available.
 */
public class AddTrackingButtonListener implements View.OnClickListener {

    private Context context;
    private int trackableId;
    public AddTrackingButtonListener(Context context, int trackableId) {
        this.context = context;
        this.trackableId = trackableId;
    }
    @Override
    public void onClick(View view) {
        if (hasTrackingInfo()){
            Intent intent = new Intent(context, ModifyTrackingActivity.class);
            intent.putExtra("Trackable_Id", trackableId);
            context.startActivity(intent);
        } else {
            ((TrackableActivity) context).showNoTrackingInfoAlertDialog();
        }
    }


    private boolean hasTrackingInfo(){
        List<String> trackingStartList = TrackManager.getSingletonInstance(context)
                                                     .getTrackingInfoProcessor()
                                                     .getStartTimes(trackableId);
        return trackingStartList.size() != 0;
    }
}
