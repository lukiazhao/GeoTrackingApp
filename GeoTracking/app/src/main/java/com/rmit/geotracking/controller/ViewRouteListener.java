package com.rmit.geotracking.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.view.MapActivity;
import com.rmit.geotracking.view.TrackableActivity;

/**
 * A listener registered on view button in Trackable list.
 *
 * A series of locations will be displayed in a pop-up dialog.
 *
 */

public class ViewRouteListener implements View.OnClickListener {

    private int trackableID;
    private Context context;

    public ViewRouteListener(Context context, int trackableID){
        this.trackableID = trackableID;
        this.context = context;
    }

    // Call show route function in trackable activity by passing trackable ID
    @Override
    public void onClick(View view) {

        if(TrackManager.getSingletonInstance(context).getTrackingInfoProcessor().getTrackingInfoWithId(trackableID).size() == 0) {
            ((TrackableActivity) context).showNoTrackingInfoAlertDialog();
        } else {
            Intent gotoMap = new Intent(context, MapActivity.class);
            gotoMap.putExtra("trackableID", trackableID);

            context.startActivity(gotoMap);
        }

//        ((TrackableActivity) context).showRouteDialog(trackableID);

    }
}
