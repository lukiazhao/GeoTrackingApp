package com.rmit.geotracking.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.rmit.geotracking.R;
import com.rmit.geotracking.adapter.RecyclerAdapter;
import com.rmit.geotracking.MainActivity;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Trackable;
import com.rmit.geotracking.model.TrackableLoader;

import java.util.ArrayList;
import java.util.Map;

public class TrackableActivity extends MainActivity {

    TrackManager trackManager = TrackManager.getSingletonInstance(this);
    public TrackableActivity(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackable);


        loadSpinner();
        loadRecycler();

    }

    public void loadSpinner(){
        ArrayList<String> data = new ArrayList<>();
//
        data.add("category 1"); data.add("category 2"); data.add("category last");
        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter adapterSpin = new ArrayAdapter(this, android.R.layout.simple_spinner_item, data);
        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapterSpin);
        //set the default display item to last one
        spinner.setSelection(data.size() - 1);
    }

    public void loadRecycler(){

        Map<Integer, Trackable> trackablesMap = trackManager.getTrackableMap();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_trackables);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerAdapter adapter = new RecyclerAdapter(this, trackablesMap);
        recyclerView.setAdapter(adapter);
    }

}
