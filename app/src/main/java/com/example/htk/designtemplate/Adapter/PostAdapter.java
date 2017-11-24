package com.example.htk.designtemplate.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.htk.designtemplate.Activity.CommentActivity;
import com.example.htk.designtemplate.Activity.FriendWallActivity;
import com.example.htk.designtemplate.Activity.MainActivity;
import com.example.htk.designtemplate.Activity.MusicPlayer;
import com.example.htk.designtemplate.Activity.NotificationActivity;
import com.example.htk.designtemplate.Activity.PrivacyWallActivity;
import com.example.htk.designtemplate.Activity.SearchActivity;
import com.example.htk.designtemplate.Model.Post;
import com.example.htk.designtemplate.R;

import java.util.List;

/**
 * Created by HTK on 11/12/2017.
 */

public class PostAdapter extends ArrayAdapter<Post> {
    private Activity context;
    String currentUser;

    public PostAdapter(Activity context, int layoutID, List<Post> objects) {
        super(context, layoutID, objects);
        this.context = context;
        // Get current user
        SharedPreferences sharedPreferences= context.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        currentUser = sharedPreferences.getString("userName","");

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_post_listview, null,
                    false);
        }


        // Get item
        final Post post = getItem(position);

        // Get view
        TextView title = (TextView) convertView.findViewById(R.id.titleTextView);
        TextView userName = (TextView) convertView.findViewById(R.id.usernameTextView);
        TextView dateTime = (TextView) convertView.findViewById(R.id.timeTextView);
        TextView description = (TextView) convertView.findViewById(R.id.descriptionTextView);
        TextView likeNumber = (TextView) convertView.findViewById(R.id.likeNumberTextView);
        TextView commentNumber = (TextView) convertView.findViewById(R.id.commentNumberTextView);
        TextView listenNumber = (TextView) convertView.findViewById(R.id.listenNumberTextView);
        ImageView avatar = (ImageView) convertView.findViewById(R.id.avatarImageView);
        ImageView imageTrack = (ImageView) convertView.findViewById(R.id.imageView);
        ImageView playMusic = (ImageView) convertView.findViewById(R.id.playMusic);
        final ImageView menu = (ImageView) convertView.findViewById(R.id.menuImage);
        ImageView commentIcon = (ImageView) convertView.findViewById(R.id.commentIconImage);
        SeekBar seekBar = (SeekBar) convertView.findViewById(R.id.trackTimeSeekBar);

        // set seek bar width full parent
        seekBar.setPadding(0,0,0,0);

        // set action for clicking avatar
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visitWall(post);
            }
        });

        // set action for clicking user name
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visitWall(post);
            }
        });

        playMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playMusic(post);
            }
        });

        // set action for clicking menu on each item list view
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if this is post of current user
                if(post.getAccount().getUserName().equals(currentUser)) {
                    showMenu(menu);
                }
                // if this is not post of current user, do nothing
            }
        });

        // Set title
        title.setText(post.getTitle());

        // Set username
        userName.setText(post.getAccount().getUserName());

        // Set avater image
        String url="";
        Glide.with(context).load(url).apply(RequestOptions.circleCropTransform().placeholder(R.mipmap.ic_launcher_round)).into(avatar);

        //Set track image
        String url_image=post.getUrlImage();
        Glide.with(context).load(url_image).apply(RequestOptions.placeholderOf(R.mipmap.ic_launcher)).into(imageTrack);

        // set action for clicking comment icon
        commentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                context.startActivity(intent);
            }
        });
        // Set other attribute of post ...


        return convertView;
    }
    public void showMenu (View view)
    {
            PopupMenu menu = new PopupMenu (context, view);
            menu.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener ()
            {
                @Override
                public boolean onMenuItemClick (MenuItem item)
                {
                    int id = item.getItemId();
                    switch (id)
                    {
                        case R.id.item_edit: Toast.makeText(context,"sửa",Toast.LENGTH_LONG).show(); break;
                        case R.id.item_delete: Toast.makeText(context,"xóa",Toast.LENGTH_LONG).show(); break;
                    }
                    return true;
                }
            });
            menu.inflate (R.menu.popup_menu_post);
            menu.show();
    }
    public void visitWall(Post post){
        Intent intent;
        if(post.getAccount().getUserName().equals(currentUser)){
            // if avatar clicked isn current user, start privacy wall activity ...
            intent = new Intent(context, PrivacyWallActivity.class);
        }
        else{
            // if avatar clicked isn't current user, start friend wall activity
            intent = new Intent(context, FriendWallActivity.class);
            int indexActivity = -1;
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
        }
        context.startActivity(intent);
    }

    public void playMusic(Post post){
        Intent intent;
        intent = new Intent(context, MusicPlayer.class);
        context.startActivity(intent);
    }


}
