package com.rmit.geotracking.broadcast_receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rmit.geotracking.database.DeleteSingleTrackingTask;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Tracking;

public class CancelTrackingReceiver extends BroadcastReceiver {
    private final String LOG_TAG = this.getClass().getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, String.format("Receive intent "));

        int notificationID = intent.getIntExtra("notificationId", 0);
        Log.i(LOG_TAG, String.format("CHECK tracking id:  " + notificationID));

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationID);

        String trackingID = intent.getStringExtra("TrackingID");

        TrackManager trackManager = TrackManager.getSingletonInstance(context);
        Tracking removeTracking = trackManager.getTrackingMap().get(trackingID);

        if(trackingID != null) {

            // remove tracking from model
            trackManager.getTrackingManager().removeTracking(removeTracking);
            Log.i(LOG_TAG, String.format("remove " + trackingID));

            // remove tracking from database
            new Thread(new DeleteSingleTrackingTask(trackingID, context)).run();
        }

    }
}
