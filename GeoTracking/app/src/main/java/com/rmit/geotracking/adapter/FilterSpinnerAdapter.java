package com.rmit.geotracking.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FilterSpinnerAdapter  extends ArrayAdapter {


    public FilterSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);

    }

//    @Override
//    public boolean isEnabled(int position) {
//        if (position == 0) {
//            return false;
//        } else {
//            return true;
//        }
//    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);

        System.out.println("Spinner" + position);

        TextView tv = (TextView) view;
        if(position > 0){
            tv.setTextColor(Color.RED);
        } else {
            tv.setTextColor(Color.BLACK);
        }

        return view;
    }
}
