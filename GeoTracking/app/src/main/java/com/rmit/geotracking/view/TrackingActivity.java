package com.rmit.geotracking.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.service.TrackingService;
import com.rmit.geotracking.service.TrackingService.TrackingInfo;
import com.rmit.geotracking.MainActivity;


import java.util.Map;


public class TrackingActivity extends MainActivity {
    private static final String LOG_TAG = "TrackingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_tracking_view);
        Log.i(LOG_TAG, "start");

        TextView textView = findViewById(R.id.tracking_item);

        TrackingService trackingService = TrackingService.getSingletonInstance(this);


        Map<String, TrackingInfo> tr = trackingService.getTrackingMap();

        for(String t:tr.keySet()){
            textView.append(t);
            textView.append(" \n");
        }
    }
}
