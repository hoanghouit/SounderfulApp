package com.example.htk.designtemplate.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.htk.designtemplate.Activity.MainActivity;
import com.example.htk.designtemplate.Activity.MusicPlayer;
import com.example.htk.designtemplate.Model.Post;
import com.example.htk.designtemplate.Model.PostModel;
import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Service.ApiUtils;
import com.example.htk.designtemplate.Service.PostService;
import com.example.htk.designtemplate.Utils.MultipleToast;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HTK on 11/16/2017.
 */

public class TrackSearchAdapter extends ArrayAdapter<Post> {
    private static final String tag = "trackSearchAdapter";
    private Activity context;
    private PostService postService = ApiUtils.getPostService();
    private List<Integer> likedPostIds = null;
    public TrackSearchAdapter(Activity context, int layoutID, List<Post> objects) {
        super(context, layoutID, objects);
        this.context = context;
    }
    public void setLikedPostIds(List<Integer> likedPostIds){
        this.likedPostIds = likedPostIds;
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
        final boolean[] liked = new boolean[1];

        if(likedPostIds != null){
            if(likedPostIds.contains((Integer)post.getPostId())) {
                liked[0] = true;
            }
            else{
                liked[0] = false;
            }
        }

        // Set username
        userName.setText(post.getAccount().getUserName());
        // Set biography
        title.setText(post.getTitle());

        // Set avater image
        String url= ApiUtils.getImageUrl(post.getUrlImage());
        Glide.with(context).load(url).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).skipMemoryCache(true).override(200,200).circleCrop().error(R.drawable.circle_gray_background)).into(image);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playMusic(post,liked[0] );
            }
        });
        return convertView;
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
                    MultipleToast.showToast(MainActivity.fail_request);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MultipleToast.showToast(MainActivity.fail_request);
                Log.d(tag, "fail");
            }
        });
    }
}
