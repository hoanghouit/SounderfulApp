package com.example.htk.designtemplate.Service;

import com.example.htk.designtemplate.Activity.SignUpActivity;
import com.example.htk.designtemplate.Model.Account;
import com.example.htk.designtemplate.Model.FollowingModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @GET("accounts/checkfollow/{userNameA}/follow/{userNameB}")
    Call<SignUpActivity.existsUser> checkFollow(@Path("userNameA") String userNameA, @Path("userNameB") String userNameB);

    @FormUrlEncoded
    @POST("api/followings/")
    Call<FollowingModel> createFollow(@Field("userNameA") String userNameA, @Field("userNameB") String userNameB);

    @FormUrlEncoded
    @POST("accounts/follow/delete/")
    Call<ResponseBody> deleteFollow(@Field("usernameA") String userNameA, @Field("usernameB") String userNameB);

    @Multipart
    @POST("accounts/update/avatar/")
    Call<ResponseBody> updateAvatar(@Part MultipartBody.Part file, @Part("username") String username);


    @Multipart
    @POST("accounts/update/background/")
    Call<ResponseBody> updateBackground(@Part MultipartBody.Part file, @Part("username") String username);
}
