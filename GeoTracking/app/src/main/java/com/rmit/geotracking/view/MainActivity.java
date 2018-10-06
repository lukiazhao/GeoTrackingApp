package com.rmit.geotracking.view;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.rmit.geotracking.R;
import com.rmit.geotracking.broadcast_receiver.NetworkReceiver;
import com.rmit.geotracking.controller.ActivityEntryListener;
import com.rmit.geotracking.view.preference.FragmentPreferencesActivity;
import com.rmit.geotracking.view.preference.PreferencesFragment;


//Entrypoint of the whole application. including two buttons, trackinglist and trackable list.
//A menu on actionbar is defined in this activity.

public class MainActivity extends AppCompatActivity {

    // register connectivity receiver
    NetworkReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View trackableBut = findViewById(R.id.trackable_button);
        View trackingBut = findViewById(R.id.tracking_button);

        trackableBut.setOnClickListener(ActivityEntryListener.getSingletonInstance(this));
        trackingBut.setOnClickListener(ActivityEntryListener.getSingletonInstance(this));

        //register connectivity broadcast
        networkReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkReceiver,filter );
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
            default:
        }
        return super.onOptionsItemSelected(item);
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
