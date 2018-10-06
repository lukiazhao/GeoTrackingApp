package com.rmit.geotracking.broadcast_receiver;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rmit.geotracking.controller.ShowReminderAlarmListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ModifyTrackingReminderReceiver extends BroadcastReceiver {
    private final String LOG_TAG = this.getClass().getName();
    private Context context;
    private AlarmManager alarmManager;
    private static Map<String, AlarmManager.OnAlarmListener> reminderAlarms = new HashMap<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long time = intent.getLongExtra("Meettime", 0);

        String trackingID = intent.getStringExtra("TrackingID");
        Log.i(LOG_TAG, String.format("On receive TrackingID" + trackingID));

        String type = intent.getStringExtra("type");
        Log.i(LOG_TAG, String.format("Type info:" + type));

        if(type != null) {
            if(type.equals("ADD")){
                registerAlarm(time, trackingID);
            } else if (type.equals("REMOVE")) {
                removeAlarm(trackingID);
            } else {
                removeAlarm(trackingID);
                registerAlarm(time, trackingID);
                Log.i(LOG_TAG, String.format("Edit:" + type));
            }
        }
    }

    private void registerAlarm(Long time, String trackingID) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        AlarmManager.OnAlarmListener reminderListener = new ShowReminderAlarmListener(context, trackingID);
        reminderAlarms.put(trackingID, reminderListener);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, "Show notification", reminderListener, null);
    }

    private void removeAlarm(String trackingID) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        AlarmManager.OnAlarmListener alarmListener = reminderAlarms.get(trackingID);

        if(alarmListener != null) {
            alarmManager.cancel(reminderAlarms.get(trackingID));
        }
        Log.i(LOG_TAG, String.format("Remove alarm"));
    }
}
