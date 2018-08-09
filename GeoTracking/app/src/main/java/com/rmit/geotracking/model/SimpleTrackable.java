package com.rmit.geotracking.model;


public class SimpleTrackable implements Trackable {

    private int id;
    private String name;
    private String description;
    private String url;
    private String category;
    private String photo;

    public SimpleTrackable(int id, String name, String description, String url,
                           String category, String photo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.category = category;
        this.photo = photo;
    }

    // optional constructor
    public SimpleTrackable(int id, String name, String description, String url,
                           String category){
        this(id, name, description, url, category, null);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getPhoto() {
        return photo;
    }

    @Override
    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
