package com.rmit.geotracking.view;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import com.rmit.geotracking.broadcast_receiver.NetworkReceiver;
import com.rmit.geotracking.database.SyncTrackingListTask;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.utilities.AlarmGenerator;

/**
 * Start UP of application. Register broadcasts and services only triggered once during
 * the lifetime of app
 */

public class StartUp extends Application {
    private NetworkReceiver networkReceiver = new NetworkReceiver();

    @Override
    public void onCreate() {
        super.onCreate();

        //register connectivity broadcast
        networkReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(networkReceiver, filter);

        //import existing tracking data from SQLite
        TrackManager.getSingletonInstance(this);
        new Thread(new SyncTrackingListTask(this)).start();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(networkReceiver);
    }
}
