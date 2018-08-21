package com.rmit.geotracking.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.adapter.TrackingListAdapter;
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

        loadListView();
    }

    public void loadListView(){

        Map<Integer, Tracking> trackingMap = trackManager.getTrackingMap();
        ListView trackingView = findViewById(R.id.tracking_list);
     //   trackingView.setLayoutManager(new LinearLayoutManager(this));

        TrackingListAdapter adapter = new TrackingListAdapter(this, this);
        trackingView.setAdapter(adapter);
    }


}
