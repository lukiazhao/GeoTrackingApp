package com.rmit.geotracking.broadcast_receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rmit.geotracking.R;

/**
 * Broadcast receiver to dismiss notification
 */

public class AutoDismissReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationID = intent.getIntExtra(context.getResources()
                .getString(R.string.intentkey_notificationid), 0);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.cancel(notificationID);
    }
}
