package com.rmit.geotracking.controller;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.rmit.geotracking.R;
import com.rmit.geotracking.broadcast_receiver.AutoDismissReceiver;
import com.rmit.geotracking.broadcast_receiver.CancelTrackingReceiver;
import com.rmit.geotracking.broadcast_receiver.RemindLaterReceiver;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.notification.NotificationsGenerator;
import com.rmit.geotracking.view.TrackingActivity;

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
        Log.i(LOG_TAG, String.format("Receive intent "));
        NotificationsGenerator.getSingletonInstance(context).buildReminderNotification(trackingID);
    }
}
