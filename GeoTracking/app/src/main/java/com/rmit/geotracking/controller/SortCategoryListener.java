package com.rmit.geotracking.controller;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.rmit.geotracking.R;
import com.rmit.geotracking.adapter.FilterSpinnerAdapter;
import com.rmit.geotracking.adapter.TrackableListAdapter;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Trackable;
import com.rmit.geotracking.view.TrackableActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SortCategoryListener implements AdapterView.OnItemSelectedListener {

    private TrackableListAdapter adapter ;
    private Context context;
    private Map<Integer, Trackable> trackableMap;

    public SortCategoryListener(Context context, TrackableListAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
        this.trackableMap = TrackManager.getSingletonInstance(context).getTrackableMap();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItemText = (String) parent.getItemAtPosition(position);

            System.out.println("spinner: selected item is " + selectedItemText + " at: "+ position);
            getSelectedTrackable(position, selectedItemText);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



   public void getSelectedTrackable(int position, String category) {

       Map<Integer, Trackable> selected = new HashMap<>();
       if (position == 0) {
           adapter = new TrackableListAdapter(context, trackableMap);

       } else {
            for(Trackable trackable: trackableMap.values()){
                if(trackable.getCategory().equals(category)){
                    selected.put(trackable.getId(),trackable);
                    System.out.println("Sort : "+ trackable.getId() + trackable.getName());
                }
            }

            adapter = new TrackableListAdapter(context, selected);
       }

       Toast.makeText(context, "You have selected " + category, Toast.LENGTH_SHORT).show();
       // update list view
       ListView listView = ((Activity) context).findViewById(R.id.trackable_list);
       listView.setAdapter(adapter);

   }
}
