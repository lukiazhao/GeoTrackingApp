package com.rmit.geotracking.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.controller.CheckTrackingListener;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Tracking;

import java.util.Map;

public class TrackingListAdapter extends BaseAdapter {
    private Map<Integer, Tracking> trackingMap;
    LayoutInflater inflater;
    Context context;
    Activity activity;

    public TrackingListAdapter(Context context, Activity activity){
        System.out.println("Create TrackingList adapter");
        this.context = context;
        this.trackingMap = TrackManager.getSingletonInstance(context).getTrackingMap();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        System.out.println(trackingMap.get(0).getTitle());
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return trackingMap.size();
    }

    @Override
    public Object getItem(int i) {
        return trackingMap.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_tracking_view, viewGroup, false);
        TextView trackingTitleView = (TextView) v.findViewById(R.id.trackingTitleTextView);
        System.out.println("Position "+ position + "  Getview  " + trackingMap.get(position).getTitle());
        trackingTitleView.setText(trackingMap.get(position).getTitle());
        TextView trackingMeetView = (TextView) v.findViewById(R.id.trackingMeetTextView);
        TextView trackingLocationView = (TextView) v.findViewById(R.id.trackingLocationTextView);

        trackingMeetView.setText(trackingMap.get(position).getMeetTime().toString());
        trackingLocationView.setText(trackingMap.get(position).getMeetLocation());

        Button trackingViewButton = (Button) v.findViewById(R.id.trackingViewButton);
        Button trackingEditButton = (Button) v.findViewById(R.id.trackingEditButton);

        trackingViewButton.setOnClickListener(new CheckTrackingListener(context, trackingMap.get(position), this));

        return v;
    }
}
