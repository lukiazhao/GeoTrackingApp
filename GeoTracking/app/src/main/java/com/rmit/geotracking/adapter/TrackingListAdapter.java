package com.rmit.geotracking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.controller.ViewTrackingListener;
import com.rmit.geotracking.controller.EditTrackingListener;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Tracking;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Adapter to help generate Tracking list in Tracking list activity
 *
 * Use key array generated from Tracking Manager to identify item in each view
 *
 */

public class TrackingListAdapter extends BaseAdapter implements Observer {

    private Context context;
    private Map<String, Tracking> trackingMap;
    private String [] keyArray;
    private TrackManager manager; // use manager to help format date and update key array

    public TrackingListAdapter(Context context){
        this.context = context;
        manager = TrackManager.getSingletonInstance(context);
        this.trackingMap = manager.getTrackingMap();
        this.keyArray = manager.getTrackingManager().generateTrackingAdapterArray();
        manager.getTrackingManager().addObserver(this);// use observer to update this adapter
    }

    @Override
    public int getCount() {
        return keyArray.length;
    }

    // Item in this list view is a specific Tracking get from model
    @Override
    public Object getItem(int i) {
        return trackingMap.get(keyArray[i]);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_trackable_view, viewGroup, false);

        // get all elements from view
        TextView trackingTitleView = v.findViewById(R.id.trackable_item);
        TextView trackingMeetView = v.findViewById(R.id.item_description);
        TextView trackingLocationView = v.findViewById(R.id.item_url);
        Button trackingViewButton = v.findViewById(R.id.item_view_button);
        Button trackingEditButton = v.findViewById(R.id.item_add_button);

        // Set text and listener to button and textview
        trackingEditButton.setText(context.getResources().getString(R.string.singletrackingview_editbutton));
        trackingTitleView.setText(trackingMap.get(keyArray[position]).getTitle());
        trackingMeetView.setText(manager.getTrackingInfoProcessor()
                .getFormatedDate(trackingMap.get(keyArray[position]).getMeetTime()));
        trackingLocationView.setText(trackingMap.get(keyArray[position]).getMeetLocation());

        trackingViewButton.setOnClickListener(
                new ViewTrackingListener(context, trackingMap.get(keyArray[position])));
        trackingEditButton.setOnClickListener(
                new EditTrackingListener(context, trackingMap.get(keyArray[position])));

        return v;
    }

    // update data set and refresh adapter when certain methods in observable are called.
    @Override
    public void update(Observable observable, Object o) {
        this.keyArray = manager.getTrackingManager().generateTrackingAdapterArray();
        notifyDataSetChanged();
    }
}
