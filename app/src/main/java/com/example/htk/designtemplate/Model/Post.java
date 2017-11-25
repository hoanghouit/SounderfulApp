package com.example.htk.designtemplate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by HTK on 11/12/2017.
 */

public class Post {
    @SerializedName("id")
    @Expose
    private int postId;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("userName")
    @Expose
    private Account account;

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

    @SerializedName("num_of_like")
    @Expose
    private int likeNumber;

    @SerializedName("num_of_comment")
    @Expose
    private int commentNumber;

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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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

    public int getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }
}
