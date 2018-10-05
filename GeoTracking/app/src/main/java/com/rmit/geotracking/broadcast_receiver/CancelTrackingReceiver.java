package com.rmit.geotracking.broadcast_receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rmit.geotracking.view.TrackingActivity;

public class CancelTrackingReceiver extends BroadcastReceiver {
    private final String LOG_TAG = this.getClass().getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, String.format("Receive intent "));

        int notificationID = intent.getIntExtra("notificationId", 0);
        Log.i(LOG_TAG, String.format("CHECK tracking id:  " + notificationID));


        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationID);
        Intent activityIntent = new Intent(context, TrackingActivity.class);
        context.startActivity(activityIntent);



        // prolly change the intent to
    }
}
