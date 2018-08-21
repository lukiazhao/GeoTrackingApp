package com.rmit.geotracking.model;

import java.security.SecureRandom;
import java.util.Date;

public class SimpleTracking implements Tracking {
    private static final Integer LENGTH = 4 ;
    private String trackingId;
    private int trackableId;
    private String title;
    private Date targetStartTime;
    private Date targetEndTime;
    private String meetTime;
    private String currentLocation;
    private String meetLocation;

    public SimpleTracking(String trackingId, int trackableId, String title,
                          Date targetStartTime, Date targetEndTime, String meetTime,
                          String currentLocation, String meetLocation) {
        this.trackingId = generateRandomString(LENGTH);
        this.trackableId = trackableId;
        this.title = title;
        this.targetStartTime = targetStartTime;
        this.targetEndTime = targetEndTime;
        this.meetTime = meetTime;
        this.currentLocation = currentLocation;
        this.meetLocation = meetLocation;
    }

    @Override
    public String getTrackingId() {
        return trackingId;
    }

    @Override
    public int getTrackableId() {
        return trackableId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Date getTargetStartTime() {
        return targetStartTime;
    }

    @Override
    public Date getTargetEndTime() {
        return targetEndTime;
    }

    @Override
    public String getMeetTime() {
        return meetTime;
    }

    @Override
    public String getCurrentLocation() {
        return currentLocation;
    }

    @Override
    public String getMeetLocation() {
        return meetLocation;
    }

    public String generateRandomString(int length){
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for( int i = 0; i < length; i++ ) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    @Override
    public String toString(){
        return " tracking id: " + this.trackingId + " ; trackable id: " + this.trackableId +
                "; title: " + title + "; meet time: "
                + this.meetTime + "; meet location: " + meetLocation;
    }

}
