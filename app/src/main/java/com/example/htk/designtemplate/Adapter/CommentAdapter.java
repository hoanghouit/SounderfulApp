package com.example.htk.designtemplate.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.htk.designtemplate.Activity.FriendWallActivity;
import com.example.htk.designtemplate.Activity.MainActivity;
import com.example.htk.designtemplate.Activity.NotificationActivity;
import com.example.htk.designtemplate.Activity.PrivacyWallActivity;
import com.example.htk.designtemplate.Activity.SearchActivity;
import com.example.htk.designtemplate.Model.Comment;
import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Service.ApiUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by HTK on 11/18/2017.
 */

public class CommentAdapter extends ArrayAdapter<Comment> {
    private Activity context;
    private String currentUser;
    public CommentAdapter(Activity context, int layoutID, List<Comment> objects) {
        super(context, layoutID, objects);
        this.context = context;
        // set user name
        SharedPreferences sharedPreferences= context.getSharedPreferences("user", Context.MODE_PRIVATE);
        currentUser = sharedPreferences.getString("userName","");
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment_listview, null,
                    false);
        }

        // Get item
        final Comment comment = getItem(position);

        // Get view
        TextView commentText = (TextView) convertView.findViewById(R.id.commentTextView_commentActivity);
        TextView time = (TextView) convertView.findViewById(R.id.timeTextView_commentActivity);
        ImageView avatar = (ImageView) convertView.findViewById(R.id.avatarImage_commentActivity);



        // Set action
        String userName = comment.getAccount().getUserName();
        String content = comment.getContent();
        String textView = userName+" "+content;
        int userNameLength = userName.length();
        SpannableStringBuilder text = new SpannableStringBuilder(textView);
        text.setSpan(new android.text.style.StyleSpan(Typeface.BOLD),0,userNameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        commentText.setText(text);

        // Set avatar image
        String url= ApiUtils.getImageUrl((comment.getAccount().getUrlAvatar()));
        Glide.with(context).load(url).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).override(200,200).circleCrop().error(R.mipmap.ic_avatar_error)).into(avatar);

        // set time
        Date date = comment.getCommentTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        time.setText(simpleDateFormat.format(date));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visitWall(comment.getAccount().getUserName());
            }
        });

        return convertView;
    }
    public void visitWall(String userName){
        Intent intent;
        if(userName.equals(currentUser)){
            // if avatar clicked isn current user, start privacy wall activity ...
            intent = new Intent(context, PrivacyWallActivity.class);
        }
        else{
            // if avatar clicked isn't current user, start friend wall activity
            intent = new Intent(context, FriendWallActivity.class);
            int indexActivity = 0;
            if(context.getClass()== MainActivity.class){
                indexActivity = 0;
            }
            if(context.getClass()== SearchActivity.class){
                indexActivity = 1;
            }
            if(context.getClass()== NotificationActivity.class){
                indexActivity = 3;
            }
            if(context.getClass()== PrivacyWallActivity.class){
                indexActivity = 4;
            }
            intent.putExtra("indexActivity", indexActivity);
            intent.putExtra("userName",userName);
        }
        context.startActivity(intent);
    }

}