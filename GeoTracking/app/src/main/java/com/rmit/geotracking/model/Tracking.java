package com.rmit.geotracking.model;

import java.util.Date;

/**
 *  Tracking interface represent a single tracking protocol.
 *
 */

public interface Tracking extends Comparable<Tracking> {
    String getTrackingId(); //unique trackingID

    int getTrackableId();

    String getTitle();

    Date getTargetStartTime();

    Date getTargetEndTime();

    Date getMeetTime();

    String getCurrentLocation();

    String getMeetLocation();

    //A tracking is able to edit itself in model.
    void editTrackingInfo(String title, Date startTime, Date endTime, Date meetTime, String currentLocation, String meetLocation);
}
