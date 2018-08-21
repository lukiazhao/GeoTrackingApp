package com.rmit.geotracking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rmit.geotracking.R;

import com.rmit.geotracking.model.Trackable;

import java.util.Map;

public class TrackableRecyclerAdapter extends RecyclerView.Adapter<TrackableRecyclerAdapter.ViewHolder>{

    private String LOG_TAG = this.getClass().getName();
    private static Context context;
    private Map<Integer, Trackable> trackablesMap;

    public TrackableRecyclerAdapter(Context context, Map<Integer, Trackable> trackableMap){
        this.context = context;
        this.trackablesMap = trackableMap;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.trackable_item);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            contextMenu.setHeaderTitle("fjdalk");
            contextMenu.add("Add this to Tracking List");
            Toast.makeText(context,"clicked", Toast.LENGTH_SHORT).show();

        }
    }

    @NonNull
    @Override
    //before view is created, android allows us to do sth
    public TrackableRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.single_trackable_view, viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    //before the data is attached to the view, android allow us to do sth
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        Log.i(LOG_TAG,"view int i: "+ i);
        Log.i(LOG_TAG,"id for map: " + (i + 1));

        final String textToBind = trackablesMap.get(i+1).getId() + " . " + trackablesMap.get(i+1).getName();
        Log.i(LOG_TAG, textToBind);

        viewHolder.textView.setText(textToBind);

        // add listener here


//        viewHolder.itemView.setOnContextClickListener(new ItemViewListener(context, i));


//        {
//            @Override
//            public boolean onLongClick(View view) {
//                Toast.makeText(context, "you select: " + textToBind + (i+1) , Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });







//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "you select: " + textToBind + (i+1) , Toast.LENGTH_SHORT).show();
//                // write code to another activity
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return trackablesMap.size();
    }

}
