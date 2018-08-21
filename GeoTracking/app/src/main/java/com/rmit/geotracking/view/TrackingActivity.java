package com.rmit.geotracking.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.adapter.TrackingListAdapter;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.MainActivity;
import com.rmit.geotracking.model.Tracking;

import java.util.Map;


public class TrackingActivity extends MainActivity {
    private static final String LOG_TAG = "TrackingActivity";
    TrackManager trackManager = TrackManager.getSingletonInstance(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        Log.i(LOG_TAG, "start");

        loadListView();
    }

    public void loadListView(){
        Map<String, Tracking> trackingMap = trackManager.getTrackingMap();
        ListView trackingView = findViewById(R.id.tracking_list);
        TrackingListAdapter adapter = new TrackingListAdapter(this);
        trackingView.setAdapter(adapter);
    }
}
