package com.rmit.geotracking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.rmit.geotracking.service.TrackingService;
import com.rmit.geotracking.service.TrackingService.TrackingInfo;


import java.util.List;


public class TrackingActivity extends AppCompatActivity {
    private static final String LOG_TAG = "TrackingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_tracking_view);
        Log.i(LOG_TAG, "start");

        TextView textView = findViewById(R.id.tracking_item);

        TrackingService trackingService = TrackingService.getSingletonInstance(this);

//        TrackingService.TrackingInfo;

        List<TrackingInfo> track =  trackingService.getTrackingList();

        for (TrackingInfo t:track) {
            textView.append(t.toString());
        }
    }
}
