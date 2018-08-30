package com.rmit.geotracking.controller;

import android.content.Context;
import android.view.View;
import com.rmit.geotracking.model.Tracking;
import com.rmit.geotracking.view.TrackingActivity;

/**
 * A long click listener registered on view buttons in Tracking List
 *
 * A dialog will show up to all detail tracking infor(trackable name, id, trackingid, etc.)
 *
 */

public class ViewTrackingListener implements View.OnClickListener {

    private Context context;
    private Tracking tracking;

    // a tracking is get from adapter
    public ViewTrackingListener(Context context, Tracking tracking){
        this.context = context;
        this.tracking = tracking;
    }

    // call show dialog from tracking activity.
    @Override
    public void onClick(View view) {
        ((TrackingActivity)context).viewTrackingView(tracking);
    }
}

