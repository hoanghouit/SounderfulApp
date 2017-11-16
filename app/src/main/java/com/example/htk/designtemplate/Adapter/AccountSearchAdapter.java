package com.example.htk.designtemplate.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.htk.designtemplate.Activity.FriendWall;
import com.example.htk.designtemplate.Model.Account;
import com.example.htk.designtemplate.R;

import java.util.List;

/**
 * Created by HTK on 11/16/2017.
 */

public class AccountSearchAdapter extends ArrayAdapter<Account> {
    private Activity context;
    public AccountSearchAdapter(Activity context, int layoutID, List<Account> objects) {
        super(context, layoutID, objects);
        this.context = context;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_account_search_listview, null,
                    false);
        }


        // Get item
        final Account account = getItem(position);

        // Get view
        TextView userName = (TextView) convertView.findViewById(R.id.usernameTextView_accountSearch);
        TextView biography = (TextView) convertView.findViewById(R.id.biographyTextView_accountSearch);
        ImageView avatar = (ImageView) convertView.findViewById(R.id.avatarImage_accountSearch);


        // Set username
        userName.setText(account.getUserName());
        // Set biography
        biography.setText(account.getBiography());

        // Set avater image
        String url= account.getUrlAvatar();
        Glide.with(context).load(url).apply(RequestOptions.circleCropTransform().placeholder(R.mipmap.ic_launcher_round)).into(avatar);

        // set action for clicking view
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FriendWall.class);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
