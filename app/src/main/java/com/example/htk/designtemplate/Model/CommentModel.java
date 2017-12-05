package com.example.htk.designtemplate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by HTK on 12/6/2017.
 */

public class CommentModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("userName")
    @Expose
    private String userName;

    @SerializedName("postId")
    @Expose
    private int postId;

    @SerializedName("context")
    @Expose
    private String Content;

    @SerializedName("commentTime")
    @Expose
    private Date commentTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }
}
