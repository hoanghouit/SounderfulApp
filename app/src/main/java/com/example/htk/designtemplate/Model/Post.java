package com.example.htk.designtemplate.Model;

/**
 * Created by HTK on 11/12/2017.
 */

public class Post {
    private int postId;
    private String title;
    private String userName;
    private String dateTime;
    private String urlImage;
    private String urlTrack;
    private String description;
    private String listenNumber;


    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getUrlTrack() {
        return urlTrack;
    }

    public void setUrlTrack(String urlTrack) {
        this.urlTrack = urlTrack;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getListenNumber() {
        return listenNumber;
    }

    public void setListenNumber(String listenNumber) {
        this.listenNumber = listenNumber;
    }
}
