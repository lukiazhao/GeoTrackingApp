package com.rmit.geotracking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.service.TrackingService;

import java.util.Map;

public class TrackingRecyclerAdapter extends RecyclerView.Adapter<TrackingRecyclerAdapter.ViewHolder> {
    private Context context;
    private Map<String, TrackingService.TrackingInfo> trackingMap;


    public TrackingRecyclerAdapter(Context context){
        this.context = context;
        trackingMap = TrackingService.getSingletonInstance(context).getTrackingMap();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.tracking_item);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.single_trackable_view, viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolde, int i) {

    }

    @Override
    public int getItemCount() {
        return trackingMap.keySet().size();
    }

}
