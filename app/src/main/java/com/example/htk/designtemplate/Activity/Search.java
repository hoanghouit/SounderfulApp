package com.example.htk.designtemplate.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.htk.designtemplate.R;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView backImage;
    private EditText searchEditText;
    private ImageView clearIcon;
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
        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                int lengthText = searchEditText.getText().length();
                if(lengthText > 0)
                {
                    clearIcon.setVisibility(View.VISIBLE);
                }
                else
                    clearIcon.setVisibility(View.GONE);
                return false;
            }
        });

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AccountSearch_Fragment(), "Mọi người");
        adapter.addFragment(new TrackSearch_Fragment(), "Hàng đầu");
        viewPager.setAdapter(adapter);
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

    public void setActionBar(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_searchActivity);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
