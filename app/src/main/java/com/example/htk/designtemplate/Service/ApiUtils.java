package com.example.htk.designtemplate.Service;


/**
 *  Created by HTK on 11/23/2017.
 */

public class ApiUtils {

    public static final String BASE_URL = "http://10.0.3.2:8000/";
    //public static final String BASE_URL = "http://192.168.42.136:8000/";

    public static PostService getPostService() {
        return RetrofitClient.getClient(BASE_URL).create(PostService.class);
    }
    public static AccountService getAccountService() {
        return RetrofitClient.getClient(BASE_URL).create(AccountService.class);
    }
    public static String getImageUrl(String filename){
        return BASE_URL + "download/image/" + filename +"/";
    }
    public static String getTrackUrl(String filename){
        return BASE_URL + "download/audio/" + filename +"/";
    }

}
