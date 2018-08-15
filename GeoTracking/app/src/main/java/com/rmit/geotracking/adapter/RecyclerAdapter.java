package com.rmit.geotracking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rmit.geotracking.R;
import com.rmit.geotracking.model.Trackable;
import com.rmit.geotracking.model.TrackableLoader;

import java.util.Map;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private String LOG_TAG = this.getClass().getName();
    private Context context;
    private Map<Integer, Trackable> trackablesMap;

    public RecyclerAdapter(Context context){
        this.context = context;
        this.trackablesMap = new TrackableLoader(context).readFile();
        Log.i(LOG_TAG, "n: "+ trackablesMap.size());

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public TextView textView2;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.trackable_item);
//            textView2 = itemView.findViewById(R.id.is_tracking);
        }
    }

    @NonNull
    @Override
    //before view is created, android allows us to do sth
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.single_trackable_view, viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    //before the data is attached to the view, android allow us to do sth
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder viewHolder, int i) {

        Log.i(LOG_TAG,"view int i: "+ i);
        Log.i(LOG_TAG,"id for map: " + (i + 1));
        // need to convert key type from int to string
        final String textToBind = trackablesMap.get((i+1)+"").getId() + trackablesMap.get((i+1)+"").getName();
        Log.i(LOG_TAG, textToBind);

//        Boolean isUnderTracking = true;

        viewHolder.textView.setText(textToBind);
//        viewHolder.textView2.setText(isUnderTracking.toString());

        // add listener here
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "you select: " + textToBind, Toast.LENGTH_SHORT).show();
                // write code to another activity
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackablesMap.size();
    }

}
