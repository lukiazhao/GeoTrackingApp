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

    private final int NOTIFY_ID = 2;
    private final String CHANNEL_ID = "2";

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

        Log.i(LOG_TAG, String.format("Tracking ID in send on chanel "));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(getDismissIntent())
                .addAction(android.R.drawable.ic_dialog_alert, "Dismiss", getDismissIntent())
                .addAction(android.R.drawable.ic_dialog_alert, "Delete Tracking", getCancelIntent())
                .addAction(android.R.drawable.ic_dialog_alert, "Remind Me Later", getRemindLaterIntent())
                .setAutoCancel(false);

        Log.i(LOG_TAG, String.format("Send on Channel TrackingID: " + trackingID));

        manager.notify(NOTIFY_ID, builder.build());
    }

    private PendingIntent getDismissIntent() {
        Intent buttonIntent = new Intent(context, AutoDismissReceiver.class);
        buttonIntent.putExtra("notificationId", NOTIFY_ID);
        return PendingIntent.getBroadcast(context, NOTIFY_ID, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private PendingIntent getCancelIntent() {
        Intent buttonIntent = new Intent(context, CancelTrackingReceiver.class);
        buttonIntent.putExtra("notificationId", NOTIFY_ID);
        buttonIntent.putExtra("TrackingID", trackingID);
        return PendingIntent.getBroadcast(context, NOTIFY_ID, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getRemindLaterIntent() {
        Log.i(LOG_TAG, String.format("RemindLater intent"));
        Intent buttonIntent = new Intent(context, RemindLaterReceiver.class);
        buttonIntent.putExtra("notificationId", NOTIFY_ID);
        buttonIntent.putExtra("TrackingID", trackingID);

        return PendingIntent.getBroadcast(context, NOTIFY_ID, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // register channel, register as soon as possible
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Tracking", importance);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
