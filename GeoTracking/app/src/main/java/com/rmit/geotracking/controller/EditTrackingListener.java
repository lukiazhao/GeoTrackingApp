package com.rmit.geotracking.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.rmit.geotracking.adapter.TrackingListAdapter;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Tracking;
import com.rmit.geotracking.view.AddToTrackingActivity;

import java.util.Map;

public class EditTrackingListener implements View.OnClickListener {

    private Context context;
    private Map<String, Tracking> trackingMap;
    private Tracking tracking;


    public EditTrackingListener(Context context, Tracking tracking){
        this.context = context;
        this.tracking = tracking;
        this.trackingMap = TrackManager.getSingletonInstance(context).getTrackingMap();
    }

    @Override
    public void onClick(View view) {
        Intent toEditActivityIntent = new Intent(context, AddToTrackingActivity.class);
        toEditActivityIntent.putExtra("Tracking_Id", tracking.getTrackingId());
        context.startActivity(toEditActivityIntent);
    }
}
