package com.rmit.geotracking.view;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rmit.geotracking.R;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Trackable;
import com.rmit.geotracking.model.TrackingInfoProcessor;

import java.util.List;
import java.util.Map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TrackManager manager = TrackManager.getSingletonInstance(this);
    private Map<Integer, Trackable> trackableMap = manager.getTrackableMap();
    private int trackableID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            trackableID = extras.getInt("trackableID");
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoomLevel = 15.0f;
        List<TrackingInfoProcessor.Pair> routeList = manager.getTrackingInfoProcessor().createRouteList(trackableID);
        for(TrackingInfoProcessor.Pair route: routeList){
            LatLng routePoint = new LatLng((Double) route.getFirstAttribute(), (Double) route.getSecondAttribute());
            mMap.addMarker((new MarkerOptions().position(routePoint)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(routePoint, zoomLevel));
        }
    }
}
