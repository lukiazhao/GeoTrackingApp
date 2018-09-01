package com.rmit.geotracking.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rmit.geotracking.MainActivity;
import com.rmit.geotracking.R;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.view.TrackableActivity;
import com.rmit.geotracking.view.TrackingActivity;

public class ActivityEntryListener implements View.OnClickListener {

    private static Context context;


    private ActivityEntryListener() {
    }

    private static class LazyHolder
    {
        @SuppressLint("StaticFieldLeak")
        static final ActivityEntryListener INSTANCE = new ActivityEntryListener();
    }

    // singleton
    public static ActivityEntryListener getSingletonInstance(Context context)
    {
        ActivityEntryListener.context = context;
        return ActivityEntryListener.LazyHolder.INSTANCE;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.trackable_button:
                ((MainActivity) context).goTrackable();
                Toast.makeText(context, context.getResources()
                        .getString(R.string.menu_trackable_list), Toast.LENGTH_SHORT).show();
                break;
            case R.id.tracking_button:
                ((MainActivity) context).goTracking();
                Toast.makeText(context, context.getResources().getString(R.string.menu_tracking_list), Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }
}
