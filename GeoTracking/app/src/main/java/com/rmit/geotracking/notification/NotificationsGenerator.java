package com.rmit.geotracking.notification;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.rmit.geotracking.R;
import com.rmit.geotracking.broadcast_receiver.AutoDismissReceiver;
import com.rmit.geotracking.broadcast_receiver.CancelTrackingReceiver;
import com.rmit.geotracking.broadcast_receiver.RemindLaterReceiver;
import com.rmit.geotracking.model.Reachables;
import com.rmit.geotracking.broadcast_receiver.CancelSuggestionReceiver;
import com.rmit.geotracking.broadcast_receiver.SkipSuggestionReceiver;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Trackable;
import com.rmit.geotracking.model.TrackingInfoProcessor;
import com.rmit.geotracking.view.ModifyTrackingActivity;

import java.util.Map;

/**
 * A helper class to generate all kinds of notifications in the app including suggestions and
 * reminders.
 *
 * Store all functions to build proper pending intent for notifications
 *
 * create two notifications channels for suggestions and reminders;
 */

public class NotificationsGenerator {

    private final String LOG_TAG = this.getClass().getName();

    // attributes for suggestion channel and notifications generation
    private static final String CHANNEL_SUGGESTION = "1";
    private static final String CHANNELNAME_SUGGESTION = "SUGGESTIONS";

    public static final int NOTIFY_ID = 1;

    // attributes for reminder channel and notifications generation
    private static final String CHANNEL_REMINDER = "2";
    private static final String CHANNELNAME_REMINDERS = "REMINDERS";

    private static final int NOTIFY_ID_REMINDER = 2;

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private Resources resource;

    // final strings values for intent sending key
    private final String INTENTKEY_NOTIFICATIONID;
    private final String INTENTKEY_TRACKABLEID;
    private final String INTENTKEY_TRACKINGID;
    private final String INTENTKEY_TRACKINGDURATION;

    private NotificationsGenerator(){
        createNotificationChannels();
        resource = context.getResources();
        INTENTKEY_NOTIFICATIONID = resource.getString(R.string.intentkey_notificationid);
        INTENTKEY_TRACKABLEID = resource.getString(R.string.intentkey_trackableid);
        INTENTKEY_TRACKINGID = resource.getString(R.string.intentkey_trackingid);
        INTENTKEY_TRACKINGDURATION = resource.getString(R.string.intentkey_duration);
    }

    private static class LazyHolder
    {
        @SuppressLint("StaticFieldLeak")
        static final NotificationsGenerator INSTANCE = new NotificationsGenerator();
    }

    // singleton
    public static NotificationsGenerator getSingletonInstance(Context context)
    {
        NotificationsGenerator.context = context;
        return LazyHolder.INSTANCE;
    }

    // create channels during initiation
    private void createNotificationChannels() {
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel1 = new NotificationChannel(CHANNEL_SUGGESTION, CHANNELNAME_SUGGESTION, importance);
        NotificationChannel channel2 = new NotificationChannel(CHANNEL_REMINDER, CHANNELNAME_REMINDERS, importance);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        assert notificationManager != null;

        notificationManager.createNotificationChannel(channel1);
        notificationManager.createNotificationChannel(channel2);
    }

    public void buildSuggestionNotification(TrackingInfoProcessor.Pair<Integer, Integer> closestIdDurationPair){

        // if no more closest reachable available, early exit.
        if(closestIdDurationPair == null){
            return;
        }

        Map<Integer, Trackable> trackableMap = TrackManager.getSingletonInstance(context).getTrackableMap();
        String contentText = resource.getString(R.string.Spend)
                            + closestIdDurationPair.getSecondAttribute()
                            +  resource.getString(R.string._mins) + trackableMap.get(closestIdDurationPair.getFirstAttribute()).getName()
                            +  resource.getString(R.string.question);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NotificationsGenerator.CHANNEL_SUGGESTION)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle(resource.getString(R.string.suggestion_notification_title))
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(getCancelIntent())
                .addAction(android.R.drawable.ic_dialog_alert,
                        resource.getString(R.string.suggestion_notification_yes),
                        getAcceptIntent(closestIdDurationPair))
                .addAction(android.R.drawable.ic_dialog_alert,
                        resource.getString(R.string.suggestion_notification_no),
                        getSkipIntent())
                .addAction(android.R.drawable.ic_dialog_alert,
                        resource.getString(R.string.suggestion_notification_cancel),
                        getCancelIntent())
                .setAutoCancel(true);

        notificationManager.notify(NOTIFY_ID, mBuilder.build());

        // remove current pair from reachables list
        Reachables.getSingletonInstance().removeSuggestedReachable(closestIdDurationPair);
    }

    public void buildReminderNotification(String trackingID) {
        String title = resource.getString(R.string.reminder_notification_title);
        String message = resource.getString(R.string.reminder_notification_dialog1)
                + TrackManager.getSingletonInstance(context).getTrackingMap().get(trackingID).getTitle()
                + resource.getString(R.string.reminder_notification_dialog2);

        Log.i(LOG_TAG, "Tracking ID in send on chanel ");

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_REMINDER);
        builder.setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(getDismissIntent())
                .addAction(android.R.drawable.ic_dialog_alert,
                        resource.getString(R.string.reminder_notification_dismiss),
                        getDismissIntent())
                .addAction(android.R.drawable.ic_dialog_alert,
                        resource.getString(R.string.reminder_notification_delete),
                        getDeleteIntent(trackingID))
                .addAction(android.R.drawable.ic_dialog_alert,
                        resource.getString(R.string.reminder_notification_relater),
                        getRemindLaterIntent(trackingID))
                .setAutoCancel(false);

        Log.i(LOG_TAG, "Send on Channel TrackingID: " + trackingID);

        manager.notify(NOTIFY_ID_REMINDER, builder.build());
    }

    // suggestion intent getters
    private PendingIntent getAcceptIntent(TrackingInfoProcessor.Pair<Integer, Integer> closestReachable){
        Intent[] intents = new Intent[1];
        Intent acceptIntent = new Intent(context, ModifyTrackingActivity.class);
        acceptIntent.putExtra(INTENTKEY_TRACKABLEID, closestReachable.getFirstAttribute());
        acceptIntent.putExtra(INTENTKEY_TRACKINGDURATION, closestReachable.getSecondAttribute());
        acceptIntent.putExtra(INTENTKEY_NOTIFICATIONID, NOTIFY_ID);
        intents[0] = acceptIntent;
        return PendingIntent.getActivities(context, NOTIFY_ID, intents, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getSkipIntent(){
        Intent skipIntent = new Intent(context, SkipSuggestionReceiver.class);
        skipIntent.putExtra(INTENTKEY_NOTIFICATIONID, NOTIFY_ID);
        return PendingIntent.getBroadcast(context, NOTIFY_ID, skipIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private PendingIntent getCancelIntent() {
        Intent buttonIntent = new Intent(context, CancelSuggestionReceiver.class);
        buttonIntent.putExtra(INTENTKEY_NOTIFICATIONID, NOTIFY_ID);
        return PendingIntent.getBroadcast(context, NOTIFY_ID, buttonIntent, PendingIntent.FLAG_ONE_SHOT);
    }


    // reminder intent getters
    private PendingIntent getDismissIntent() {
        Intent buttonIntent = new Intent(context, AutoDismissReceiver.class);
        buttonIntent.putExtra(INTENTKEY_NOTIFICATIONID, NOTIFY_ID_REMINDER);
        return PendingIntent.getBroadcast(context, NOTIFY_ID_REMINDER, buttonIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getDeleteIntent(String trackingID) {
        Intent buttonIntent = new Intent(context, CancelTrackingReceiver.class);
        buttonIntent.putExtra(INTENTKEY_NOTIFICATIONID, NOTIFY_ID_REMINDER);
        buttonIntent.putExtra(INTENTKEY_TRACKINGID, trackingID);
        return PendingIntent.getBroadcast(context, NOTIFY_ID_REMINDER, buttonIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getRemindLaterIntent(String trackingID) {
        Log.i(LOG_TAG, "RemindLater intent");
        Intent buttonIntent = new Intent(context, RemindLaterReceiver.class);
        buttonIntent.putExtra(INTENTKEY_NOTIFICATIONID, NOTIFY_ID_REMINDER);
        buttonIntent.putExtra(INTENTKEY_TRACKINGID, trackingID);

        return PendingIntent.getBroadcast(context, NOTIFY_ID_REMINDER, buttonIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
