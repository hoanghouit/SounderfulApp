package com.example.htk.designtemplate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by HTK on 11/18/2017.
 */

public class Comment {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("userName")
    @Expose
    private Account account;

    @SerializedName("postId")
    @Expose
    private Post post;

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
