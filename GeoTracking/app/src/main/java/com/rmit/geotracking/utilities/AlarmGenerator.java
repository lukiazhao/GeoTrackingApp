package com.rmit.geotracking.utilities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.rmit.geotracking.service.LocationService;

import java.util.Calendar;
/**
 * A helper class to set alarms with user defined polling time
 */
public class AlarmGenerator {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    // singleton support
    private static class LazyHolder
    {
        @SuppressLint("StaticFieldLeak")
        static final AlarmGenerator INSTANCE = new AlarmGenerator();
    }

    // singleton
    public static AlarmGenerator getSingletonInstance(Context context)
    {
        AlarmGenerator.context = context;
        return LazyHolder.INSTANCE;
    }

    public void setAlarm(){

        Calendar triggerAt = Calendar.getInstance();
        triggerAt.set(Calendar.SECOND, triggerAt.get(Calendar.SECOND) + readStoredPollingTime());
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myintent = new Intent(context, LocationService.class);
        PendingIntent pendingIntent = PendingIntent.getService
                (context, 0, myintent, PendingIntent.FLAG_ONE_SHOT);
        if (manager != null) {
            manager.setExact(AlarmManager.RTC_WAKEUP, triggerAt.getTimeInMillis(), pendingIntent);
        }
    }

    private int readStoredPollingTime(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(preferences.getString("PollingTime", "100"));
    }

}
