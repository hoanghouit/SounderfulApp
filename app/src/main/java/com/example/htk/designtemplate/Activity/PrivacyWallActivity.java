package com.example.htk.designtemplate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.htk.designtemplate.Adapter.PostAdapter;
import com.example.htk.designtemplate.Model.Account;
import com.example.htk.designtemplate.Model.Post;
import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Service.AccountService;
import com.example.htk.designtemplate.Service.ApiUtils;
import com.example.htk.designtemplate.Service.PostService;
import com.example.htk.designtemplate.Utils.BottomNavigationViewHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacyWallActivity extends AppCompatActivity {
    private static final int ACTIVITY_NUM = 4;
    private ArrayList<Post> postArray = new ArrayList<Post>() ;
    private PostAdapter postArrayAdapter;
    private ListView listView;
    private ImageView backImage;
    private TextView userNameTextView;
    private TextView biographyTextView;
    private ImageView avatar;
    private ImageView background;
    private TextView postNumberTextView;
    private TextView followerTextView;
    private TextView followingTextView;
    private ImageView menu;
    private AccountService accountService;
    private String userName;
    private PostService mService;
    private Activity context =this;
    private ArrayList<Integer> likedPostIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_wall);
        // set retrofit account service
        accountService = ApiUtils.getAccountService();

        // set user name
        SharedPreferences sharedPreferences= getSharedPreferences("user",Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("userName","");

        // Inflat header (user information) to listview
        listView = (ListView) findViewById(R.id.ListViewPost);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View head= inflater.inflate(R.layout.header_privacy_wall,listView,false);
        loadUserInfo();
        listView.addHeaderView(head);

         // set adapter for list view
        postArrayAdapter = new PostAdapter(this,R.layout.item_post_listview, postArray);
        listView.setAdapter(postArrayAdapter);

        // add post for newsfeed
        //addPost();
        // set retrofit service
        mService = ApiUtils.getPostService();
        loadPost();

        //set action for back button
        backImage = (ImageView) findViewById(R.id.backImage);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //set menu
        menu = (ImageView) findViewById(R.id.menuImage);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(menu);
            }
        });


        //setupBottomNavigationView
        BottomNavigationViewHelper.setupBottomNavigationView(this,ACTIVITY_NUM);

    }
    @Override
    public void onPause(){
        super.onPause();
        //tắt hiệu ứng khi chuyển activity
        overridePendingTransition(0, 0);
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        loadPost();
    }

    public void loadUserInfo(){
        accountService.getAccountDetail(userName).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {

                if(response.isSuccessful()) {
                    setUserInfo(response.body());
                    Log.d("PrivacyWallActivity", "posts loaded from API");
                }else {
                    int statusCode  = response.code();
                    Log.d("PrivacyWallActivity", "fail loaded from API");
                    Log.d("PrivacyWallActivity", ((Integer)statusCode).toString());
                    // handle request errors depending on status code
                }
            }
            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Log.d("PrivacyWallActivity", "fail");
            }
        });
    }
    public void setUserInfo(Account account){
        userNameTextView = (TextView) findViewById(R.id.usernameTextViewPrivacyWall);
        biographyTextView = (TextView) findViewById(R.id.biographyTextView);
        avatar = (ImageView) findViewById(R.id.avatarImagePrivacyWall);
        background = (ImageView) findViewById(R.id.backGroundImage);
        postNumberTextView = (TextView) findViewById(R.id.postNumberTextView);
        followerTextView = (TextView) findViewById(R.id.followerNumberTextView);
        followingTextView = (TextView) findViewById(R.id.followingNumberTextView);

        userNameTextView.setText(MainActivity.userName);
        biographyTextView.setText(account.getBiography());
        // Set avatar image
        String url= ApiUtils.getImageUrl(account.getUrlAvatar());
        Glide.with(this).load(url).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).override(500,500).circleCrop().error(R.mipmap.ic_avatar_error)).into(avatar);

        String url_background= ApiUtils.getImageUrl(account.getUrlBackground());
        Glide.with(this).load(url_background).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).centerCrop().error(R.color.colorLittleGray)).into(background);

        postNumberTextView.setText(getNumber(account.getPostNumber()));
        followerTextView.setText(getNumber(account.getFollowerNumber()));
        followingTextView.setText(getNumber(account.getFollowNumber()));
    }

    public void loadPost(){
        mService.getPostsWall(userName).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if(response.isSuccessful()) {
                    getLikedPosts(response.body());
                    Log.d("PrivacyWallActivity", "posts loaded from API");
                }else {
                    int statusCode  = response.code();
                    Log.d("PrivacyWallActivity", "fail loaded from API");
                    Log.d("PrivacyWallActivity", ((Integer)statusCode).toString());
                    // handle request errors depending on status code
                }
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d("PrivacyWallActivity", "fail");
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
                    Log.d("MainActivity", "posts loaded from API");
                }else {
                    int statusCode  = response.code();
                    Log.d("MainActivity", "fail loaded from API");
                    Log.d("MainActivity", ((Integer)statusCode).toString());
                    // handle request errors depending on status code
                }
                postArrayAdapter.addAll(postList);
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d("MainActivity","fail");
            }
        });
    }
    public String getNumber(int number){
        int n;
        if(number<999){
            return Integer.toString(number);
        }
        else{
            if(number<999999){
                n = number/1000;
                return Integer.toString(n).concat("K");
            }
            else{
                if(number<999999999){
                    n = number/1000000;
                    return Integer.toString(n).concat("Tr");
                }
                else{
                    n = number/1000000000;
                    return Integer.toString(n).concat("T");
                }
            }
        }

    }
    public void showMenu (View view)
    {
        PopupMenu menu = new PopupMenu (context, view);
        menu.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener ()
        {
            @Override
            public boolean onMenuItemClick (MenuItem item)
            {
                int id = item.getItemId();
                switch (id)
                {
                    case R.id.item_logout:{
                        SharedPreferences sharedPreferences= getSharedPreferences("user",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userName","");
                        editor.commit();
                        Intent intent = new Intent(context,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;}

                }
                return true;
            }
        });
        menu.inflate (R.menu.popup_menu_privacy_wall);
        menu.show();
    }

}
