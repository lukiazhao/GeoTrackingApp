package com.rmit.geotracking.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.rmit.geotracking.R;
import com.rmit.geotracking.adapter.FilterSpinnerAdapter;
import com.rmit.geotracking.adapter.TrackableListAdapter;
import com.rmit.geotracking.MainActivity;
import com.rmit.geotracking.controller.SortCategoryListener;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Trackable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrackableActivity extends MainActivity {

    private ListView listView;
    private TrackManager trackManager = TrackManager.getSingletonInstance(this);
    private Map<Integer, Trackable> trackableMap = trackManager.getTrackableMap();
    private TrackableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getResources().getString(R.string.actionbar_trackablelist));
        setContentView(R.layout.activity_trackable_list);

        System.out.println("Instance 2 hash:" + trackManager.hashCode());

        listView = (ListView) findViewById(R.id.trackable_list);

        // add spinner
        loadSpinner();

        // call adapter
        adapter = new TrackableListAdapter(this, trackableMap);

        // set adapter into list view
        listView.setAdapter(adapter);
    }

    public void loadSpinner(){

        List<String> category = trackManager.getCategory();

        // get reference of widgets from xml layout.
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        final ArrayAdapter adapterSpin = new FilterSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, category);

        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapterSpin);

        spinner.setOnItemSelectedListener(new SortCategoryListener(this, adapter));
    }

    public void showNoRouteToast(){
        Toast.makeText(this, getResources().getString(R.string.routedialog_norouteToast), Toast.LENGTH_SHORT).show();
    }

}
