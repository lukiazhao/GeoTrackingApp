package com.rmit.geotracking.broadcast_receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rmit.geotracking.model.Reachables;
import com.rmit.geotracking.model.TrackingInfoProcessor;
import com.rmit.geotracking.notification.NotificationsGenerator;

/*
 * Broadcast receiver to receive the "no" notification intent
 * it will trigger another notification for the same list of reachables
 */

public class SkipSuggestionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // dismiss current notification
        int notificationID = intent.getIntExtra("notificationId", 0);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancel(notificationID);
        }

        // start next notification with current list of reachables
        TrackingInfoProcessor.Pair<Integer, Integer> nextClosest = Reachables.getSingletonInstance().suggestClosestTrackable();
        NotificationsGenerator.getSingletonInstance(context).buildSuggestionNotification(nextClosest);
    }
}
