package com.rmit.geotracking.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rmit.geotracking.R;
import com.rmit.geotracking.controller.ModifyTrackingListener;
import com.rmit.geotracking.controller.HideKeyboardListener;
import com.rmit.geotracking.controller.TimeSelectionListener;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Tracking;
import com.rmit.geotracking.model.TrackingInfoProcessor;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class ModifyTrackingActivity extends AppCompatActivity {

    private Integer selectedTrackableId;
    private Spinner startTimeSpinner;
    private Spinner meetTimeSpinner;
    private TextView endTimeTextView;
    private EditText title ;
    private ArrayAdapter<String> startTimeAdapter;
    private String selectedTrakcingId = null;
    private Tracking selectedTracking = null;
    private TrackManager manager = TrackManager.getSingletonInstance(this);
    private TrackingInfoProcessor processor = manager.getTrackingInfoProcessor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_tracking);

        //get incoming intent's data
        getIncomingIntentExtras();

        // set edit title text to hide keyboard listener
        title = findViewById(R.id.edit_title);
        title.setOnFocusChangeListener(new HideKeyboardListener(this));

        if(selectedTrakcingId != null) {
            selectedTrackableId = manager.getTrackingMap().get(selectedTrakcingId).getTrackableId();
            selectedTracking = manager.getTrackingMap().get(selectedTrakcingId);
            setTitle();
            loadTimesWithDefault();
        } else {
            loadStartTimeSpinner();
        }

        // set intent data to text (trackable name is shown as title for customer to refer to)
        TextView trackableName = findViewById(R.id.item_name);
        trackableName.setText(manager.getTrackableMap().get(selectedTrackableId).getName());

        // set listener to "finish" button
        Button finishButton = findViewById(R.id.finish_button);
        finishButton.setOnClickListener(new ModifyTrackingListener(this, this.selectedTrackableId, this.selectedTrakcingId));
    }

    public void setTitle() {
        title.setText(manager.getTrackingMap().get(selectedTrakcingId).getTitle());
    }



    public void loadStartTimeSpinner() {

        List<String> startTimes = processor.getStartTimes(selectedTrackableId);

        startTimeSpinner = findViewById(R.id.select_start_spinner);
        // initialise adapter
        startTimeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, startTimes);

        // set adatper to spinner
        startTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startTimeSpinner.setAdapter(startTimeAdapter);

        // set listener to spinner
        startTimeSpinner.setOnItemSelectedListener(new TimeSelectionListener(this, selectedTrackableId));

    }

    public void loadTimesWithDefault() {
        loadStartTimeSpinner();
        selectedTracking = manager.getTrackingMap().get(selectedTrakcingId);
        int startTimePosition = startTimeAdapter.getPosition(processor.getFormatedDate(selectedTracking.getTargetStartTime()));

        startTimeSpinner.setSelection(startTimePosition);
        updateEndTimeTextView(selectedTracking.getTargetEndTime());
    }

    public void updateMeetTimeSpinner(List<String> meetTimes) {
        meetTimeSpinner = findViewById(R.id.select_meet_spinner);
        ArrayAdapter<String> meetTimeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, meetTimes);
        meetTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        meetTimeSpinner.setAdapter(meetTimeAdapter);

        if (selectedTrakcingId != null) {
            int meetTimePosition = meetTimeAdapter.getPosition(processor.getFormatedDate(selectedTracking.getMeetTime()));
            meetTimeSpinner.setSelection(meetTimePosition);
        }
    }

    public void updateEndTimeTextView(Date date){
        endTimeTextView = findViewById(R.id.end_time_text);
        String newDate = DateFormat.getDateTimeInstance(
                DateFormat.SHORT, DateFormat.MEDIUM).format(date);
        endTimeTextView.setText(newDate);
    }

    public void noTitleTrackingAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.modify_tracking_no_title_message)
                .setNeutralButton(R.string.viewtracking_confirmButton, null)
                .show();
    }

    public void getIncomingIntentExtras() {
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            this.selectedTrackableId = extras.getInt("Trackable_Id");
            this.selectedTrakcingId = extras.getString("Tracking_Id");
        }
    }

    public void updateMeetLocation(String selectedMeetLocation) {
        TextView meetLocation = findViewById(R.id.meet_location_text);
        meetLocation.setText(selectedMeetLocation);
    }

    public TextView getTrackingTitle() {
        return title;
    }

    public TextView getMeetLocation() {
        return findViewById(R.id.meet_location_text);
    }

    public Spinner getStartTimeSpinner() {
        return startTimeSpinner;
    }

    public Spinner getMeetTimeSpinner() {
        return meetTimeSpinner;
    }

    public TextView getEndTimeTextView() {
        return endTimeTextView;
    }
}
