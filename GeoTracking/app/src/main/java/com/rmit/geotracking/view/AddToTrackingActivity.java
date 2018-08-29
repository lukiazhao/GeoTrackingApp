package com.rmit.geotracking.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.controller.AddTrackingListener;
import com.rmit.geotracking.controller.TimeSelectionListener;
import com.rmit.geotracking.model.TrackManager;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.Integer.parseInt;

public class AddToTrackingActivity extends AppCompatActivity {

    private static final String TAG = "AddToTrackingActivity";

    private Integer selectedTrackableId;
    private Spinner startTimeSpinner;
    private Spinner meetTimeSpinner;
    private TextView endTimeTextView;
    private TextView meetLocation;
    private ArrayAdapter meetTimeAdapter;
    private String selectedTrakcingId = null;
    TrackManager manager = TrackManager.getSingletonInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_tracking);

        //get incoming intent's data
        getIncomingIntentExtras();

        if(selectedTrakcingId != null) {
            selectedTrackableId = manager.getTrackingMap().get(selectedTrakcingId).getTrackableId();
            setTitle();
        }

//        // set intent data to text (trackable name is shown as title for customer to refer to)
        TextView trackableName = findViewById(R.id.item_name);
        trackableName.setText(manager.getTrackableMap().get(selectedTrackableId).getName());

        // load Target Start Time Spinner and Meet Time Spinner
        loadDateTimeSpinners();

        // set listener to "finish" button
        Button finishButton = (Button) findViewById(R.id.finish_button);

        finishButton.setOnClickListener(new AddTrackingListener(this, this.selectedTrackableId, this.selectedTrakcingId));
    }

    public void setTitle() {
        EditText title = (EditText) findViewById(R.id.edit_title);
        title.setText(manager.getTrackingMap().get(selectedTrakcingId).getTitle());
    }



    public void loadDateTimeSpinners() {

        List<Date> startTimes = manager.getStartTimes(selectedTrackableId);
        startTimeSpinner = (Spinner) findViewById(R.id.select_start_spinner);
        // initialise adapter
        ArrayAdapter startTimeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, startTimes);

        // set adatper to spinner
        startTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startTimeSpinner.setAdapter(startTimeAdapter);

        // set listener to spinner
        startTimeSpinner.setOnItemSelectedListener(new TimeSelectionListener(this, selectedTrackableId));

    }

    public void updateMeetTimeSpinner(List<Date> meetTimes) {
        meetTimeSpinner = (Spinner) findViewById(R.id.select_meet_spinner);
        meetTimeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, meetTimes);
        meetTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        meetTimeSpinner.setAdapter(meetTimeAdapter);
    }

    public void updateEndTimeTextView(Date date){
        endTimeTextView = (TextView) findViewById(R.id.end_time_text);
        String newDate = DateFormat.getDateTimeInstance(
                DateFormat.SHORT, DateFormat.MEDIUM).format(date);
        endTimeTextView.setText(newDate);
    }

    public void getIncomingIntentExtras() {
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            this.selectedTrackableId = extras.getInt("Trackable_Id");
            this.selectedTrakcingId = extras.getString("Tracking_Id");
            System.out.println("selected tracking id: " + selectedTrakcingId);
            System.out.println("selected tracking id: " + manager.getTrackingMap().get(selectedTrakcingId));
        }
    }

    public void updateMeetLocation(String selectedMeetLocation) {
        TextView meetLocation = (TextView) findViewById(R.id.meet_location_text);
        meetLocation.setText(selectedMeetLocation);
    }

    public TextView getTrackingTitle() {
        TextView title = findViewById(R.id.edit_title);
        return title;
    }

    public TextView getMeetLocation() {
        TextView location = findViewById(R.id.meet_location_text);
        return location;
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
