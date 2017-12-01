package com.example.htk.designtemplate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by HTK on 11/30/2017.
 */

public class PostModel {
    @SerializedName("id")
    @Expose
    private int postId;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("userName")
    @Expose
    private String userName;

    @SerializedName("postTime")
    @Expose
    private Date dateTime;

    @SerializedName("urlImage")
    @Expose
    private String urlImage;

    @SerializedName("urlTrack")
    @Expose
    private String urlTrack;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("listenNumber")
    @Expose
    private int listenNumber;

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


    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
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

    public int getListenNumber() {
        return listenNumber;
    }

    public void setListenNumber(int listenNumber) {
        this.listenNumber = listenNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
