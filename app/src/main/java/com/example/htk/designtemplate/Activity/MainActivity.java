package com.example.htk.designtemplate.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.htk.designtemplate.Adapter.PostAdapter;
import com.example.htk.designtemplate.Model.Post;
import com.example.htk.designtemplate.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Post> postArray= new ArrayList<Post>() ;
    private PostAdapter postArrayAdapter;
    private ListView listView;
    private ImageView privacyWallImageIcon;
    private Context context;
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

        // set intent to privacy wall
        context = getApplicationContext();
        privacyWallImageIcon = (ImageView) findViewById(R.id.privacyWallImageIcon);
        privacyWallImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PrivacyWall.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        

    }
    public void addPost(){
        String url1="http://genknews.genkcdn.vn/2017/smile-emojis-icon-facebook-funny-emotion-women-s-premium-long-sleeve-t-shirt-1500882676711.jpg";
        String url_image="https://images.vexels.com/media/users/6821/74972/raw/1054e351afe112bca797a70d67d93f9e-purple-daisies-blue-background.jpg";
        Post p = new Post();
        p.setTitle("Mashup Em gái mưa (Hương Tràm) - Từ hôm nay (Chi Pu)| Ghitar version");
        p.setUrlAvatar(url1);
        p.setUrlImage(url_image);
        postArray.add(p);

        Post pi = new Post();
        pi.setTitle("em gái mưa");
        postArray.add(pi);
        Post pio = new Post();
        pio.setTitle("ngày em đến");
        postArray.add(pio);
    }

    public void setActionBar(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
