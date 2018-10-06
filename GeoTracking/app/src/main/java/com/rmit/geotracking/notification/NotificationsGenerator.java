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
import android.widget.Toast;

import com.rmit.geotracking.model.Reachables;
import com.rmit.geotracking.broadcast_receiver.CancelSuggestionReceiver;
import com.rmit.geotracking.broadcast_receiver.SkipSuggestionReceiver;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Trackable;
import com.rmit.geotracking.model.TrackingInfoProcessor;
import com.rmit.geotracking.view.ModifyTrackingActivity;

import java.util.Map;


public class NotificationsGenerator {

    private final String LOG_TAG = this.getClass().getName();

    private static final String CHANNEL_SUGGESTION = "3";
    public static final int NOTIFY_ID = 1;
    private static Context context;

//    private NotificationsGenerator(){
//        createNotificationChannel();
//    }

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


    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_SUGGESTION, "suggestion ", importance);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void buildSuggestionNotification(TrackingInfoProcessor.Pair<Integer, Integer> closestIdDurationPair){

        if(closestIdDurationPair == null){
            Toast.makeText(context.getApplicationContext(), "No available trackables to suggest now ", Toast.LENGTH_LONG).show();
            return;
        }

        Map<Integer, Trackable> trackableMap = TrackManager.getSingletonInstance(context).getTrackableMap();
        String contentText = trackableMap.get(closestIdDurationPair.getFirstAttribute()).getName()
                            + " will take " + closestIdDurationPair.getSecondAttribute();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NotificationsGenerator.CHANNEL_SUGGESTION)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Closest Avaliable Trackable")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(getCancelIntent())
                .addAction(android.R.drawable.ic_dialog_alert, "Yes", getAcceptIntent(closestIdDurationPair.getFirstAttribute()))
                .addAction(android.R.drawable.ic_dialog_alert, "No", getSkipIntent())
                .addAction(android.R.drawable.ic_dialog_alert, "Cancel",getCancelIntent())
                .setAutoCancel(true);

        notificationManager.notify(NOTIFY_ID, mBuilder.build());

        // remove current pair from reachable
        Reachables.getSingletonInstance().removeSuggestedReachable(closestIdDurationPair);
    }


    private PendingIntent getAcceptIntent(int trackableId){
        Intent[] intents = new Intent[1];
        Intent acceptIntent = new Intent(context, ModifyTrackingActivity.class);
        acceptIntent.putExtra("Trackable_Id", trackableId);
        acceptIntent.putExtra("notificationId", NOTIFY_ID);
        intents[0] = acceptIntent;
        return PendingIntent.getActivities(context, NOTIFY_ID, intents, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getSkipIntent(){
        Intent skipIntent = new Intent(context, SkipSuggestionReceiver.class);
        skipIntent.putExtra("notificationId", NOTIFY_ID);
        return PendingIntent.getBroadcast(context, NOTIFY_ID, skipIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private PendingIntent getCancelIntent() {
        Intent buttonIntent = new Intent(context, CancelSuggestionReceiver.class);
        buttonIntent.putExtra("notificationId", NOTIFY_ID);
        System.out.println("CancelSuggestion"+ buttonIntent.getIntExtra("notificationId", 0));

        return PendingIntent.getBroadcast(context, NOTIFY_ID, buttonIntent, PendingIntent.FLAG_ONE_SHOT);
    }


}
