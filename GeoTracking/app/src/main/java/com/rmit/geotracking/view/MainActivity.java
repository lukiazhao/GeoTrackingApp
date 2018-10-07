package com.rmit.geotracking.view;


import android.Manifest;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rmit.geotracking.R;
import com.rmit.geotracking.controller.ActivityEntryListener;

import com.rmit.geotracking.permission.PermissionActivity;
import com.rmit.geotracking.service.LocationService;
import com.rmit.geotracking.view.preference.FragmentPreferencesActivity;

//Entrypoint of the whole application. including two buttons, trackinglist and trackable list.
//A menu on actionbar is defined in this activity.

public class MainActivity extends PermissionActivity {

    private static final int REQUEST_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View trackableBut = findViewById(R.id.trackable_button);
        View trackingBut = findViewById(R.id.tracking_button);

        trackableBut.setOnClickListener(ActivityEntryListener.getSingletonInstance(this));
        trackingBut.setOnClickListener(ActivityEntryListener.getSingletonInstance(this));

        // check permission and handle it if no permission
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            startHandlePermission();
            return;
        }
    }


    public void startHandlePermission(){
        addPermissionHelper(REQUEST_FINE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if(checkPermission(REQUEST_FINE_LOCATION)) {
            Toast.makeText(this, "Location permission added successfully", Toast.LENGTH_LONG).show();
        }
    }

    //Import methods related to menu options and selections
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.trackable_list:
                goTrackable();
                break;
            case R.id.tracking_list:
                goTracking();
                break;
            case R.id.Preferences:
                goPreferences();
                break;
            case R.id.suggest_now:
                suggestionNow();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public void suggestionNow(){
        Intent suggestIntent = new Intent(this, LocationService.class);
        suggestIntent.putExtra("suggest_now", true);
        startService(suggestIntent);
    }

    public void goTrackable() {
        Intent myIntent = new Intent(this, TrackableActivity.class);
        startActivity(myIntent);
    }

    public void goTracking() {
        Intent myIntent = new Intent(this, TrackingActivity.class);
        startActivity(myIntent);
    }

    public void goPreferences() {
        Intent myIntent = new Intent(this, FragmentPreferencesActivity.class);
        startActivity(myIntent);
    }
}
