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
import com.example.htk.designtemplate.Model.Comment;
import com.example.htk.designtemplate.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by HTK on 11/18/2017.
 */

public class CommentAdapter extends ArrayAdapter<Comment> {
    private Activity context;
    public CommentAdapter(Activity context, int layoutID, List<Comment> objects) {
        super(context, layoutID, objects);
        this.context = context;
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
        String url= comment.getAccount().getUrlAvatar();
        Glide.with(context).load(url).apply(RequestOptions.circleCropTransform().placeholder(R.mipmap.ic_launcher_round)).into(avatar);

        // set time
        Date date = comment.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        time.setText(simpleDateFormat.format(date));

        return convertView;
    }
}