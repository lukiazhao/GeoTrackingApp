package com.rmit.geotracking.model;

public interface Trackable {

    int getId();

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
