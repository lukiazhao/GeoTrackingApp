package com.rmit.geotracking.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.view.ModifyTrackingActivity;
import com.rmit.geotracking.view.TrackableActivity;

import java.util.Date;
import java.util.List;

public class AddButtonListener implements View.OnClickListener {

    private Context context;
    private int trackableId;
    private String trackableName;
    public AddButtonListener(Context context, int trackableId, String trackableName) {
        this.context = context;
        this.trackableId = trackableId;
        this.trackableName = trackableName;
    }
    @Override
    public void onClick(View view) {
        if (hasTrackingInfo()){
            Intent toAddActivityIntent = new Intent(context, ModifyTrackingActivity.class);
            toAddActivityIntent.putExtra("Trackable_Id", trackableId);
            toAddActivityIntent.putExtra("Trackable_Name", trackableName);
            context.startActivity(toAddActivityIntent);
        } else {
            ((TrackableActivity) context).showNoTrackingInfoAlertDialog();
        }
    }


    public boolean hasTrackingInfo(){
        List<String> trackingStartList = TrackManager.getSingletonInstance(context).getTrackingInfoProcessor().getStartTimes(trackableId);
        if(trackingStartList.size() == 0) {
            return false;
        } else {
            return true;
        }
    }
}
