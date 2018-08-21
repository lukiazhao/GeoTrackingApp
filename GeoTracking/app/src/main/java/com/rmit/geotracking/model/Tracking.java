package com.rmit.geotracking.model;

import java.util.Date;

public interface Tracking {
    String getTrackingId();

    int getTrackableId();

    String getTitle();

    Date getTargetStartTime();

    Date getTargetEndTime();

    String getMeetTime();

    String getCurrentLocation();

    String getMeetLocation();
}
