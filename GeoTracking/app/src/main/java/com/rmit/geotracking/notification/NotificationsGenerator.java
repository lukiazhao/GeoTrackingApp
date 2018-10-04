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


public class NotificationsGenerator {

    private final String LOG_TAG = this.getClass().getName();

    private static final String CHANNEL_SUGGESTION = "2";

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

    public void buildNotify(){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NotificationsGenerator.CHANNEL_SUGGESTION)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Suggestion")
                .setContentText(" message ")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setContentIntent(getDismissIntent())
//                .addAction(android.R.drawable.ic_dialog_alert, "Accept", getAcceptIntent())
//                .addAction(android.R.drawable.ic_dialog_alert, "No", getSkipIntent())
//                .addAction(android.R.drawable.ic_dialog_alert, "Cancel",getCancelIntent())
                .setAutoCancel(true);

        notificationManager.notify(1, mBuilder.build());
    }


}
