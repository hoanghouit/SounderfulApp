package com.example.htk.designtemplate.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.htk.designtemplate.Activity.MainActivity;
import com.example.htk.designtemplate.Activity.NotificationActivity;
import com.example.htk.designtemplate.Activity.PrivacyWallActivity;
import com.example.htk.designtemplate.Activity.SearchActivity;
import com.example.htk.designtemplate.R;

import java.lang.reflect.Field;

/**
 * Created by HTK on 11/18/2017.
 */

public class BottomNavigationViewHelper {

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);

            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                item.setPadding(0,25,0,0);
                item.setChecked(item.getItemData().isChecked());

            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }
    public static void enableNavigation(final Context context, BottomNavigationView view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.ic_house:
                        Intent intent = new Intent(context, MainActivity.class);

                        context.startActivity(intent);

                        break;
                    case R.id.ic_search:
                        Intent intent1 = new Intent(context, SearchActivity.class);
                        context.startActivity(intent1);
                        break;
                    case R.id.ic_post:
                        break;
                    case R.id.ic_notification:
                        Intent intent3 = new Intent(context, NotificationActivity.class);
                        context.startActivity(intent3);
                        break;
                    case R.id.ic_profile:
                        Intent intent4 = new Intent(context, PrivacyWallActivity.class);
                        context.startActivity(intent4);
                        break;
                }
                return false;
            }
        });
    }
    public static void setupBottomNavigationView(Activity context, int indexActivity){
        BottomNavigationView bottomNavigationView = (BottomNavigationView) context.findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(context, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(indexActivity);
        menuItem.setChecked(true);
    }
}

