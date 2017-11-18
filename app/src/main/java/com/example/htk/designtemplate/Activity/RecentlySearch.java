package com.example.htk.designtemplate.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.htk.designtemplate.R;

public class RecentlySearch extends AppCompatActivity {
    private ImageView searchIcon;
    private TextView searchTextView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_search);

        searchIcon = (ImageView) findViewById(R.id.searchImageIcon_searchActivity);
        searchTextView = (TextView) findViewById(R.id.searchTextView_searchActivity);
        context= getApplicationContext();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,SearchActivity.class);
                startActivity(intent);

            }
        };
        searchIcon.setOnClickListener(onClickListener);
        searchTextView.setOnClickListener(onClickListener);

    }
}
