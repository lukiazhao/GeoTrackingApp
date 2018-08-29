package com.rmit.geotracking.controller;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.view.AddToTrackingActivity;

import java.util.Date;
import java.util.List;

public class TimeSelectionListener implements AdapterView.OnItemSelectedListener {

    private Context context;
    private int trackableId;
    public TimeSelectionListener(Context context, int trackableId) {
        this.context = context;
        this.trackableId = trackableId;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        Date selectedTime = (Date) parent.getItemAtPosition(position);

        // update target end time
        Date endTime = extractEndTime(selectedTime);

        ((AddToTrackingActivity) context).updateEndTimeTextView(endTime);

        // update the meet time spinner list
        List<Date> meetTimes = TrackManager.getSingletonInstance(context).getMeetTimeList(selectedTime, endTime);
        ((AddToTrackingActivity) context).updateMeetTimeSpinner(meetTimes);

    }

    public Date extractEndTime(Date startTime) {
        Date endTime = null;
        List<TrackManager.Pair> pairs = TrackManager.getSingletonInstance(context).getStartEndPairs(trackableId);
        for (TrackManager.Pair pair:pairs) {
            if((pair.getFirstAttribute()).equals(startTime)) {
                endTime = (Date) pair.getSecondAttribute();
            }
        }

        return endTime;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

}
