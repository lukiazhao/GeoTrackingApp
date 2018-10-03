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
import com.rmit.geotracking.view.TrackingActivity;

public class ShowReminderAlarmListener implements AlarmManager.OnAlarmListener {

    private final String LOG_TAG = this.getClass().getName();
    private Context context;
    private String trackingID;

    private NotificationManagerCompat manager;

    public ShowReminderAlarmListener(Context context, String trackingID) {
        this.context = context;
        this.trackingID = trackingID;
        manager = NotificationManagerCompat.from(context);
    }

    @Override
    public void onAlarm() {
        Log.i(LOG_TAG, String.format("Receive intent "));
        createNotificationChannel();
        sendOnChannel();
    }

    private void sendOnChannel() {
        String title = "GeoReminder";
        String message = "You should go to pick up tracking '" + TrackManager.getSingletonInstance(context)
                .getTrackingMap().get(trackingID).getTitle() + "' now!";

        Notification notification = new NotificationCompat.Builder(context, "1")
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(getDismissIntent())
                .addAction(android.R.drawable.ic_dialog_alert, "Dismiss",getDismissIntent())
                .addAction(android.R.drawable.ic_dialog_alert, "Cancel",getCancelIntent())
                .addAction(android.R.drawable.ic_dialog_alert, "RemindLater",getRemindLaterIntent(trackingID))
                .setAutoCancel(true)
                .build();
        Log.i(LOG_TAG, String.format("Send on Channel TrackingID: " + trackingID));

        manager.notify(2, notification);
    }

    private PendingIntent getDismissIntent() {
        Log.i(LOG_TAG, String.format("Dismiss intent"));

        Intent buttonIntent = new Intent(context, AutoDismissReceiver.class);
        buttonIntent.putExtra("notificationId", 1);
        return PendingIntent.getBroadcast(context, 1, buttonIntent, 0);
    }


    private PendingIntent getCancelIntent() {
        Log.i(LOG_TAG, String.format("Cancel intent"));
        Intent buttonIntent = new Intent(context, CancelTrackingReceiver.class);
        buttonIntent.putExtra("notificationId", 2);
        return PendingIntent.getBroadcast(context, 2, buttonIntent, 0);

//        return PendingIntent.getBroadcast(context, 1, buttonIntent, 0);
    }

    private PendingIntent getRemindLaterIntent(String specificTrackingID) {
        Log.i(LOG_TAG, String.format("RemindLater intent"));
        Intent buttonIntent = new Intent(context, RemindLaterReceiver.class);
        buttonIntent.putExtra("notificationId", 3);
        buttonIntent.putExtra("TrackingID", specificTrackingID);
        Log.i(LOG_TAG, String.format("getRemindLater TrackingID : ", specificTrackingID));

        return PendingIntent.getBroadcast(context, 3, buttonIntent, 0);

//        return PendingIntent.getBroadcast(context, 1, buttonIntent, 0);
    }

    // register channel, register as soon as possible
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("1", "Tracking", importance);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
