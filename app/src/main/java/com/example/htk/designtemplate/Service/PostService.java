package com.example.htk.designtemplate.Service;

import com.example.htk.designtemplate.Model.Comment;
import com.example.htk.designtemplate.Model.CommentModel;
import com.example.htk.designtemplate.Model.LikeModel;
import com.example.htk.designtemplate.Model.Notification;
import com.example.htk.designtemplate.Model.Post;
import com.example.htk.designtemplate.Model.PostModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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

    @FormUrlEncoded
    @POST("posts/search/bytitle/")
    Call<List<Post>> searchPost(@Field("title") String title);

    @Multipart
    @POST("upload/image/")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);

    @Multipart
    @POST("upload/audio/")
    Call<ResponseBody> uploadTrack(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/posts/")
    Call<PostModel> createPost(@Field("userName") String userName, @Field("title") String title, @Field("description") String description);

    @PUT("api/posts/{id}/")
    Call<ResponseBody> updatePost(@Path("id") int id, @Body PostModel postModel);

    @DELETE("api/posts/{id}/")
    Call<ResponseBody> deletePost(@Path("id") int id);

    @GET("posts/{userName}/like/")
    Call<List<Post>> getLikedPosts(@Path("userName") String userName);

    @FormUrlEncoded
    @POST("api/likes/")
    Call<LikeModel> createLike(@Field("userName") String userName, @Field("postId") String postId);

    @FormUrlEncoded
    @POST("likes/delete/")
    Call<ResponseBody> deleteLike(@Field("username") String userName, @Field("postid") String postId);

    @GET("comments/{postid}/")
    Call<List<Comment>> getCommentsOfPost(@Path("postid") int postId);

    @FormUrlEncoded
    @POST("api/comments/")
    Call<CommentModel> createComment(@Field("userName") String userName, @Field("postId") int postId, @Field("context") String content);

    @GET("api/posts/{postid}")
    Call<Post> getOnePost(@Path("postid") int postId);

    @Multipart
    @POST("posts/update/image/")
    Call<ResponseBody> updateImage(@Part MultipartBody.Part file, @Part("postid") int postid);

    @FormUrlEncoded
    @POST("posts/update/info/")
    Call<ResponseBody> modifyPost(@Field("id") int postId, @Field("title") String title, @Field("description") String description);

    @GET("notifications/{username}/")
    Call<List<Notification>> getNotifications(@Path("username") String userName);

}
