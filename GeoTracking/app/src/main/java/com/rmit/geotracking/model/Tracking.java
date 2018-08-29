package com.rmit.geotracking.model;

import java.util.Date;

public interface Tracking extends Comparable<Tracking> {
    String getTrackingId();

    int getTrackableId();

    String getTitle();

    Date getTargetStartTime();

    Date getTargetEndTime();

    Date getMeetTime();

    String getCurrentLocation();

    String getMeetLocation();

    void editTracking(String title, Date startTime, Date endTime, Date meetTime);
}
