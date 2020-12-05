package com.example.rxdeneme.Models;

import java.util.List;

public class Album {

    private int userId;
    private int id;
    private String title;
    private List<Photo> photos;

    public Album(int userId, int id, String title, List<Photo> photos) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.photos = photos;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

}
