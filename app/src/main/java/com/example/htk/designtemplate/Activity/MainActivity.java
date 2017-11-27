package com.example.htk.designtemplate.Activity;

import android.content.Context;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up custom action bar
        setActionBar();

        // set adapter for list view
        listView = (ListView) findViewById(R.id.listView);
        postArrayAdapter = new PostAdapter(this,R.layout.item_post_listview,postArray);
        listView.setAdapter(postArrayAdapter);

        //setupBottomNavigationView
        BottomNavigationViewHelper.setupBottomNavigationView(this,ACTIVITY_NUM);

        // set retrofit service
         mService = ApiUtils.getPostService();
         loadPost();

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

    public void loadPost(){
        mService.getPostsNewsfeed("anhtoan441996").enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if(response.isSuccessful()) {
                    //itemArrayList = new ArrayList<Item>(response.body().getItems());
                    postArrayAdapter.addAll(response.body());
                    Log.d("MainActivity", "posts loaded from API");
                }else {
                    int statusCode  = response.code();
                    Log.d("MainActivity", "fail loaded from API");
                    Log.d("MainActivity", ((Integer)statusCode).toString());
                    // handle request errors depending on status code
                }
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d("MainActivity", t.getMessage());
            }
        });
    }

}
