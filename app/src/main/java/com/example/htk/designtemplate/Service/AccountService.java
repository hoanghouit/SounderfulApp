package com.example.htk.designtemplate.Service;

import com.example.htk.designtemplate.Activity.SignUpActivity;
import com.example.htk.designtemplate.Model.Account;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by HTK on 11/23/2017.
 */

public interface AccountService {
    @GET("api/accounts/{userName}/")
    Call<Account> getAccountDetail(@Path("userName") String userName);

    @GET("accounts/search/{key}/")
    Call<List<Account>> searchAccount(@Path("key") String userName);

    @POST("api/accounts/")
    Call<Account> createAccount(@Body Account account);

    @GET("accounts/check/{userName}/")
    Call<SignUpActivity.existsUser> checkUserName(@Path("userName") String userName);

    @FormUrlEncoded
    @POST("accounts/checklogin/")
    Call<SignUpActivity.existsUser> login(@Field("userName") String userName, @Field("password") String password);
}