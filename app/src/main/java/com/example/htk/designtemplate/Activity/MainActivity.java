package com.example.htk.designtemplate.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import com.example.htk.designtemplate.Adapter.PostAdapter;
import com.example.htk.designtemplate.Model.Post;
import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Service.ApiUtils;
import com.example.htk.designtemplate.Service.PostService;
import com.example.htk.designtemplate.Utils.BottomNavigationViewHelper;
import com.example.htk.designtemplate.Utils.MultipleToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Context context = MainActivity.this;
    private static final int ACTIVITY_NUM = 0;
    private ArrayList<Post> postArray= new ArrayList<Post>() ;
    private PostAdapter postArrayAdapter;
    private ListView listView;
    private Toolbar myToolbar;
    private PostService mService;
    protected static String userName;
    private ProgressDialog progressDialog;
    public static String fail_request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // set context for toasts
        MultipleToast.context = this;
        // set string
        fail_request = getResources().getString(R.string.request_fail);

        // set up custom action bar
        setActionBar();

        //setupBottomNavigationView
        BottomNavigationViewHelper.setupBottomNavigationView(this,ACTIVITY_NUM);

        // set adapter for list view
        listView = (ListView) findViewById(R.id.listView);
        postArrayAdapter = new PostAdapter(this,R.layout.item_post_listview,postArray);
        listView.setAdapter(postArrayAdapter);

        // set user name
        SharedPreferences sharedPreferences= getSharedPreferences("user",Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("userName","");

        // set progress dialog
        createProgressDialog();
        progressDialog.show();

        // set retrofit service
         mService = ApiUtils.getPostService();
    }

    public void setActionBar(){
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    @Override
    public void onPause(){
        super.onPause();
        //tắt hiệu ứng khi chuyển activity
        overridePendingTransition(0, 0);
    }
    @Override
    protected void onResume(){
        super.onResume();
        progressDialog.show();
        loadPost();
    }
    public void loadPost(){
        mService.getPostsNewsfeed(userName).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if(response.isSuccessful()) {
                    getLikedPosts(response.body());
                    Log.d(TAG, "posts loaded from API");
                }else {
                    int statusCode  = response.code();
                    Log.d(TAG, "fail loaded from API");
                    Log.d(TAG, ((Integer)statusCode).toString());
                    MultipleToast.showToast(fail_request);
                }
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d(TAG,"fail");
                progressDialog.dismiss();
                MultipleToast.showToast(fail_request);
            }
        });
    }
    public void createProgressDialog(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading ...");
        progressDialog.setProgress(0);
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
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d(TAG,"fail");
                progressDialog.dismiss();
                MultipleToast.showToast(fail_request);
            }
        });
    }
}
