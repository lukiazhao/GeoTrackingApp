package com.rmit.geotracking.model;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Observable;


public class SimpleTracking extends Observable implements Tracking, Comparable<Tracking> {

    private static final Integer LENGTH = 4 ;
    private String trackingId;
    private int trackableId;
    private String title;
    private Date targetStartTime;
    private Date targetEndTime;
    private Date meetTime;
    private String currentLocation;
    private String meetLocation;

    public SimpleTracking(int trackableId, String title,
                          Date targetStartTime, Date targetEndTime, Date meetTime,
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
    public Date getMeetTime() {
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

    @Override
    public void editTrackingInfo(String title, Date startTime, Date endTime, Date meetTime, String currentLocation, String meetLocation) {
        this.title = title;
        this.targetStartTime = startTime;
        this.targetEndTime = endTime;
        this.meetTime = meetTime;
        this.currentLocation = currentLocation;
        this.meetLocation = meetLocation;

    }


    public int compareTo(Tracking tracking1) {
        if(this.meetTime.compareTo(tracking1.getMeetTime()) >= 0) {
            return 1;
        } else {
            return -1;
        }
    }

}
