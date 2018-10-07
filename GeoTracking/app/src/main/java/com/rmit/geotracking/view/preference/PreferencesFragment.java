package com.rmit.geotracking.view.preference;

// Refactored and additions by Caspar Ryan and Zhongnian Lu

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rmit.geotracking.R;
import com.rmit.geotracking.utilities.AlarmGenerator;

import java.util.Map;

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
       SharedPreferences mySharedPreferences = PreferenceManager
               .getDefaultSharedPreferences(this.getActivity());

       // display the preferences for debugging purposes
       Map<String, ?> prefmap = mySharedPreferences.getAll();
       Log.i(LOG_TAG, prefmap.toString());


//       //set the changed polling time
//       if (key.equals("PollingTime")) {
//           // set a new alarm once the preferred polling time
//          int changedPollingTime = Integer.parseInt(sharedPreferences.getString("PollingTime", "100"));
//          AlarmGenerator.getSingletonInstance(getContext()).setAlarm();
//       }
   }

}