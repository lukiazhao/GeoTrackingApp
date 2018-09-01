package com.rmit.geotracking.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.controller.AddTrackingButtonListener;
import com.rmit.geotracking.controller.ViewRouteListener;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Trackable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/*
 * Trackable List Adapter is used to generate trackable list
 *
 * It will be updated whenever the filteredTrackable list is changed (filter spinner item selected)
 */
public class TrackableListAdapter extends BaseAdapter implements Observer {
    private static Context context;
    private Map<Integer, Trackable> trackableMap;
    private List<Integer> trackableKeyArray;

    private TrackableListAdapter(){
        this.trackableMap = TrackManager.getSingletonInstance(context).getTrackableMap();;
        this.trackableKeyArray = new ArrayList<>();
        trackableKeyArray.addAll(trackableMap.keySet());
        TrackManager.getSingletonInstance(context).addObserver(this);
    }

    private static class LazyHolder {
        @SuppressLint("StaticFieldLeak")
        static final TrackableListAdapter INSTANCE = new TrackableListAdapter();
    }

    // singleton
    public static TrackableListAdapter getSingletonInstance(Context context) {
        TrackableListAdapter.context = context;
        return LazyHolder.INSTANCE;
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
            result = LayoutInflater.from(context).inflate(R.layout.single_item_view, viewGroup, false);
        } else {
            result = convertView;
        }

        final Trackable trackable = trackableMap.get(getItem(position));

        // set trackable data into textView
        ((TextView) result.findViewById(R.id.trackable_item)).setText(trackable.getName());
        ((TextView) result.findViewById(R.id.item_description)).setText(trackable.getDescription());
        ((TextView) result.findViewById(R.id.item_url)).setText(trackable.getUrl());

        // set listner to "Add" button
        Button boundTrackingBut = (Button) result.findViewById(R.id.item_add_button);
        boundTrackingBut.setOnClickListener(new AddTrackingButtonListener(context, trackable.getId()));
        Button viewButton = (Button) result.findViewById(R.id.item_view_button);
        viewButton.setOnClickListener(new ViewRouteListener(context, trackable.getId()));

        return result;
    }

    // update adapter whenever filtered
    @Override
    public void update(Observable o, Object arg) {
        this.trackableKeyArray = TrackManager.getSingletonInstance(context).getFilteredTrackableIds();
        notifyDataSetChanged();

    }
}
