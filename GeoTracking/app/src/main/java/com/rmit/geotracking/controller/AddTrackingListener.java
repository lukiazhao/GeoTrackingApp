package com.rmit.geotracking.controller;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.EditText;

import com.rmit.geotracking.R;
import com.rmit.geotracking.model.SimpleTracking;
import com.rmit.geotracking.model.Tracking;

import java.util.Date;

import static java.lang.Integer.parseInt;

public class AddTrackingListener implements View.OnClickListener {

     private Context contex;

     private String trackableId;
     private EditText title;
     private Date meetTime;
     private EditText meetLocation;

     public AddTrackingListener(Activity context, String trackableId)
     {
        this.contex = context;
        this.trackableId = trackableId;
        this.title = (EditText) context.findViewById(R.id.edit_title);
//        this.meetTime =  context.findViewById(R.id.edit_meet_time);
        this.meetLocation = (EditText) context.findViewById(R.id.edit_meet_location);
     }

    @Override
    public void onClick(View view)
    {
         Log.i("view?", view.getClass().getName());


    }

    public void createTracking()
    {
//        Tracking tracking = new SimpleTracking(null, parseInt(trackableId), title.getText().toString(), null, null,
//                meetTime
//                null, meetLocation.getText().toString());

    }
}
