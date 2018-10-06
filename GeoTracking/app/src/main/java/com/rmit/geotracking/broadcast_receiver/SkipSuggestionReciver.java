package com.rmit.geotracking.broadcast_receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import com.rmit.geotracking.Reachables;
import com.rmit.geotracking.model.TrackingInfoProcessor;
import com.rmit.geotracking.notification.NotificationsGenerator;

public class SkipSuggestionReciver extends BroadcastReceiver {

    private final String LOG_TAG = this.getClass().getName();

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("Skipped the suggestion");


        // dismiss current notification
        int notificationID = intent.getIntExtra("notificationId", 0);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationID);

        // start next notification
        TrackingInfoProcessor.Pair<Integer, Integer> nextClosest = Reachables.getSingletonInstance().suggestClosestTrackable();
        NotificationsGenerator.getSingletonInstance(context).buildSuggestionNotification(nextClosest);
    }
}
