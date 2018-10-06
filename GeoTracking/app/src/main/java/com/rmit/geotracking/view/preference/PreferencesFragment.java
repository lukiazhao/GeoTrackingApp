package com.rmit.geotracking.view.preference;

// Refactored and additions by Caspar Ryan

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rmit.geotracking.R;
import com.rmit.geotracking.service.LocationService;
import com.rmit.geotracking.view.MainActivity;

import java.util.Calendar;
import java.util.Map;

import static android.content.Context.ALARM_SERVICE;

public class PreferencesFragment extends PreferenceFragment implements
      OnSharedPreferenceChangeListener  {

   private final String LOG_TAG = this.getClass().getName();


   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);

      // Load the preferences from an XML resource
      addPreferencesFromResource(R.xml.preferences);
   }

   // added by Caspar to do something with the prefs!
   @Override
   public void onResume()
   {
      super.onResume();

      getPreferenceScreen().getSharedPreferences()
            .registerOnSharedPreferenceChangeListener(this);
   }

   @Override
   public void onPause()
   {
      super.onPause();
      // added by Caspar
      getPreferenceScreen().getSharedPreferences()
            .unregisterOnSharedPreferenceChangeListener(this);
   }

   // added by Caspar
   @Override
   public void onSharedPreferenceChanged(
         SharedPreferences sharedPreferences, String key) {
       // don't actually need this since we have the param but shows how to
       // get the underlying SharedPreferences elsewhere in an app since we can only
       // call getPreferenceScreen() in a PreferenceActivity/PreferenceFragment
       SharedPreferences mySharedPreferences = PreferenceManager
               .getDefaultSharedPreferences(this.getActivity());

       // display the preferences for debugging purposes
       Map<String, ?> prefmap = mySharedPreferences.getAll();
       Log.i(LOG_TAG, prefmap.toString());


       //set the changed polling time

       if (key.equals("PollingTime")) {
           // set Alarm

           Log.i(LOG_TAG, "Pollint time " + sharedPreferences.getString("PollingTime", "60"));

           int pollingTime = Integer.parseInt(sharedPreferences.getString("PollingTime", "60"));
           Calendar triggerAt = Calendar.getInstance();
           triggerAt.set(Calendar.SECOND, triggerAt.get(Calendar.SECOND) + pollingTime);

           AlarmManager manager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
           Intent myintent = new Intent(getContext(), LocationService.class);
           PendingIntent pendingIntent = PendingIntent.getService
                   (getContext(), 0, myintent, PendingIntent.FLAG_ONE_SHOT);
           if (manager != null) {
               manager.setExact(AlarmManager.RTC_WAKEUP, triggerAt.getTimeInMillis(), pendingIntent);
               Log.i(LOG_TAG, "set first exact time");
           }
       }
   }

}