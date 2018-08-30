package com.rmit.geotracking.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Tracking;

import static android.widget.Toast.LENGTH_SHORT;

public class RemoveTrackingListener implements DialogInterface.OnClickListener {

    private TrackManager trackManager;
    private Context context;
    private Tracking tracking;

    RemoveTrackingListener(Context context, Tracking tracking) {
        this.context = context;
        this.tracking = tracking;
        trackManager = TrackManager.getSingletonInstance(context);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        trackManager.getTrackingManager().removeTracking(tracking);
        Toast toast = Toast.makeText(context, "Tracking deleted.", LENGTH_SHORT);
        toast.show();

    }
}
