package com.rmit.geotracking.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.rmit.geotracking.service.LocationService;

import java.util.Objects;

/**
 * Receiver monitor network state.
 *
 * Provide new suggestion if network avaliable
 */

public class NetworkReceiver extends BroadcastReceiver {
    private final String LOG_TAG = this.getClass().getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "receive");

        if (Objects.requireNonNull(intent.getAction()).equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                Log.d("Network", "Internet YAY");

               // fire the tracking suggestion
                Intent myintent = new Intent(context, LocationService.class);
                myintent.putExtra("networkReceived", true);
                context.startService(myintent);
            }
        }
    }
}
