package com.rmit.geotracking.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.rmit.geotracking.R;
import com.rmit.geotracking.adapter.TrackingRecyclerAdapter;
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

        loadRecycler();
    }

    public void loadRecycler(){

        Map<Integer, Tracking> trackingMap = trackManager.getTrackingMap();
        RecyclerView recyclerView = findViewById(R.id.tracking_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TrackingRecyclerAdapter adapter = new TrackingRecyclerAdapter(this, trackingMap);
        recyclerView.setAdapter(adapter);
    }


}
