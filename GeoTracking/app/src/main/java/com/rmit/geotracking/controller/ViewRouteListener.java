package com.rmit.geotracking.controller;

import android.content.Context;
import android.view.View;
import com.rmit.geotracking.view.TrackableActivity;

public class ViewRouteListener implements View.OnClickListener {

    private int trackableID;
    private Context context;

    public ViewRouteListener(Context context, int trackableID){
        this.trackableID = trackableID;
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        ((TrackableActivity) context).showRouteDialog(trackableID);
    }
}
