package com.example.htk.designtemplate.Model;

/**
 * Created by HTK on 11/14/2017.
 */

public class Account {
    private String userName;
    private String passWord;
    private String biography;
    private String urlAvatar;
    private String urlBackground;

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
}
