package com.example.htk.designtemplate.Adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.htk.designtemplate.Model.Notification;
import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Service.ApiUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by HTK on 11/17/2017.
 */

public class NotificationAdapter extends ArrayAdapter<Notification> {
    private Activity context;
    public NotificationAdapter(Activity context, int layoutID, List<Notification> objects) {
        super(context, layoutID, objects);
        this.context = context;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_notification_listview, null,
                    false);
        }


        // Get item
        final Notification notification = getItem(position);

        // Get view
        TextView notificationText = (TextView) convertView.findViewById(R.id.noificationTextView_notificationActivity);
        TextView time = (TextView) convertView.findViewById(R.id.timeTextView_notificationActivity);
        ImageView avatar = (ImageView) convertView.findViewById(R.id.avatarImage_notificationActivity);



        // Set action
        String userName = notification.getAccount().getUserName();
        String action = notification.getAction();
        String content = userName+" "+action;
        int userNameLength = userName.length();
        SpannableStringBuilder text = new SpannableStringBuilder(content);
        text.setSpan(new android.text.style.StyleSpan(Typeface.BOLD),0,userNameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        notificationText.setText(text);

        // Set avatar image
        String url= ApiUtils.getImageUrl(notification.getAccount().getUrlAvatar());
        Glide.with(context).load(url).apply(RequestOptions.circleCropTransform().placeholder(R.mipmap.ic_launcher_round)).into(avatar);

        // set time
        Date date = notification.getNotificationTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        time.setText(simpleDateFormat.format(date));
        // set action for clicking view
/*        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FriendWallActivity.class);
                context.startActivity(intent);
            }
        });*/
        return convertView;
    }
}

