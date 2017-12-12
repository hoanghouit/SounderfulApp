package com.example.htk.designtemplate.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.htk.designtemplate.Adapter.NotificationAdapter;
import com.example.htk.designtemplate.Model.Notification;
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

public class NotificationActivity extends AppCompatActivity {
    private List<Notification> notificationArrayList= new ArrayList<Notification>() ;
    private NotificationAdapter notificationAdapter;
    private ListView listView;
    private Context context;
    private Toolbar myToolbar;
    private ImageView backImage;
    private static final int ACTIVITY_NUM = 3;
    private PostService mService;
    private String userName;
    private static final String tag = "notificationActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        // set action bar
        setActionBar();
        //set retrofit post service
        mService = ApiUtils.getPostService();

        // set user name
        SharedPreferences sharedPreferences= getSharedPreferences("user",Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("userName","");

        // set adapter for list view
        listView = (ListView) findViewById(R.id.listView_notificationActivity);
        notificationAdapter = new NotificationAdapter(this, R.layout.item_notification_listview, notificationArrayList);
        listView.setAdapter(notificationAdapter);

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

        loadNotifications();

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

    public void loadNotifications(){
        mService.getNotifications(userName).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if(response.isSuccessful()) {

                    notificationAdapter.clear();
                    notificationAdapter.addAll(response.body());
                    notificationAdapter.notifyDataSetChanged();
                    Log.d(tag, "get notifications from API");
                }else {
                    int statusCode  = response.code();
                    Log.d(tag, "fail get notifications from API");
                    Log.d(tag, ((Integer)statusCode).toString());
                    MultipleToast.showToast(MainActivity.fail_request);
                }
            }
            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Log.d(tag,"fail");
                //progressDialog.dismiss();
                MultipleToast.showToast(MainActivity.fail_request);
            }
        });
    }

}
