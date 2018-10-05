package com.rmit.geotracking;

public final class APIs {

    private static String baseUrl = "https://maps.googleapis.com/maps/api/distancematrix";
    private static String APIKey = "AIzaSyDWSXxSpQiv5N68Ax3WT-FwUFrQ_a3dUbc";

    private static class LazyHolder
    {
        static final APIs INSTANCE = new APIs();
    }

    // singleton
    public static APIs getSingletonInstance()
    {
        return LazyHolder.INSTANCE;
    }

}
