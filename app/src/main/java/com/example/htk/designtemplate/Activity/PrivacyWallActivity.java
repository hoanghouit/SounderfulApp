package com.example.htk.designtemplate.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.htk.designtemplate.Adapter.PostAdapter;
import com.example.htk.designtemplate.Model.Account;
import com.example.htk.designtemplate.Model.Post;
import com.example.htk.designtemplate.R;

import java.util.ArrayList;

public class PrivacyWallActivity extends AppCompatActivity {
    private ArrayList<Post> postArray = new ArrayList<Post>() ;
    private PostAdapter postArrayAdapter;
    private ListView listView;
    private ImageView backImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_wall);

        // Inflat header (user information) to listview
        listView = (ListView) findViewById(R.id.ListViewPost);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View head= inflater.inflate(R.layout.header_privacy_wall,listView,false);
        listView.addHeaderView(head);

         // set adapter for list view
        postArrayAdapter = new PostAdapter(this,R.layout.item_post_listview, postArray);
        listView.setAdapter(postArrayAdapter);

        // add post for newsfeed
        addPost();

        //set action for back button
        backImage = (ImageView) findViewById(R.id.backImage);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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


}