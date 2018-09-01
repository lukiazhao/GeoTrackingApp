package com.rmit.geotracking.model;

/**
 * An interface represents trackable protocol
 */

public interface Trackable {

    int getId(); // every Trackable should have unique ID

    void setId(int id);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    String getUrl();

    void setUrl(String url);

    String getCategory();

    void setCategory(String category);

    String getPhoto();

    void setPhoto(String photo);
}
