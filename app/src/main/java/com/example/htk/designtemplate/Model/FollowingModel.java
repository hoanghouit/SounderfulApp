package com.example.htk.designtemplate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by HTK on 12/5/2017.
 */

public class FollowingModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("userNameA")
    @Expose
    private String userNameA;

    @SerializedName("userNameB")
    @Expose
    private String userNameB;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserNameA() {
        return userNameA;
    }

    public void setUserNameA(String userNameA) {
        this.userNameA = userNameA;
    }

    public String getUserNameB() {
        return userNameB;
    }

    public void setUserNameB(String userNameB) {
        this.userNameB = userNameB;
    }
}
