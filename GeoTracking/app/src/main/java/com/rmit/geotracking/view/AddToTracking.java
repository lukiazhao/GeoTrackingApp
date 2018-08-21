package com.rmit.geotracking.view;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.controller.AddTrackingListener;

import java.util.Calendar;

public class AddToTracking extends AppCompatActivity {

    private static final String TAG = "AddToTrackingActivity";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_tracking);

        mDisplayDate = (TextView) findViewById(R.id.select_date);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddToTracking.this, android.R.style.Theme,
                        mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date = day + "/" + (month+1) + "/" + year;
                mDisplayDate.setText(date);
            }
        };


        String trackableId = getIntent().getStringExtra("Trackable_Id");

        Button finishButton = (Button) findViewById(R.id.finish_button);
        finishButton.setOnClickListener(new AddTrackingListener(this, trackableId));

    }



}
