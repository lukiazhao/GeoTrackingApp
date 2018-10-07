package com.rmit.geotracking.view;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.rmit.geotracking.broadcast_receiver.NetworkReceiver;
import com.rmit.geotracking.database.SyncTrackingListTask;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.utilities.AlarmGenerator;

public class StartUp extends Application {
    private NetworkReceiver networkReceiver = new NetworkReceiver();

    @Override
    public void onCreate() {
        super.onCreate();

        //register connectivity broadcast
        networkReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);

        //import existing tracking data from SQLite
        TrackManager.getSingletonInstance(this);
        new Thread(new SyncTrackingListTask(this)).start();
      
        // set first alarm
        AlarmGenerator.getSingletonInstance(this).setAlarm();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(networkReceiver);

    }
}
