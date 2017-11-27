package com.example.htk.designtemplate.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Utils.BottomNavigationViewHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView backImage;
    private EditText searchEditText;
    private ImageView clearIcon;
    private Context context;
    private static final int ACTIVITY_NUM = 1;
    private AccountSearch_Fragment accountSearch_fragment = new AccountSearch_Fragment();
    private TrackSearch_Fragment trackSearch_fragment= new TrackSearch_Fragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // set up custom action bar
        setActionBar();

        // set up tab
        viewPager = (ViewPager) findViewById(R.id.viewpager_searchActivity);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs_searchActivity);
        tabLayout.setupWithViewPager(viewPager);

        // set action for back image icon
        backImage = (ImageView) findViewById(R.id.backImage_searchActivity);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // set action display/ hide clear icon on search edit text
        searchEditText = (EditText) findViewById(R.id.searchEditText_searchActivity);
        clearIcon = (ImageView) findViewById(R.id.clearImageIcon_searchActivity);
        context= getApplicationContext();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
               search();
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
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(accountSearch_fragment, getResources().getString(R.string.account));
        adapter.addFragment(trackSearch_fragment, getResources().getString(R.string.track));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                search();
            }

            @Override
            public void onPageSelected(int position) {
                search();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    public void search(){
        String  text = searchEditText.getText().toString().trim();
        if(text.length()>0)
        {
            clearIcon.setVisibility(View.VISIBLE);
            if(viewPager.getCurrentItem()==0){
                accountSearch_fragment.searchAccounts(text);
            }
            else{
                trackSearch_fragment.searchPosts(text);
            }
            SharedPreferences preferences= getSharedPreferences("searchActivity", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("keySearch",text);
            editor.commit();
        }
        else{
            clearIcon.setVisibility(View.GONE);
            if(viewPager.getCurrentItem()==0){
                accountSearch_fragment.recentlySearch();
            }
            else{
                trackSearch_fragment.recentlySearch();
            }

        }
    }
    public void setActionBar(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_searchActivity);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
