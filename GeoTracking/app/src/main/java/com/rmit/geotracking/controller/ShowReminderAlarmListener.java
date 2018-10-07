package com.rmit.geotracking.controller;

import android.app.AlarmManager;
import android.content.Context;
import android.util.Log;

import com.rmit.geotracking.notification.NotificationsGenerator;

/**
 * Alarm listener set to alarm manager to register a tracking reminder
 */

public class ShowReminderAlarmListener implements AlarmManager.OnAlarmListener {

    private final String LOG_TAG = this.getClass().getName();
    private Context context;
    private String trackingID;

    public ShowReminderAlarmListener(Context context, String trackingID) {
        this.context = context;
        this.trackingID = trackingID;
    }

    @Override
    public void onAlarm() {
        Log.i(LOG_TAG, "Receive intent ");

        // Use helper class Notification generator to gerernate notification.
        NotificationsGenerator.getSingletonInstance(context).buildReminderNotification(trackingID);
    }
}
