package com.example.htk.designtemplate.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.htk.designtemplate.Adapter.PostAdapter;
import com.example.htk.designtemplate.Service.AccountService;
import com.example.htk.designtemplate.Service.ApiUtils;
import com.example.htk.designtemplate.Service.PostService;
import com.example.htk.designtemplate.Utils.BottomNavigationViewHelper;
import com.example.htk.designtemplate.Model.Account;
import com.example.htk.designtemplate.Model.Post;
import com.example.htk.designtemplate.R;

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
    private ImageView avatarImage;
    private ImageView backgroundImage;
    private AccountService accountService;
    private String userName;
    private PostService mService;

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

        //setupBottomNavigationView
        BottomNavigationViewHelper.setupBottomNavigationView(this,ACTIVITY_NUM);

        // set sample avatar image
        String url1="https://scontent.fsgn5-4.fna.fbcdn.net/v/t1.0-9/21687485_1133775720087600_8541101764693327944_n.jpg?oh=b0b927dee62f2cec22aeb14a687e6814&oe=5A9A38C1";
        String url_image="https://scontent.fsgn5-4.fna.fbcdn.net/v/t1.0-9/18222582_887073868098395_7159416397014555794_n.jpg?oh=6d68e61d83b11b5f812e1533904d2f93&oe=5AAFE4A2";

        avatarImage = (ImageView) findViewById(R.id.avatarImagePrivacyWall);
        backgroundImage = (ImageView) findViewById(R.id.backGroundImage);
        Glide.with(this).load(url1).apply(RequestOptions.circleCropTransform().placeholder(R.mipmap.ic_launcher_round)).into(avatarImage);
        Glide.with(this).load(url_image).apply(RequestOptions.placeholderOf(R.mipmap.ic_launcher)).into(backgroundImage);
    }
    @Override
    public void onPause(){
        super.onPause();
        //tắt hiệu ứng khi chuyển activity
        overridePendingTransition(0, 0);
    }
    public void addPost(){
        String url1="http://genknews.genkcdn.vn/2017/smile-emojis-icon-facebook-funny-emotion-women-s-premium-long-sleeve-t-shirt-1500882676711.jpg";
        String url_image="https://images.vexels.com/media/users/6821/74972/raw/1054e351afe112bca797a70d67d93f9e-purple-daisies-blue-background.jpg";
        Post p = new Post();
        p.setTitle("Mashup Em gái mưa (Hương Tràm) - Từ hôm nay (Chi Pu)| Ghitar version");
        p.setUrlImage(url_image);
        Account a=new Account();
        a.setUserName("hoanghtk3108");
        p.setAccount(a);
        postArray.add(p);

        Post pi = new Post();
        pi.setTitle("em gái mưa");
        Account ai=new Account();
        ai.setUserName("hoanghtk3108");
        pi.setAccount(ai);
        postArray.add(pi);

        Post pio = new Post();
        pio.setTitle("Ngày em đến");
        pio.setAccount(ai);
        postArray.add(pio);
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
                Log.d("PrivacyWallActivity", t.getMessage());
            }
        });
    }
    public void setUserInfo(Account account){
        TextView userName = (TextView) findViewById(R.id.usernameTextViewPrivacyWall);
        TextView biography = (TextView) findViewById(R.id.biographyTextView);
        ImageView avatar = (ImageView) findViewById(R.id.avatarImagePrivacyWall);
        ImageView background = (ImageView) findViewById(R.id.backGroundImage);
        TextView postNumber = (TextView) findViewById(R.id.postNumberTextView);
        TextView follower = (TextView) findViewById(R.id.followerNumberTextView);
        TextView following = (TextView) findViewById(R.id.followingNumberTextView);

        userName.setText(account.getUserName());
        biography.setText(account.getBiography());
        // Set avatar image
        String url= account.getUrlAvatar();
        Glide.with(this).load(url).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).override(500,500).circleCrop().error(R.mipmap.ic_avatar_error)).into(avatar);

        String url_background= account.getUrlBackground();
        Glide.with(this).load(url_background).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).centerCrop().error(R.color.colorLittleGray)).into(background);

    }

    public void loadPost(){
        mService.getPostsWall(userName).enqueue(new Callback<List<Post>>() {
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
