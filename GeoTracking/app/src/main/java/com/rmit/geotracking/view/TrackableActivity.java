package com.rmit.geotracking.view;

import android.app.AlertDialog;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.rmit.geotracking.R;
import com.rmit.geotracking.adapter.TrackableListAdapter;
import com.rmit.geotracking.controller.SortCategoryListener;
import com.rmit.geotracking.database.SyncTrackableListTask;
import com.rmit.geotracking.model.TrackManager;


import java.util.List;

/**
 * This activity is mainly for providing UI to let user viewing all trackables and
 * providing AlertDialog functions
 *
 * Related UI components such as Buttons are also created with this activity.
 */

public class TrackableActivity extends MainActivity {

 //   private TrackManager trackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getResources().getString(R.string.actionbar_trackablelist));
   //     trackManager = TrackManager.getSingletonInstance(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        setContentView(R.layout.activity_trackable_list);

        // add spinner
        loadSpinner();

        // list view
        ListView listView = findViewById(R.id.trackable_list);

        // set adapter into list view
        listView.setAdapter(TrackableListAdapter.getSingletonInstance(this));
    }


    @Override
    protected void onStart() {
        super.onStart();

        //Sync trackable list from database
        new Thread(new SyncTrackableListTask(this)).start();
    }


    public void loadSpinner() {
        List<String> category = TrackManager.getSingletonInstance(this).readAllCategories();

        // get reference of widgets from xml layout.
        Spinner spinner = findViewById(R.id.spinner);

        final ArrayAdapter<String> adapterSpin = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, category);
        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpin);
        spinner.setOnItemSelectedListener(SortCategoryListener.getSingletonInstance(this));
    }



    //This app will block user to view or add upon a trackable without any route info
    public void showNoTrackingInfoAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(this.getResources().getString(R.string.trackablelist_noinfo))
                    .setNeutralButton(this.getResources()
                            .getString(R.string.viewtracking_confirmButton), null)
                    .setCancelable(false).show();
    }

    //Old trackable activity would be finished automaticly
    //to let the user back to home page.
    public void onRestart() {
        super.onRestart();
        this.finish();
    }
}
