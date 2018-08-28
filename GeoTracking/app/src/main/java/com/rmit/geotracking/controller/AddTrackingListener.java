package com.rmit.geotracking.controller;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rmit.geotracking.model.SimpleTracking;
import com.rmit.geotracking.model.Tracking;
import com.rmit.geotracking.view.AddToTrackingActivity;

import static java.lang.Integer.parseInt;

public class AddTrackingListener implements View.OnClickListener {

     private AddToTrackingActivity context;

     private Integer trackableId;
     private EditText title;
     private TextView meetDate;
     private TextView meetTime;
     private EditText meetLocation;

     public AddTrackingListener(AddToTrackingActivity context, Integer trackableId)
     {
         // context.getView()
        this.context = context;
        this.trackableId = trackableId;
//        this.meetLocation = (EditText) context.findViewById(R.id.edit_meet_location);
     }

    @Override
    public void onClick(View view)
    {
        context.getTrackingTitle();

         createTracking();
//         context.finish();
    }

    public void createTracking()
//    DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse(scanner.next());
    {

        String date = meetDate.getText().toString();
        String time = meetTime.getText().toString();

        String dateTime = date +" "+ time;

        Tracking tracking = new SimpleTracking(null, trackableId, title.getText().toString(),
                null, null, dateTime, null, meetLocation.getText().toString());

        System.out.println("print"+ tracking.toString());

    }
}
