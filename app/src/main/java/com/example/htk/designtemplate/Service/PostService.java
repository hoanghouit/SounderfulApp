package com.example.htk.designtemplate.Service;

import com.example.htk.designtemplate.Model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by HTK on 11/23/2017.
 */

public interface PostService {
    @GET("api/posts/")
    Call<List<Post>> getPosts();

    @GET("posts/{userName}/follow/")
    Call<List<Post>> getPostsNewsfeed(@Path("userName") String userName);

    @GET("posts/{userName}/")
    Call<List<Post>> getPostsWall(@Path("userName") String userName);
}
