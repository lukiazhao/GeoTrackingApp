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

import org.w3c.dom.Text;

import java.util.Map;

public class TrackableListAdapter extends BaseAdapter{
    private Context context;
    Map<Integer, Trackable> trackableMap;
//            = TrackManager.getSingletonInstance(context).getTrackableMap();

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
    public View getView(int position, View view, ViewGroup viewGroup) {

       View v = LayoutInflater.from(context).inflate(R.layout.single_trackable_view, viewGroup, false);
       System.out.println("Debug:  " + (v == null));

        TextView name = v.findViewById(R.id.trackable_item);
        TextView description = v.findViewById(R.id.item_description);
        TextView url = v.findViewById(R.id.item_url);

        //set data into textView
        name.setText(String.format("%d .%s", trackableMap.get(position + 1).getId(), trackableMap.get(position + 1).getName()));
        description.setText(trackableMap.get(position + 1).getDescription());
        url.setText(trackableMap.get(position + 1).getUrl());
        return v;
    }

}
