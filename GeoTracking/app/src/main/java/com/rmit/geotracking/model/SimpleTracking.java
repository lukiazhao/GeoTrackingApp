package com.rmit.geotracking.model;

public class SimpleTracking implements Tracking {
    private String trackingId;
    private int trackableId;
    private String title;
    private String targetStartTime;
    private String targetEndTime;
    private String meetTime;
    private String currentLocation;
    private String meetLocation;

    public SimpleTracking(String trackingId, int trackableId, String title,
                          String targetStartTime, String targetEndTime, String meetTime,
                          String currentLocation, String meetLocation) {
        this.trackingId = trackingId;
        this.trackableId = trackableId;
        this.title = title;
        this.targetStartTime = targetStartTime;
        this.targetEndTime = targetEndTime;
        this.meetTime = meetTime;
        this.currentLocation = currentLocation;
        this.meetLocation = meetLocation;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public int getTrackableId() {
        return trackableId;
    }

    public String getTitle() {
        return title;
    }

    public String getTargetStartTime() {
        return targetStartTime;
    }

    public String getTargetEndTime() {
        return targetEndTime;
    }

    public String getMeetTime() {
        return meetTime;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public String getMeetLocation() {
        return meetLocation;
    }


}
