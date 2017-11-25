package com.example.htk.designtemplate.Service;

import com.example.htk.designtemplate.Model.Account;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by HTK on 11/23/2017.
 */

public interface AccountService {
    @GET("api/accounts/{userName}/")
    Call<Account> getAccountDetail(@Path("userName") String userName);
}
