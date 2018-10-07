package com.rmit.geotracking.broadcast_receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
/*
 *  * Broadcast receiver to receive the cancel notification intent
*/

public class CancelSuggestionReceiver extends BroadcastReceiver {
    private final String LOG_TAG = this.getClass().getName();


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, "Receive intent ");

        int notificationID = intent.getIntExtra("notificationId", 0);
        Log.i(LOG_TAG, "CHECK tracking id:  " + notificationID);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancel(notificationID);
        }


    }
}
