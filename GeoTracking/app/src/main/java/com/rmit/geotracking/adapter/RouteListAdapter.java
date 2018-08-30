package com.rmit.geotracking.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rmit.geotracking.R;

import java.util.List;

public class RouteListAdapter extends BaseAdapter {

    private List<String[]> routelist;
    private Context context;

    public RouteListAdapter(Context context, List<String[]> routelist){
        this.context = context;
        this.routelist = routelist;
    }

    @Override
    public int getCount() {
        return routelist.size();
    }

    @Override
    public String[] getItem(int i) {
        return routelist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") View v = LayoutInflater.from(context).inflate(R.layout.single_route_view, viewGroup, false);
        TextView location = v.findViewById(R.id.location_TextView);
        TextView date = v.findViewById(R.id.location_time_TextView);
        TextView stoptime = v.findViewById(R.id.location_stoptime_TextView);

        location.setText(getItem(i)[0]);
        date.setText(getItem(i)[1]);
        stoptime.setText(getItem(i)[2]);
        return v;
    }
}
