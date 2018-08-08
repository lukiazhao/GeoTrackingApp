package com.rmit.geotracking;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.rmit.geotracking.service.TestTrackingService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_view);


        TestTrackingService.test(this);

        /* spinner **/
//        String[] data = new String[]{"Category first", "selection 2", "selection 3", "selection 4", "Category last"};
        ArrayList<String> data = new ArrayList<>();
        data.add("selection 1"); data.add("selection 3"); data.add("category last");
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter adapterSpin = new ArrayAdapter(this, android.R.layout.simple_spinner_item, data);
        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapterSpin);
//        spinner.setSelection(adapterSpin.getCount());
//        spinner.setSelection(adapterSpin.getCount());

        /* Recycler view of trackables **/

        RecyclerView recyclerView = findViewById(R.id.recycler_view_trackables);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create an adapter object
        RecyclerAdapter adapter = new RecyclerAdapter(this);

        // associate the recycler view with the adapter
        recyclerView.setAdapter(adapter);
    }




}
