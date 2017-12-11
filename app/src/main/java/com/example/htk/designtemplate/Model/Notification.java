package com.example.htk.designtemplate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by HTK on 11/17/2017.
 */

public class Notification {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("userName")
    @Expose
    private Account account;

    @SerializedName("postId")
    @Expose
    private Post post;

    @SerializedName("action")
    @Expose
    private String action;

    @SerializedName("notificationTime")
    @Expose
    private Date notificationTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(Date notificationTime) {
        this.notificationTime = notificationTime;
    }
}
