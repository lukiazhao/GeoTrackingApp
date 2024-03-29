/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rmit.geotracking.view.preference;


import android.app.Activity;
import android.os.Bundle;

import com.rmit.geotracking.view.MainActivity;

/**
 * Demonstration of PreferenceFragment, showing a single fragment in an
 * activity.
 * 
 * Refactored and additions by Caspar
 */
public class FragmentPreferencesActivity extends Activity
{
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);

      // Display the fragment as the main content.
      getFragmentManager().beginTransaction()
            .replace(android.R.id.content, new PreferencesFragment()).commit();
   }
}
