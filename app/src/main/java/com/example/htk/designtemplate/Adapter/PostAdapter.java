package com.example.htk.designtemplate.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.htk.designtemplate.Activity.CommentActivity;
import com.example.htk.designtemplate.Activity.EditPostActivity;
import com.example.htk.designtemplate.Activity.FriendWallActivity;
import com.example.htk.designtemplate.Activity.MainActivity;
import com.example.htk.designtemplate.Activity.MusicPlayer;
import com.example.htk.designtemplate.Activity.NotificationActivity;
import com.example.htk.designtemplate.Activity.PrivacyWallActivity;
import com.example.htk.designtemplate.Activity.SearchActivity;
import com.example.htk.designtemplate.Model.LikeModel;
import com.example.htk.designtemplate.Model.Post;
import com.example.htk.designtemplate.Model.PostModel;
import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Service.ApiUtils;
import com.example.htk.designtemplate.Service.PostService;
import com.example.htk.designtemplate.Utils.MultipleToast;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HTK on 11/12/2017.
 */

public class PostAdapter extends ArrayAdapter<Post> {
    private final static String tag = "PostAdapter";
    private Activity context;
    private String currentUser;
    private List<Integer> likedPostIds = null;
    private PostService postService = ApiUtils.getPostService();

    public PostAdapter(Activity context, int layoutID, List<Post> objects) {
        super(context, layoutID, objects);
        this.context = context;
        // Get current user
        SharedPreferences sharedPreferences= context.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        currentUser = sharedPreferences.getString("userName","");

    }
    public void setLikedPostIds(List<Integer> likedPostIds){
        this.likedPostIds = likedPostIds;
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
        final TextView description = (TextView) convertView.findViewById(R.id.descriptionTextView);
        final TextView likeNumber = (TextView) convertView.findViewById(R.id.likeNumberTextView);
        TextView commentNumber = (TextView) convertView.findViewById(R.id.commentNumberTextView);
        final TextView listenNumber = (TextView) convertView.findViewById(R.id.listenNumberTextView);
        ImageView avatar = (ImageView) convertView.findViewById(R.id.avatarImageView);
        ImageView imageTrack = (ImageView) convertView.findViewById(R.id.imageView);
        ImageView playMusic = (ImageView) convertView.findViewById(R.id.playTrackIconImage);
        final ImageView menu = (ImageView) convertView.findViewById(R.id.menuImage);
        ImageView commentIcon = (ImageView) convertView.findViewById(R.id.commentIconImage);
        final ImageView likeIcon = (ImageView) convertView.findViewById(R.id.likeIconImage);
        SeekBar seekBar = (SeekBar) convertView.findViewById(R.id.trackTimeSeekBar);
        final TextView readMore = (TextView) convertView.findViewById(R.id.readMoreTextView);
        TextView txtDuration = (TextView) convertView.findViewById(R.id.totalTrackTimeTextView);
        TextView txtCurrentTime =(TextView) convertView.findViewById(R.id.currentTimeTextView);
        final boolean[] liked = new boolean[1];
        /*MyMediaPlayer myMediaPlayer = new MyMediaPlayer(playMusic, txtDuration, seekBar,txtCurrentTime, ApiUtils.getTrackUrl(post.getUrlTrack()));
        myMediaPlayer.createMediaPlayer();*/
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
                playMusic(post, liked[0]);
                listenNumber.setText(getNumber(post.getListenNumber())+" lượt nghe");
            }
        });

        // set action for clicking menu on each item list view
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if this is post of current user
                if(post.getAccount().getUserName().equals(currentUser)) {
                    showMenu(menu, post);
                }
                // if this is not post of current user, do nothing
            }
        });

        // Set title
        title.setText(post.getTitle());

        // Set username
        userName.setText(post.getAccount().getUserName());

        // Set avatar image
        String url= ApiUtils.getImageUrl(post.getAccount().getUrlAvatar());
        Glide.with(context).load(url).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).override(200,200).circleCrop().error(R.mipmap.ic_avatar_error)).into(avatar);

        //Set track image
        String url_image= ApiUtils.getImageUrl(post.getUrlImage());
        Glide.with(context).load(url_image).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).override(800,400).error(R.color.colorLittleGray)).into(imageTrack);

        // set like icon

        if(likedPostIds != null){
            if(likedPostIds.contains((Integer)post.getPostId())) {
                    likeIcon.setImageResource(R.drawable.ic_liked);
                    liked[0] = true;
            }
            else{
                liked[0] = false;
                likeIcon.setImageResource(R.drawable.ic_like);
            }
        }

        // Set description
        description.setText(post.getDescription());
        // set readmore text view
        if(description.getLineCount()>4){
            readMore.setVisibility(View.VISIBLE);
            description.setMaxLines(4);
        }
        else{
            readMore.setVisibility(View.GONE);
        }

        // Set listen number
       listenNumber.setText(getNumber(post.getListenNumber())+" lượt nghe");

        // Set like number
        likeNumber.setText(getNumber(post.getLikeNumber()));

        // Set comment number
        commentNumber.setText(getNumber(post.getCommentNumber()));

        // set time
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        dateTime.setText(simpleDateFormat.format(post.getDateTime()));


        // set action for clicking comment icon
        commentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId", post.getPostId());
                context.startActivity(intent);
            }
        });

        // set action for clicking readmore textview
        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readMore.setVisibility(View.GONE);
                description.setMaxLines(1000);
            }
        });

        // set action for clicking like icon
        likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(liked[0]){
                    liked[0] = false;
                    likeIcon.setImageResource(R.drawable.ic_like);
                    likeNumber.setText(Integer.toString(Integer.parseInt(likeNumber.getText().toString())-1));
                    deleteLike(post.getPostId());
                }
                else{
                    liked[0] = true;
                    likeIcon.setImageResource(R.drawable.ic_liked);
                    likeNumber.setText(Integer.toString(Integer.parseInt(likeNumber.getText().toString())+1));
                    createLike(post.getPostId());
                }
            }
        });

       /* playMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMediaPlayer.playMusic();
            }
        });
*/

        return convertView;
    }
    public void showMenu (View view, final Post post)
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
                        case R.id.item_edit: {
                            Intent intent = new Intent(context, EditPostActivity.class);
                            intent.putExtra("postId", post.getPostId());
                            context.startActivity(intent);
                            break;
                        }
                        case R.id.item_delete: {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                            dialogBuilder.setMessage("Bạn có chắc chắc muốn xóa?");
                            dialogBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deletePost(post);
                                }
                            });
                            dialogBuilder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();
                            break;
                        }
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
            intent.putExtra("userName", post.getAccount().getUserName());
        }
        context.startActivity(intent);
    }

    public void playMusic(Post post, boolean liked){
        Intent intent;
        intent = new Intent(context, MusicPlayer.class);
        Bundle bundle = new Bundle();
        bundle.putString("urlImage",post.getUrlImage());
        bundle.putString("urlTrack",post.getUrlTrack());
        bundle.putString("title",post.getTitle());
        bundle.putInt("postId",post.getPostId());
        bundle.putBoolean("liked",liked);
        intent.putExtras(bundle);
        // increase Listennumber
        PostModel postModel = new PostModel();
        getPostModel(postModel, post);
        post.setListenNumber(post.getListenNumber()+1);
        postModel.setListenNumber(postModel.getListenNumber()+1);
        increaseListenNumber(postModel);
        context.startActivity(intent);
    }

    public String getNumber(int number){
        int n;
        if(number<99999){
            return String.format(Locale.US,"%,d",number).replace(',','.');
        }
        else{
            if(number<999999){
                n = number/1000;
                return Integer.toString(n).concat("K");
            }
            else{
                if(number<999999999){
                    n = number/1000000;
                    return Integer.toString(n).concat("Tr");
                }
                else{
                    n = number/1000000000;
                    return Integer.toString(n).concat("T");
                }
            }
        }
    }
    public void createLike(final int postId){
        postService.createLike(currentUser,Integer.toString(postId)).enqueue(new Callback<LikeModel>() {
            @Override
            public void onResponse(Call<LikeModel> call, Response<LikeModel> response) {
                if(response.isSuccessful()) {
                    likedPostIds.add(postId);
                    Log.d(tag, "create like from API");
                }else {
                    MultipleToast.showToast("Yêu thích không thành công");
                    int statusCode  = response.code();
                    Log.d(tag, "fail create like from API");
                    Log.d(tag, ((Integer)statusCode).toString());
                    // handle request errors depending on status code
                }
            }
            @Override
            public void onFailure(Call<LikeModel> call, Throwable t) {
                MultipleToast.showToast("Yêu thích không thành công");
                Log.d(tag, "fail");
            }
        });
    }

    public void deleteLike(final int postId){
        postService.deleteLike(currentUser,Integer.toString(postId)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    likedPostIds.remove((Integer)postId);
                    Log.d(tag, "delete like from API");
                }else {
                    MultipleToast.showToast("Hủy yêu thích không thành công");
                    int statusCode  = response.code();
                    Log.d(tag, "delete create like from API");
                    Log.d(tag, ((Integer)statusCode).toString());
                    // handle request errors depending on status code
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MultipleToast.showToast("Hủy yêu thích không thành công");
                Log.d(tag, "fail");
            }
        });
    }
    public void increaseListenNumber(final PostModel postModel){
        postService.updatePost(postModel.getPostId(),postModel).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Log.d(tag, "increase listen number from API");
                }else {
                    int statusCode  = response.code();
                    Log.d(tag, "fail increase listen number from API");
                    Log.d(tag, ((Integer)statusCode).toString());
                    // handle request errors depending on status code
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //MultipleToast.showToast(failSignupToast);
                Log.d("PostAdapter", "fail");
            }
        });
    }
    public PostModel getPostModel(PostModel postModel, Post post){
        postModel.setPostId(post.getPostId());
        postModel.setUrlTrack(post.getUrlTrack());
        postModel.setUrlImage(post.getUrlImage());
        postModel.setDateTime(post.getDateTime());
        postModel.setDescription(post.getDescription());
        postModel.setTitle(post.getTitle());
        postModel.setUserName(post.getAccount().getUserName());
        postModel.setListenNumber(post.getListenNumber());
        return postModel;
    }

    public void deletePost(final Post post){
        postService.deletePost(post.getPostId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    MultipleToast.showToast("Xóa bài đăng thành công");
                    remove(post);
                    Log.d(tag, "delete post from API");
                }else {
                    MultipleToast.showToast("Xóa bài đăng không thành công");
                    int statusCode  = response.code();
                    Log.d(tag, "fail delete post from API");
                    Log.d(tag, ((Integer)statusCode).toString());
                    // handle request errors depending on status code
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MultipleToast.showToast("Xóa bài đăng không thành công");
                Log.d(tag, "fail");
            }
        });
    }

}
