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

public class TrackingListAdapter extends BaseAdapter {
    private Map<String, Tracking> trackingMap;
    private Context context;
    private String [] keyArray;
    private TrackManager manager;


    public TrackingListAdapter(Context context, TrackManager manager){
    //    System.out.println("Create TrackingList adapter");
        this.context = context;
        this.trackingMap = TrackManager.getSingletonInstance(context).getTrackingMap();
        this.manager = manager;
        this.keyArray = manager.generateTrackingAdapterArray();
    }

    @Override
    public int getCount() {
        return keyArray.length;
    }

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
        TextView trackingTitleView = (TextView) v.findViewById(R.id.trackable_item);
        TextView trackingMeetView = (TextView) v.findViewById(R.id.item_description);
        TextView trackingLocationView = (TextView) v.findViewById(R.id.item_url);
        Button trackingViewButton = (Button) v.findViewById(R.id.item_view_button);
        Button trackingEditButton = (Button) v.findViewById(R.id.item_add_button);

        trackingEditButton.setText(context.getResources().getString(R.string.singletrackingview_editbutton));
        trackingTitleView.setText(trackingMap.get(keyArray[position]).getTitle());
        trackingMeetView.setText(trackingMap.get(keyArray[position]).getMeetTime().toString());
        trackingLocationView.setText(trackingMap.get(keyArray[position]).getMeetLocation());

        trackingViewButton.setOnClickListener(new ViewTrackingListener(context, trackingMap.get(keyArray[position])));
        trackingEditButton.setOnClickListener(new EditTrackingListener(context, trackingMap.get(keyArray[position]), this));
        return v;
    }

    public void updateKey(){
        this.keyArray = manager.generateTrackingAdapterArray();
    }
}
