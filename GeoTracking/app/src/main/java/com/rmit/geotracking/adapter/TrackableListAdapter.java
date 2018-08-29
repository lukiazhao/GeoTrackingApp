package com.rmit.geotracking.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.controller.AddButtonListener;
import com.rmit.geotracking.controller.ViewRouteListener;
import com.rmit.geotracking.model.Trackable;
import com.rmit.geotracking.view.AddToTrackingActivity;

import java.util.ArrayList;
import java.util.Map;

public class TrackableListAdapter extends BaseAdapter{
    private Context context;
    Map<Integer, Trackable> trackableMap;
    private final ArrayList<Integer> trackableKeyArray;
//            = TrackManager.getSingletonInstance(context).getTrackableMap();

    public TrackableListAdapter(Context context, Map<Integer, Trackable> trackableMap){
        this.context = context;
        this.trackableMap = trackableMap;
        this.trackableKeyArray = new ArrayList<>();
        trackableKeyArray.addAll(trackableMap.keySet());
    }

    @Override
    public int getCount() {
        return trackableKeyArray.size();
    }

    @Override
    public Integer getItem(int position) {
        return trackableKeyArray.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final View result;
        if (convertView == null){
            result = LayoutInflater.from(context).inflate(R.layout.single_trackable_view, viewGroup, false);
        } else {
            result = convertView;
        }

        final Trackable trackable = trackableMap.get(getItem(position));

        // set trackable data into textView
//        context.getResources().getString(R.string.textview)
        ((TextView) result.findViewById(R.id.trackable_item)).setText(trackable.getName());
        ((TextView) result.findViewById(R.id.item_description)).setText(trackable.getDescription());
        ((TextView) result.findViewById(R.id.item_url)).setText(trackable.getUrl());

        // set listner to "Add" button
        Button boundTrackingBut = (Button) result.findViewById(R.id.item_add_button);
        boundTrackingBut.setOnClickListener(new AddButtonListener(context, trackable.getId(), trackable.getName()));
        Button viewButton = (Button) result.findViewById(R.id.item_view_button);
        viewButton.setOnClickListener(new ViewRouteListener(context, trackable));

        return result;
    }
}
