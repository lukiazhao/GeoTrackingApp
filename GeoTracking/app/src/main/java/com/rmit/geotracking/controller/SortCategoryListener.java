package com.rmit.geotracking.controller;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import android.widget.Toast;

import com.rmit.geotracking.R;
import com.rmit.geotracking.model.TrackManager;

import java.util.Observable;

public class SortCategoryListener extends Observable implements AdapterView.OnItemSelectedListener {

    private Context context;

    public SortCategoryListener(Context context) {
        this.context = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItemText = (String) parent.getItemAtPosition(position);

        if(position == 0){
            TrackManager.getSingletonInstance(context).setfilteredTrackable(null);
        } else {
            TrackManager.getSingletonInstance(context).setfilteredTrackable(selectedItemText);
            Toast.makeText(context,  context.getResources().getString(R.string.sortcategory_message)
                    + " " + selectedItemText, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

}
