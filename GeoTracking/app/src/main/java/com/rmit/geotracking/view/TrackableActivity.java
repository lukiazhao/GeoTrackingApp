package com.rmit.geotracking.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.rmit.geotracking.R;
import com.rmit.geotracking.adapter.TrackableRecyclerAdapter;
import com.rmit.geotracking.MainActivity;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Trackable;

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
        RecyclerView recyclerView = loadRecycler();

//        registerForContextMenu(recyclerView);
    }

    public void loadSpinner(){
        ArrayList<String> data = new ArrayList<>();
//
        data.add("All"); data.add("category");
        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter adapterSpin = new ArrayAdapter(this, android.R.layout.simple_spinner_item, data);
        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapterSpin);
        //set the default display item to last one
//        spinner.setSelection(data.size() - 1);
    }

    public RecyclerView loadRecycler(){

        Map<Integer, Trackable> trackablesMap = trackManager.getTrackableMap();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_trackables);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TrackableRecyclerAdapter adapter = new TrackableRecyclerAdapter(this, trackablesMap);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v,
//                                    ContextMenu.ContextMenuInfo menuInfo) {
//        Log.i("Trackable Activity", "this is working");
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.context_menu, menu);
//    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.add_to_tracking:
                Toast.makeText(this, "menu: trackable activity" + item.getItemId(), Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

}
