package com.rmit.geotracking.view;

import android.content.Intent;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.rmit.geotracking.R;
import com.rmit.geotracking.adapter.TrackableListAdapter;
import com.rmit.geotracking.adapter.TrackableRecyclerAdapter;
import com.rmit.geotracking.MainActivity;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Trackable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrackableActivity extends MainActivity {

    private ListView listView;
    TrackManager trackManager = TrackManager.getSingletonInstance(this);
    Map<Integer, Trackable> trackableMap = trackManager.getTrackableMap();

    TrackableListAdapter adapter;
    public TrackableActivity(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackable_list);

        loadSpinner();
        ListView listView = (ListView) findViewById(R.id.trackable_list);

        // call adapter
        adapter = new TrackableListAdapter(this, trackableMap);

        // set adapter into list view
        listView.setAdapter(adapter);

        //register list view with context menu

        registerForContextMenu(listView);
    }

    public void loadSpinner(){
        ArrayList<String> data = new ArrayList<>();

        data.add("All"); data.add("category");
        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter adapterSpin = new ArrayAdapter(this, android.R.layout.simple_spinner_item, data);
        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapterSpin);
        //set the default display item to first one
//        spinner.setSelection(data.size() - 1);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_to_tracking:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Toast.makeText(this, "id +" + info.id, Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(this, AddToTracking.class);
                myIntent.putExtra("Trackable_Id", info.id + 1);
                startActivity(myIntent);

        }

        return super.onContextItemSelected(item);
    }


}
