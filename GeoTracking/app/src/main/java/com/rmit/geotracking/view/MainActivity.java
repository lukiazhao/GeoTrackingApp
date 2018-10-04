package com.rmit.geotracking.view;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rmit.geotracking.HttpAsynTask;
import com.rmit.geotracking.R;
import com.rmit.geotracking.controller.ActivityEntryListener;
import com.rmit.geotracking.controller.LocationMonitorListener;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.notification.NotificationsGenerator;
import com.rmit.geotracking.permission.GetPermissionActivity;
import com.rmit.geotracking.service.LocationService;

import java.util.Calendar;

//Entrypoint of the whole application. including two buttons, trackinglist and trackable list.
//A menu on actionbar is defined in this activity.

public class MainActivity extends GetPermissionActivity {

    private TrackManager trackManager;


    private String LOG_TAG = this.getClass().getName();

    private static final int REQUEST_FINE_LOCATION = 1;
//    private static final int REQUEST_NETWORK_STATE = 2;
//    private ProgressBar bar = null;

    // hard coded prefered polling time;
    private int pollingTime = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trackManager = TrackManager.getSingletonInstance(this);

        View trackableBut = findViewById(R.id.trackable_button);
        View trackingBut = findViewById(R.id.tracking_button);

        trackableBut.setOnClickListener(ActivityEntryListener.getSingletonInstance(this));
        trackingBut.setOnClickListener(ActivityEntryListener.getSingletonInstance(this));

        // check permission
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            startHandlePermission();
            return;
        }

        System.out.println("location permitted");

        // create Notification with channels
        NotificationsGenerator generator = NotificationsGenerator.getSingletonInstance(this);

    }

    @Override
    protected void onStart (){
        super.onStart();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + pollingTime);

        AlarmManager manager = (AlarmManager) getSystemService(this.ALARM_SERVICE);

        Intent myintent = new Intent(MainActivity.this, LocationService.class);

        PendingIntent pendingIntent = PendingIntent.getService
                (MainActivity.this, 0, myintent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (manager != null) {
            manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

    }

    public void startHandlePermission(){
        addPermissionHelper(REQUEST_FINE_LOCATION,
                "REQUEST LOCATION PERMISSION", Manifest.permission.ACCESS_FINE_LOCATION);
        if(checkPermission(REQUEST_FINE_LOCATION)) {
            Toast.makeText(this, "LOCATION permission added successfully", Toast.LENGTH_LONG).show();
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
}
