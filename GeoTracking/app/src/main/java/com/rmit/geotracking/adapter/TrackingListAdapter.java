package com.rmit.geotracking.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.controller.CheckTrackingListener;
import com.rmit.geotracking.controller.EditTrackingListener;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Tracking;

import java.util.Map;
import java.util.Set;

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
        this.keyArray = manager.generateTrackingKeyArray();
    //    this.keyArray = generateKeyArray(trackingMap.keySet());
        System.out.println("generateKeyArray: keyset:  " + trackingMap.keySet());
  //      System.out.println(trackingMap.get(0).getTitle());
    }

    //gerenate an array of keys to help get value in getItem() method
//    public String [] generateKeyArray(Set<String> keyset){
//        String [] outputarray = new String [keyset.size()];
//        int position = 0;
//
//        for (String key : keyset) {
//            outputarray[position] = key;
//            position++;
//            System.out.println("Checkarray!" + key);
//        }
//
//        return outputarray;
//    }

    @Override
    public int getCount() {
        return manager.generateTrackingKeyArray().length;
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

        View v = LayoutInflater.from(context).inflate(R.layout.single_tracking_view, viewGroup, false);
        System.out.println(getCount());

        TextView trackingTitleView = (TextView) v.findViewById(R.id.trackingTitleTextView);
//        TextView trackingMeetView = (TextView) v.findViewById(R.id.trackingMeetTextView);
//        TextView trackingLocationView = (TextView) v.findViewById(R.id.trackingLocationTextView);

        trackingTitleView.setText(trackingMap.get(keyArray[position]).getTitle());
//        trackingMeetView.setText(trackingMap.get(keyArray[position]).getMeetTime().toString());
//        trackingLocationView.setText(trackingMap.get(keyArray[position]).getMeetLocation());

        Button trackingViewButton = (Button) v.findViewById(R.id.trackingViewButton);
        Button trackingEditButton = (Button) v.findViewById(R.id.trackingEditButton);

        trackingViewButton.setOnClickListener(new CheckTrackingListener(context, trackingMap.get(keyArray[position]), this));
        trackingEditButton.setOnClickListener(new EditTrackingListener(context, trackingMap.get(keyArray[position]), this));
        return v;
    }

    public void updateKeyArray(String [] keyArray){
        this.keyArray = keyArray;

    }
}
