package com.example.htk.designtemplate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by HTK on 12/2/2017.
 */

public class Like {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("postId")
    @Expose
    private Post post;

    @SerializedName("userName")
    @Expose
    private Account account;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
