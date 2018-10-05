package com.rmit.geotracking.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.maps.GoogleMap;
import com.rmit.geotracking.broadcast_receiver.AutoDismissReceiver;
import com.rmit.geotracking.broadcast_receiver.CancelTrackingReceiver;
import com.rmit.geotracking.broadcast_receiver.ModifyTrackingReminderReceiver;
import com.rmit.geotracking.broadcast_receiver.SkipSuggestionReciver;
import com.rmit.geotracking.service.LocationService;
import com.rmit.geotracking.view.ModifyTrackingActivity;


public class NotificationsGenerator {

    private final String LOG_TAG = this.getClass().getName();

    private static final String CHANNEL_SUGGESTION = "2";
    private static final int NOTIFY_ID = 1;

    private static Context context;

    private NotificationsGenerator(){
        createNotificationChannel();
    }

    private static class LazyHolder
    {
        static final NotificationsGenerator INSTANCE = new NotificationsGenerator();
    }

    // singleton
    public static NotificationsGenerator getSingletonInstance(Context context)
    {
        NotificationsGenerator.context = context;
        return LazyHolder.INSTANCE;
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_SUGGESTION, "suggestion ", importance);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void buildSuggestionNotification(int trackableId){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NotificationsGenerator.CHANNEL_SUGGESTION)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Closest Avaliable Trackable")
                .setContentText(" Do you want add this trackable to your tracking list? ")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(getDismissIntent())
                .addAction(android.R.drawable.ic_dialog_alert, "Yes", getAcceptIntent(trackableId))
//                .addAction(android.R.drawable.ic_dialog_alert, "No", getSkipIntent())
                .addAction(android.R.drawable.ic_dialog_alert, "Cancel",getCancelIntent());
//                .setAutoCancel(true);

        notificationManager.notify(1, mBuilder.build());
    }


    private PendingIntent getDismissIntent() {
        Intent buttonIntent = new Intent(context, AutoDismissReceiver.class);
        buttonIntent.putExtra("notificationId", NOTIFY_ID);
        return PendingIntent.getBroadcast(context, NOTIFY_ID, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getAcceptIntent(int trackableId){
        Intent[] intents = new Intent[1];
        Intent acceptIntent = new Intent(context, ModifyTrackingActivity.class);
        acceptIntent.putExtra("Trackable_Id", trackableId);
        intents[0] = acceptIntent;

        //remove current suggestion as well

        return PendingIntent.getActivities(context, NOTIFY_ID, intents, PendingIntent.FLAG_UPDATE_CURRENT);
    }

//    private PendingIntent getSkipIntent(){
//        Intent skipIntent = new Intent(context, SkipSuggestionReciver.class);
//        skipIntent.putExtra("SkipSuggestted_trackable_id", trackableId);
//        skipIntent.putExtra("SkipSuggestted_duration", duration);
//        return PendingIntent.getBroadcast(context, NOTIFY_ID, skipIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//    }


    private PendingIntent getCancelIntent() {
        Intent buttonIntent = new Intent(context, CancelTrackingReceiver.class);
        buttonIntent.putExtra("notificationId", NOTIFY_ID);
        return PendingIntent.getBroadcast(context, NOTIFY_ID, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
