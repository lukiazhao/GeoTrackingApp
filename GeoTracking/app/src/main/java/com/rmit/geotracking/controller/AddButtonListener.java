package com.rmit.geotracking.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.rmit.geotracking.view.AddToTrackingActivity;

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
        Intent toAddActivityIntent = new Intent(context, AddToTrackingActivity.class);
        toAddActivityIntent.putExtra("Trackable_Id", trackableId);
        toAddActivityIntent.putExtra("Trackable_Name", trackableName);
        context.startActivity(toAddActivityIntent);
    }
}
