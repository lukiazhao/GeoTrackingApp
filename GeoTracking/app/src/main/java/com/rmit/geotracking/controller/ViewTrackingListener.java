package com.rmit.geotracking.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.model.Trackable;
import com.rmit.geotracking.model.Tracking;
import com.rmit.geotracking.view.TrackingActivity;

public class ViewTrackingListener implements View.OnClickListener {

    private Context context;
    private Tracking tracking;

    public ViewTrackingListener(Context context, Tracking tracking){
        this.context = context;
        this.tracking = tracking;
    }

    @Override
    public void onClick(View view) {
        ((TrackingActivity)context).viewTrackingView(tracking);
    }
}

