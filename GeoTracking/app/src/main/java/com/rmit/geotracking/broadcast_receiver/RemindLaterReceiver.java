package com.rmit.geotracking.broadcast_receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rmit.geotracking.model.TrackManager;

import java.util.Calendar;

public class RemindLaterReceiver extends BroadcastReceiver {

    final int TIME_INTERVAL = 10000;
    private final String LOG_TAG = this.getClass().getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationID = intent.getIntExtra("notificationId", 0);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String trackingID = intent.getStringExtra("TrackingID");
        Log.i(LOG_TAG, String.format("CHECK tracking id:  " + trackingID));

        Log.i(LOG_TAG, String.format("Receive intent "));

        Calendar alarmTime = Calendar.getInstance();
        long currenttime = alarmTime.getTimeInMillis();
        long meettime = alarmTime.getTimeInMillis() + 25000;

        if((currenttime + TIME_INTERVAL) < meettime){
            Intent newIntent = new Intent(context, ModifyTrackingReminderReceiver.class);
            newIntent.putExtra("TrackingID", trackingID);
            newIntent.putExtra("Meettime", meettime + TIME_INTERVAL);
            newIntent.putExtra("type", "ADD");
            Log.i(LOG_TAG, String.format("CHECK tracking null:  " + TrackManager
                    .getSingletonInstance(context).getTrackingMap().get(trackingID)));

            context.sendBroadcast(newIntent);
        }
        manager.cancel(notificationID);

    }
}
