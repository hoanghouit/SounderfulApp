package com.example.htk.designtemplate.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.htk.designtemplate.Adapter.PostAdapter;
import com.example.htk.designtemplate.Model.Post;
import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Service.ApiUtils;
import com.example.htk.designtemplate.Service.PostService;
import com.example.htk.designtemplate.Utils.MultipleToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity {
    private static final String TAG = "PostActivity";
    private Context context = PostActivity.this;
    private static final int ACTIVITY_NUM = 0;
    private ArrayList<Post> postArray= new ArrayList<Post>() ;
    private PostAdapter postArrayAdapter;
    private ListView listView;
    private PostService mService;
    protected static String userName;
    public static String fail_request;
    private int postId;
    private ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        // set context for toasts
        MultipleToast.context = this;
        // set string
        fail_request = getResources().getString(R.string.request_fail);
        // set postId
        postId = getIntent().getIntExtra("postId", 0);

        // set adapter for list view
        listView = (ListView) findViewById(R.id.listView_postActivity);
        postArrayAdapter = new PostAdapter(this,R.layout.item_post_listview,postArray);
        listView.setAdapter(postArrayAdapter);

        // set user name
        SharedPreferences sharedPreferences= getSharedPreferences("user",Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("userName","");

        backButton = (ImageView) findViewById(R.id.backButton_PostActivity);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // set retrofit service
        mService = ApiUtils.getPostService();

        // load posts
        loadPost();
    }


    @Override
    public void onPause(){
        super.onPause();
        //tắt hiệu ứng khi chuyển activity
        overridePendingTransition(0, 0);
    }

    public void loadPost(){
        mService.getOnePost(postId).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if(response.isSuccessful()) {
                    List<Post> postList = new ArrayList<Post>();
                    postList.add(response.body());
                    getLikedPosts(postList);
                    Log.d(TAG, "posts loaded from API");
                }else {
                    int statusCode  = response.code();
                    Log.d(TAG, "fail loaded from API");
                    Log.d(TAG, ((Integer)statusCode).toString());
                    MultipleToast.showToast(fail_request);
                }
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.d(TAG,"fail");

                MultipleToast.showToast(fail_request);
            }
        });
    }

    public void getLikedPosts(final List<Post> postList){
        mService.getLikedPosts(userName).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful()) {
                    ArrayList<Integer> arr = new ArrayList<Integer>();
                    for(Post p: response.body()){
                        arr.add( p.getPostId());
                    }
                    postArrayAdapter.setLikedPostIds(arr);
                    Log.d(TAG, "posts loaded from API");
                }else {
                    int statusCode  = response.code();
                    Log.d(TAG, "fail loaded from API");
                    Log.d(TAG, ((Integer)statusCode).toString());
                    MultipleToast.showToast(fail_request);
                }
                postArrayAdapter.clear();
                postArrayAdapter.addAll(postList);

            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d(TAG,"fail");
                MultipleToast.showToast(fail_request);
            }
        });
    }
}
