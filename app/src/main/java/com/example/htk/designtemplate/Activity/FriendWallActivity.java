package com.example.htk.designtemplate.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

public class FriendWallActivity extends AppCompatActivity {

    private ArrayList<Post> postArray = new ArrayList<Post>() ;
    private PostAdapter postArrayAdapter;
    private ListView listView;
    private ImageView backImage;
    private Button followButton;
    private AccountService accountService;
    private String userNameGlobal;
    private View header;
    private Intent intent;
    private PostService mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_wall);
        // set up intent
        intent = getIntent();

        //
        userNameGlobal = intent.getStringExtra("userName");
        // set retrofit account service
        accountService = ApiUtils.getAccountService();

        // Inflat header (user information) to listview
        listView = (ListView) findViewById(R.id.ListViewPost_friendWall);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        header= inflater.inflate(R.layout.header_friend_wall,listView,false);
        loadUserInfo();
        listView.addHeaderView(header);

        // set adapter for list view
        postArrayAdapter = new PostAdapter(this,R.layout.item_post_listview, postArray);
        listView.setAdapter(postArrayAdapter);

        // add post for newsfeed
       // addPost();

        // set retrofit service
        mService = ApiUtils.getPostService();
        loadPost();


        //set action for back button
        backImage = (ImageView) findViewById(R.id.backImage_friendwall);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //set action for follow button
        followButton = (Button) findViewById(R.id.followButton_friendwall);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(followButton.getText()==getString(R.string.following)){
                    followButton.setBackgroundResource(R.color.colorWhite);
                    followButton.setTextColor(getResources().getColor(R.color.colorDark));
                    followButton.setText(R.string.follow);
                }
                else
                {
                    followButton.setBackgroundResource(R.color.colorBlue);
                    followButton.setTextColor(getResources().getColor(R.color.colorWhite));
                    followButton.setText(R.string.following);
                }
            }
        });

        //setupBottomNavigationView
        int ACTIVITY_NUM = intent.getIntExtra("indexActivity",1);
        BottomNavigationViewHelper.setupBottomNavigationView(this,ACTIVITY_NUM);
    }

    public void loadUserInfo(){
        accountService.getAccountDetail(userNameGlobal).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {

                if(response.isSuccessful()) {
                    setUserInfo(response.body());
                    Log.d("FriendWallActivity", "posts loaded from API");
                }else {
                    int statusCode  = response.code();
                    Log.d("FriendWallActivity", "fail loaded from API");
                    Log.d("FriendWallActivity", ((Integer)statusCode).toString());
                    // handle request errors depending on status code
                }
            }
            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Log.d("FriendWallActivity", t.getMessage());
            }
        });
    }
    public void setUserInfo(Account account){
        TextView userName = (TextView) findViewById(R.id.usernameTextView_friendwall);
        TextView biography = (TextView) findViewById(R.id.biographyTextView_friendwall);
        ImageView avatar = (ImageView) findViewById(R.id.avatarImage_friendwall);
        ImageView background = (ImageView) findViewById(R.id.backGroundImage_friendwall);
        TextView postNumber = (TextView) findViewById(R.id.postNumberTextView_friendwall);
        TextView follower = (TextView) findViewById(R.id.followerNumberTextView_friendwall);
        TextView following = (TextView) findViewById(R.id.followingNumberTextView_friendwall);

        userName.setText(userNameGlobal);
        biography.setText(account.getBiography());
        // Set avatar image
        String url= ApiUtils.getImageUrl(account.getUrlAvatar());
        Glide.with(this).load(url).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).override(500,500).circleCrop().error(R.mipmap.ic_avatar_error)).into(avatar);

        String url_background= ApiUtils.getImageUrl(account.getUrlBackground());
        Glide.with(this).load(url_background).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).error(R.color.colorLittleGray)).into(background);

        postNumber.setText(getNumber(account.getPostNumber()));
        follower.setText(getNumber(account.getFollowerNumber()));
        following.setText(getNumber(account.getFollowNumber()));

    }

    public void loadPost(){
        mService.getPostsWall(userNameGlobal).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if(response.isSuccessful()) {
                    //itemArrayList = new ArrayList<Item>(response.body().getItems());
                    postArrayAdapter.addAll(response.body());
                    Log.d("FriendWallActivity", "posts loaded from API");
                }else {
                    int statusCode  = response.code();
                    Log.d("FriendWallActivity", "fail loaded from API");
                    Log.d("FriendWallActivity", ((Integer)statusCode).toString());
                    // handle request errors depending on status code
                }
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d("FriendWallActivity", "fail");
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
}
