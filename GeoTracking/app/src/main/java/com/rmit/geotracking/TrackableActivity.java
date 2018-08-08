package com.rmit.geotracking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class TrackableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_view);

        /* Recycler view of trackables **/

        RecyclerView recyclerView = findViewById(R.id.recycler_view_trackables);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create an adapter object
        RecyclerAdapter adapter = new RecyclerAdapter(this);

        // associate the recycler view with the adapter
        recyclerView.setAdapter(adapter);
    }
}
