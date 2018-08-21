package com.rmit.geotracking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.model.Tracking;

import java.util.Map;

public class TrackingRecyclerAdapter extends RecyclerView.Adapter<TrackingRecyclerAdapter.ViewHolder> {
    private Context context;
    private Map<Integer, Tracking> trackingMap;


    public TrackingRecyclerAdapter(Context context, Map<Integer, Tracking> trackingMap){
        this.context = context;
        this.trackingMap = trackingMap;
        Log.i("trackingAdapter", trackingMap.keySet().size()+"");
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
        View itemView = inflater.inflate(R.layout.single_tracking_view, viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String textToBind = trackingMap.get(i).getTrackableId() + trackingMap.get(i).getTitle() + " \n "+
                           "Meet location : " + trackingMap.get(i).getMeetLocation();
//                          + "\n"+ "Meet time : " + trackingMap.get(i).getTargetStartTime() + trackingMap.get(i).getTargetEndTime();
        viewHolder.textView.setText(textToBind);
    }

    @Override
    public int getItemCount() {
        return trackingMap.keySet().size();
    }

}
