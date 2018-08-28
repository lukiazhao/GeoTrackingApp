package com.rmit.geotracking.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.adapter.TrackingListAdapter;
import com.rmit.geotracking.model.SimpleTracking;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Tracking;

import java.util.Map;

public class CheckTrackingListener implements View.OnClickListener {

    private Context context;
 //   private int position;
    private Map<String, Tracking> trackingMap;
    private TrackingListAdapter adapter;
    private Tracking tracking;

    public CheckTrackingListener(Context context, Tracking tracking, TrackingListAdapter adapter){
        this.context = context;
        this.tracking = tracking;
        this.adapter = adapter;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        trackingMap = TrackManager.getSingletonInstance(context).getTrackingMap();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View mview = inflater.inflate(R.layout.view_tracking_dialog, null);

        //get textviews for all tracking attributes
        TextView title = (TextView) mview.findViewById(R.id.trackingcheck_title);
        TextView trackingID = (TextView) mview.findViewById(R.id.trackingcheck_trackingid);
        TextView trackableID = (TextView) mview.findViewById(R.id.trackingcheck_trackableid);
        TextView trackable = (TextView) mview.findViewById(R.id.trackingcheck_trackable);
        TextView start = (TextView) mview.findViewById(R.id.trackingcheck_start);
        TextView end = (TextView) mview.findViewById(R.id.trackingcheck_end);
        TextView meettime = (TextView) mview.findViewById(R.id.trackingcheck_meettime);
        TextView currentlocation = (TextView) mview.findViewById(R.id.trackingcheck_currentlocation);
        TextView meetlocation = (TextView) mview.findViewById(R.id.trackingcheck_meetlocation);
        Button confirmbutton = (Button) mview.findViewById(R.id.trackingcheck_button);

        //show value on all text views
        title.setText(tracking.getTitle());
        trackingID.setText( tracking.getTrackingId());
        trackableID.setText(tracking.getTitle());
        start.setText(tracking.getTargetStartTime().toString());
        end.setText(tracking.getTargetEndTime().toString());
//        meettime.setText(tracking.getMeetTime().toString());
        meetlocation.setText(tracking.getMeetLocation());
        currentlocation.setText( tracking.getCurrentLocation());
        //   trackingMap.get(position);

        builder.setView(mview);
        final AlertDialog dialog = builder.create();
        dialog.show();

        confirmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Test add tracking code

//                Tracking testtracking = new SimpleTracking("AAAA", 9,
//                                                           "Test",null, null, null,null,null);
          //      trackingMap.put(testtracking.getTrackingId(), testtracking);
            //    System.out.println(trackingMap.size());
//                trackingMap.put("asdasd", testtracking);
//                adapter.updateKeyArray(TrackManager.getSingletonInstance(context).generateTrackingKeyArray());
//                adapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });
    }
}
