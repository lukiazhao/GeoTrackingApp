package com.rmit.geotracking.broadcast_receiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rmit.geotracking.service.LocationService;
import com.rmit.geotracking.view.MainActivity;
import com.rmit.geotracking.view.TrackingActivity;
import com.rmit.geotracking.view.preference.PreferencesFragment;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class CancelSuggestionReceiver extends BroadcastReceiver {
    private final String LOG_TAG = this.getClass().getName();


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, String.format("Receive intent "));

        int notificationID = intent.getIntExtra("notificationId", 0);
        Log.i(LOG_TAG, String.format("CHECK tracking id:  " + notificationID));

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationID);


    }
}
