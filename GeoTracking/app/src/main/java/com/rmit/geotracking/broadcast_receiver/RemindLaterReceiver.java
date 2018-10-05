package com.rmit.geotracking.broadcast_receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rmit.geotracking.model.TrackManager;

import java.util.Calendar;

public class RemindLaterReceiver extends BroadcastReceiver {

    private final String LOG_TAG = this.getClass().getName();
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, String.format("Receive intent "));
        this.context = context;

        // get time interval from SharedPreferences
        long timeInterval = Long.parseLong(PreferenceManager.getDefaultSharedPreferences(context)
                .getString("reminderTimeInterval", "0")) * 60000;
        if (timeInterval == 0) {
            timeInterval = 300000;
        }

        Log.i(LOG_TAG, String.format("CHECK time setting:  " + timeInterval));

        // access info from intent
        Bundle bundle = intent.getExtras();

        int notificationID = bundle.getInt("notificationId");
        String trackingID = bundle.getString("TrackingID");

        Log.i(LOG_TAG, String.format("CHECK tracking id:  " + trackingID));
        Log.i(LOG_TAG, String.format("CHECK notification id:  " + notificationID));

        Calendar alarmTime = Calendar.getInstance();
        long currenttime = alarmTime.getTimeInMillis();
        long meettime = alarmTime.getTimeInMillis() + 25000;

        sendNewReminder(meettime, currenttime, timeInterval, trackingID);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.cancel(notificationID);
    }

    // helper method to send new tracking reminder minutes later
    private void sendNewReminder(long meettime, long currenttime, long timeInterval,
                                 String trackingID ) {
        if((currenttime + timeInterval) < meettime){
            Intent newIntent = new Intent(context, ModifyTrackingReminderReceiver.class);
            newIntent.putExtra("TrackingID", trackingID);
            newIntent.putExtra("Meettime", meettime + timeInterval);
            newIntent.putExtra("type", "ADD");
            Log.i(LOG_TAG, String.format("CHECK tracking null:  " + TrackManager
                    .getSingletonInstance(context).getTrackingMap().get(trackingID)));

            context.sendBroadcast(newIntent);
        }
    }
}
