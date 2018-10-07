package com.rmit.geotracking.broadcast_receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rmit.geotracking.R;
import com.rmit.geotracking.model.TrackManager;

import java.util.Calendar;

/**
 * Receive intent after the user click remind me later
 */

public class RemindLaterReceiver extends BroadcastReceiver {

    private final String LOG_TAG = this.getClass().getName();
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, "Receive intent ");
        this.context = context;

        // get time interval from SharedPreferences
        int timeOneMinutes = 60000;
        long timeInterval = Long.parseLong(PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getResources().getString(R.string.prekey_remindinterval), "0")) * timeOneMinutes;

        System.out.println("Checking remind later : " + timeInterval);

        if (timeInterval == 0) {
            int defaultRemindLaterInterval = 1;
            timeInterval = defaultRemindLaterInterval * timeOneMinutes;
        }

        Log.i(LOG_TAG, "CHECK time setting:  " + timeInterval);

        // access info from intent
        Bundle bundle = intent.getExtras();

        // get tracking info
        assert bundle != null;
        int notificationID = bundle.getInt(context.getResources()
                .getString(R.string.intentkey_notificationid));
        String trackingID = bundle.getString(context.getResources()
                .getString(R.string.intentkey_trackingid));

        // get current time and meet time
        Calendar alarmTime = Calendar.getInstance();
        long currenttime = alarmTime.getTimeInMillis();
        long meettime = TrackManager.getSingletonInstance(context)
                .getTrackingMap().get(trackingID).getMeetTime().getTime();

        sendNewReminder(meettime, currenttime, timeInterval, trackingID);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.cancel(notificationID);
    }

    // helper method to send new tracking reminder minutes later
    private void sendNewReminder(long meettime, long currenttime, long timeInterval,
                                 String trackingID ) {
        if((currenttime + timeInterval) < meettime){
            Intent newIntent = new Intent(context, ModifyTrackingReminderReceiver.class);
            newIntent.putExtra(context.getResources().getString(R.string.intentkey_trackingid),
                    trackingID);
            newIntent.putExtra(context.getResources().getString(R.string.intentkey_meettime),
                    meettime + timeInterval);
            newIntent.putExtra(context.getString(R.string.intentkey_type), "LATER");

            Log.i(LOG_TAG, "CHECK tracking:  " + TrackManager
                    .getSingletonInstance(context).getTrackingMap().get(trackingID));
            Log.i(LOG_TAG, "CHECK time interval:  " + timeInterval);

            context.sendBroadcast(newIntent);
        }
    }
}
