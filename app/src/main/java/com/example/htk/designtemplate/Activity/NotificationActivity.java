package com.example.htk.designtemplate.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.htk.designtemplate.Adapter.NotificationAdapter;
import com.example.htk.designtemplate.Model.Account;
import com.example.htk.designtemplate.Model.Notification;
import com.example.htk.designtemplate.Model.Post;
import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Utils.BottomNavigationViewHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private List<Notification> notificationArrayList= new ArrayList<Notification>() ;
    private NotificationAdapter notificationAdapter;
    private ListView listView;
    private Context context;
    private Toolbar myToolbar;
    private ImageView backImage;
    private static final int ACTIVITY_NUM = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        // set action bar
        setActionBar();

        // set adapter for list view
        listView = (ListView) findViewById(R.id.listView_notificationActivity);
        notificationAdapter = new NotificationAdapter(this, R.layout.item_notification_listview, notificationArrayList);
        listView.setAdapter(notificationAdapter);

        // add sample notification
        addSampleData();
        // set action for clicking back icon
        backImage = (ImageView) findViewById(R.id.backImage_notificationActivity);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //setupBottomNavigationView
        BottomNavigationViewHelper.setupBottomNavigationView(this,ACTIVITY_NUM);

    }
    public void setActionBar(){
        myToolbar = (Toolbar) findViewById(R.id.toolbar_notificationActivity);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onPause(){
        super.onPause();
        //tắt hiệu ứng khi chuyển activity
        overridePendingTransition(0, 0);
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
        Notification n = new Notification();
        n.setAccount(a);
        n.setPost(p);
        n.setAction(action1);
        n.setNotificationTime(c.getTime());

        Notification na = new Notification();
        na.setAccount(ai);
        na.setPost(pi);
        na.setAction(action2);
        na.setNotificationTime(c.getTime());

        notificationArrayList.add(n);
        notificationArrayList.add(na);
        notificationArrayList.add(n);
        notificationArrayList.add(na);
        notificationArrayList.add(n);
        notificationArrayList.add(na);
        notificationArrayList.add(n);
        notificationArrayList.add(na);


    }
}
