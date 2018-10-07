package com.rmit.geotracking.broadcast_receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rmit.geotracking.R;
import com.rmit.geotracking.database.DeleteSingleTrackingTask;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Tracking;

/**
 * Broadcast receiver to delete the reminding tracking
 */

public class CancelTrackingReceiver extends BroadcastReceiver {
    private final String LOG_TAG = this.getClass().getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, "Receive intent ");

        int notificationID = intent.getIntExtra(context.getResources()
                .getString(R.string.intentkey_notificationid), 0);
        Log.i(LOG_TAG, "CHECK tracking id:  " + notificationID);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.cancel(notificationID);

        String trackingID = intent.getStringExtra(context.getResources().getString(R.string.intentkey_trackingid));

        TrackManager trackManager = TrackManager.getSingletonInstance(context);
        Tracking removeTracking = trackManager.getTrackingMap().get(trackingID);

        if(trackingID != null) {

            // remove tracking from model
            trackManager.getTrackingManager().removeTracking(removeTracking);

            // remove tracking from database
            new Thread(new DeleteSingleTrackingTask(trackingID, context)).run();
        }
    }
}
