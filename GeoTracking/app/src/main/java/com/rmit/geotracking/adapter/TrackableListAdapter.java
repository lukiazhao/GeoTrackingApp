package com.rmit.geotracking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.rmit.geotracking.R;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Trackable;

import java.util.Map;

public class TrackableListAdapter extends BaseAdapter{
    private Context context;
    Map<Integer, Trackable> trackableMap = TrackManager.getSingletonInstance(context).getTrackableMap();

    public TrackableListAdapter(Context context, Map<Integer, Trackable> trackableMap){
        this.context = context;
        this.trackableMap = trackableMap;
    }

    @Override
    public int getCount() {
        return trackableMap.keySet().size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.single_trackable_view, viewGroup, false);

        TextView textView = view.findViewById(R.id.trackable_item);

        //set data into textView
            System.out.println(trackableMap.get(position + 1) == null);
        textView.setText(trackableMap.get(position + 1).getId() + " ." + trackableMap.get(position + 1).getName());

        return view;
    }

}
