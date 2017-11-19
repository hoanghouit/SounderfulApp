package com.example.htk.designtemplate.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.example.htk.designtemplate.Adapter.PostAdapter;
import com.example.htk.designtemplate.Utils.BottomNavigationViewHelper;
import com.example.htk.designtemplate.Model.Account;
import com.example.htk.designtemplate.Model.Post;
import com.example.htk.designtemplate.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Context context = MainActivity.this;
    private static final int ACTIVITY_NUM = 0;
    private ArrayList<Post> postArray= new ArrayList<Post>() ;
    private PostAdapter postArrayAdapter;
    private ListView listView;
    private Toolbar myToolbar;
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

        // add post for newsfeed
        addPost();

        // set sample current user
        SharedPreferences sharedPreferences= getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("userName","hoanghtk3108");
        editor.commit();

        //setupBottomNavigationView
        BottomNavigationViewHelper.setupBottomNavigationView(this,ACTIVITY_NUM);

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
        ai.setUserName("yudaidang");
        pi.setAccount(ai);
        postArray.add(pi);

        Post pio = new Post();
        pio.setTitle("Ngày em đến");
        pio.setAccount(a);
        postArray.add(pio);
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


}
