package com.rmit.geotracking.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.adapter.RouteListAdapter;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Trackable;
import com.rmit.geotracking.model.TrackingInfoProcessor;
import com.rmit.geotracking.service.TrackingService;
import com.rmit.geotracking.view.TrackableActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewRouteListener implements View.OnClickListener {

    private int trackableID;
    private Context context;

    public ViewRouteListener(Context context, int trackableID){
        this.trackableID = trackableID;
        this.context = context;
        dataprocesser = TrackManager.getSingletonInstance(context).getTrackingInfoProcessor();
    }

    @Override
    public void onClick(View view) {
        ((TrackableActivity) context).showRouteDialog(trackableID);
    }
}
