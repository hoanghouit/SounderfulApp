package com.example.htk.designtemplate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by HTK on 11/14/2017.
 */

public class Account {
    @SerializedName("userName")
    @Expose
    private String userName;

    @SerializedName("password")
    @Expose
    private String passWord;

    @SerializedName("biography")
    @Expose
    private String biography;

    @SerializedName("urlAvatar")
    @Expose
    private String urlAvatar;

    @SerializedName("urlBackgroundImage")
    @Expose
    private String urlBackground;

    @SerializedName("num_of_post")
    @Expose
    private int postNumber;

    @SerializedName("num_of_follower")
    @Expose
    private int followerNumber;

    @SerializedName("num_of_follow")
    @Expose
    private int followNumber;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public String getUrlBackground() {
        return urlBackground;
    }

    public void setUrlBackground(String urlBackground) {
        this.urlBackground = urlBackground;
    }

    public int getPostNumber() {
        return postNumber;
    }

    public void setPostNumber(int postNumber) {
        this.postNumber = postNumber;
    }

    public int getFollowerNumber() {
        return followerNumber;
    }

    public void setFollowerNumber(int followerNumber) {
        this.followerNumber = followerNumber;
    }

    public int getFollowNumber() {
        return followNumber;
    }

    public void setFollowNumber(int followNumber) {
        this.followNumber = followNumber;
    }
}
