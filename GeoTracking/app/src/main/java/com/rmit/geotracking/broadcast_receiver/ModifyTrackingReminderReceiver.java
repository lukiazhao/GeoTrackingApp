package com.rmit.geotracking.broadcast_receiver;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rmit.geotracking.R;
import com.rmit.geotracking.controller.ShowReminderAlarmListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Receive intent whenever a tracking is created, edited or deleted.
 *
 * Change notification state related to modify type
 */

public class ModifyTrackingReminderReceiver extends BroadcastReceiver {
    private final String LOG_TAG = this.getClass().getName();
    private Context context;
    private AlarmManager alarmManager;

    //A map to store references of alarm to modify existing notifications
    private static Map<String, AlarmManager.OnAlarmListener> reminderAlarms = new HashMap<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //get tracking info from intent
        long time = intent.getLongExtra(context.getResources().getString(R.string.intentkey_meettime),
                0);

        String trackingID = intent.getStringExtra(context.getResources()
                .getString(R.string.intentkey_trackingid));

        int timeOneMinutes = 60000;
        long timeBefore = Long.parseLong(PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getResources().getString(R.string.prekey_meetbefore),
                        "0")) * timeOneMinutes;

        Calendar alarmTime = Calendar.getInstance();
        long currenttime = alarmTime.getTimeInMillis();

        // set default value if no preference set
        if (timeBefore == 0) {
            int defaultRemindLaterInterval = 1;
            timeBefore = defaultRemindLaterInterval * timeOneMinutes;
        }

        String type = intent.getStringExtra(context.getResources().getString(R.string.intentkey_type));

        handleAlarm(type, trackingID, time, currenttime, timeBefore);
    }

    private void handleAlarm(String type, String trackingID, long time, long currenttime,
                             long timeBefore) {
        if(type != null) {
            if(type.equals("ADD") && time > currenttime){
                registerAlarm(time - timeBefore, trackingID);
            } else if (type.equals("REMOVE")) {
                removeAlarm(trackingID);
            } else if (type.equals("EDIT") && time > currenttime){
                removeAlarm(trackingID);
                registerAlarm(time, trackingID);
            }
        }
    }

    // create a new alarm
    private void registerAlarm(Long time, String trackingID) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        AlarmManager.OnAlarmListener reminderListener = new ShowReminderAlarmListener(context, trackingID);
        reminderAlarms.put(trackingID, reminderListener);
        assert alarmManager != null;
        String ALARMTAG = "Reminders";
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, ALARMTAG, reminderListener, null);
    }

    // delete an existing alarm
    private void removeAlarm(String trackingID) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        AlarmManager.OnAlarmListener alarmListener = reminderAlarms.get(trackingID);

        if(alarmListener != null) {
            alarmManager.cancel(reminderAlarms.get(trackingID));
        }
        Log.i(LOG_TAG, "Remove alarm");
    }
}
