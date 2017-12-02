package com.example.htk.designtemplate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by HTK on 12/1/2017.
 */

public class LikeModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("postId")
    @Expose
    private int postId;

    @SerializedName("userName")
    @Expose
    private String userName;

    public int getPostId() {
        return postId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
