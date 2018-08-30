package com.rmit.geotracking.controller;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.TrackingInfoProcessor;
import com.rmit.geotracking.view.ModifyTrackingActivity;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class TimeSelectionListener implements AdapterView.OnItemSelectedListener {

    private Context context;
    private int trackableId;
    private TrackingInfoProcessor processor;
    public TimeSelectionListener(Context context, int trackableId) {
        this.context = context;
        this.trackableId = trackableId;
        this.processor = TrackManager.getSingletonInstance(context).getTrackingInfoProcessor();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        Date selectedTime = null;
        try {
            selectedTime = processor.parseStringToDate((String) parent.getItemAtPosition(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // update target end time
        Date endTime = extractEndTime(selectedTime);

        ((ModifyTrackingActivity) context).updateEndTimeTextView(endTime);

        // update the meet time spinner list
        List<String> meetTimes = TrackManager.getSingletonInstance(context)
                                            .getTrackingInfoProcessor()
                                            .getMeetTimeList(selectedTime, endTime);

        ((ModifyTrackingActivity) context).updateMeetTimeSpinner(meetTimes);

        // update meet location
        TrackingInfoProcessor.Pair location = TrackManager.getSingletonInstance(context)
                                                  .getTrackingInfoProcessor()
                                                  .getMeetLocation(trackableId, selectedTime);
        ((ModifyTrackingActivity) context).updateMeetLocation(location.toString());
    }

    public Date extractEndTime(Date startTime) {
        Date endTime = null;
        List<TrackingInfoProcessor.Pair> pairs = TrackManager.getSingletonInstance(context)
                                                             .getTrackingInfoProcessor()
                                                             .getStartEndPairs(trackableId);
        for (TrackingInfoProcessor.Pair pair:pairs) {
            if((pair.getFirstAttribute()).equals(startTime)) {
                endTime = (Date) pair.getSecondAttribute();
            }
        }

        return endTime;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

}
