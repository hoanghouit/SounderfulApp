package com.example.htk.designtemplate.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.htk.designtemplate.Adapter.CommentAdapter;
import com.example.htk.designtemplate.Model.Account;
import com.example.htk.designtemplate.Model.Comment;
import com.example.htk.designtemplate.Model.Post;
import com.example.htk.designtemplate.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private List<Comment> commentArrayList = new ArrayList<Comment>() ;
    private CommentAdapter commentAdapter;
    private ListView listView;
    private Toolbar myToolbar;
    private ImageView backImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // set up action bar
        setActionBar();
        //set adapter for list view
        listView = (ListView) findViewById(R.id.listView_commentActivity);
        commentAdapter = new CommentAdapter(this, R.layout.item_comment_listview, commentArrayList);
        listView.setAdapter(commentAdapter);
        // add sample comment
        addSampleData();

        // set action for clicking back icon
        backImage = (ImageView) findViewById(R.id.backImage_commentActivity);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    public void setActionBar(){
        myToolbar = (Toolbar) findViewById(R.id.toolbar_commentActivity);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    public void addSampleData(){
        String url="http://genknews.genkcdn.vn/2017/smile-emojis-icon-facebook-funny-emotion-women-s-premium-long-sleeve-t-shirt-1500882676711.jpg";
        String url_image="https://images.vexels.com/media/users/6821/74972/raw/1054e351afe112bca797a70d67d93f9e-purple-daisies-blue-background.jpg";
        String action1 ="đã thích bài đăng của bạn";
        String action2 ="đã bình luận bài đăng của bạn";
        Post p = new Post();
        p.setTitle("Mashup Em gái mưa (Hương Tràm) - Từ hôm nay (Chi Pu)| Ghitar version");
        p.setUrlImage(url_image);
        Account a=new Account();
        a.setUserName("hoanghtk3108cutelovely");
        p.setAccount(a);

        Post pi = new Post();
        pi.setTitle("em gái mưa");
        Account ai=new Account();
        ai.setUserName("yudaidang");
        pi.setAccount(ai);

        Post pio = new Post();
        pio.setTitle("Ngày em đến");
        pio.setAccount(a);

        Calendar c=Calendar.getInstance();
        Comment n = new Comment();
        n.setAccount(a);
        n.setPost(p);
        n.setContent("Bài hát hay lắm. Mình rất thích. Rất vui được nghe bài hát này. Chúc bạn một ngày vui vẻ hé. See you again. Moaxh . Love u");
        n.setTime(c.getTime());

        Comment na = new Comment();
        na.setAccount(ai);
        na.setPost(pi);
        na.setContent("Có tố chất làm ca sĩ lắm. cố gắng nhé nha nhoe nhen nhe");
        na.setTime(c.getTime());

        commentArrayList.add(n);
        commentArrayList.add(na);
        commentArrayList.add(n);
        commentArrayList.add(na);
        commentArrayList.add(n);
        commentArrayList.add(na);
        commentArrayList.add(n);
        commentArrayList.add(na);


    }
}
