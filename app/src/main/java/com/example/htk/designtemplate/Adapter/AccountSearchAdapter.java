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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.htk.designtemplate.Activity.FriendWallActivity;
import com.example.htk.designtemplate.Model.Account;
import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Service.ApiUtils;

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
        String url= ApiUtils.getImageUrl(account.getUrlAvatar());
        Glide.with(context).load(url).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).override(200,200).circleCrop().error(R.mipmap.ic_avatar_error)).into(avatar);

        // set action for clicking view
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FriendWallActivity.class);
                intent.putExtra("userName",account.getUserName());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
