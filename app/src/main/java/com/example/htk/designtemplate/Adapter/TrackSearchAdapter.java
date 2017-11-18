package com.example.htk.designtemplate.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.htk.designtemplate.Model.Post;
import com.example.htk.designtemplate.R;

import java.util.List;

/**
 * Created by HTK on 11/16/2017.
 */

public class TrackSearchAdapter extends ArrayAdapter<Post> {
    private Activity context;
    public TrackSearchAdapter(Activity context, int layoutID, List<Post> objects) {
        super(context, layoutID, objects);
        this.context = context;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_track_search_listview, null,
                    false);
        }


        // Get item
        final Post post = getItem(position);

        // Get view
        TextView userName = (TextView) convertView.findViewById(R.id.userName_trackSearch);
        TextView title = (TextView) convertView.findViewById(R.id.titleTrack_trackSearch);
        ImageView image = (ImageView) convertView.findViewById(R.id.trackImage_trackSearch);


        // Set username
        userName.setText(post.getAccount().getUserName());
        // Set biography
        title.setText(post.getTitle());

        // Set avater image
        String url= post.getUrlImage();
        Glide.with(context).load(url).apply(RequestOptions.circleCropTransform().placeholder(R.mipmap.ic_launcher_round)).into(image);

        return convertView;
    }
}
