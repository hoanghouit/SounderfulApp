package com.example.htk.designtemplate.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.htk.designtemplate.Model.LikeModel;
import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Service.ApiUtils;
import com.example.htk.designtemplate.Service.PostService;
import com.example.htk.designtemplate.Utils.MultipleToast;

import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicPlayer extends AppCompatActivity {
    private final static String tag = "MusicPlayer";
    private ImageView imageTrack;
    private TextView titleTextView;
    private ImageView imgPlay;
    private ImageView imgReplay;
    private ImageView imgLike;
    private MediaPlayer mediaPlayer;
    private TextView txtDuration;
    private SeekBar skSong;
    private TextView txtCurrentTime;
    private ImageView backImage;
    private String title;
    private String urlImage;
    private String urlTrack;
    private ProgressDialog progressDialog;
    private Activity context=this;
    private PostService postService = ApiUtils.getPostService();
    private String currentUser = MainActivity.userName;
    private boolean liked;
    private int postId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        // set context for toasts
        MultipleToast.context = this;

        // inital UI
        imageTrack = (ImageView) findViewById(R.id.trackImageView);
        imgPlay = (ImageView) findViewById(R.id.imgPlay);
        imgReplay = (ImageView) findViewById(R.id.imgReplay);
        txtDuration = (TextView) findViewById(R.id.txtDuration);
        txtCurrentTime = (TextView) findViewById(R.id.txtCurrentTime);
        skSong = (SeekBar) findViewById(R.id.skSong);
        backImage = (ImageView) findViewById(R.id.backImage_playMusicActivity) ;
        titleTextView = (TextView) findViewById(R.id.titleSong) ;
        imgLike = (ImageView) findViewById(R.id.imgLike);

        // get extra
        title = getIntent().getExtras().getString("title");
        urlImage = getIntent().getExtras().getString("urlImage");
        urlTrack = getIntent().getExtras().getString("urlTrack");
        liked = getIntent().getExtras().getBoolean("liked");
        postId = getIntent().getExtras().getInt("postId");

        // set image track
        Glide.with(this).load(ApiUtils.getImageUrl(urlImage)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).override(600,600).circleCrop().error(R.drawable.circle_gray_background)).into(imageTrack);

        // set title song
        titleTextView.setText(title);

        // set animation
        imageTrack.setAnimation(initRotateAnimation());

        // set like icon
        if(liked){
            imgLike.setImageResource(R.drawable.ic_liked);
        }
        else{
            imgLike.setImageResource(R.drawable.ic_like);
        }
        createMediaPlayer();

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(liked){
                    liked = false;
                    imgLike.setImageResource(R.drawable.ic_like_gray);
                    deleteLike(postId);
                }
                else{
                    liked = true;
                    imgLike.setImageResource(R.drawable.ic_liked);
                    createLike(postId);
                }
            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    //imageTrack.clearAnimation();
                    mediaPlayer.pause();
                    imgPlay.setImageResource(R.drawable.ic_play_song);
                }
                else{
                    //imageTrack.setAnimation(initRotateAnimation());
                    mediaPlayer.start();
                    imgPlay.setImageResource(R.drawable.ic_pause_song);
                }
            }
        });

        imgReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isLooping()){
                    mediaPlayer.setLooping(false);
                    imgReplay.setImageResource(R.drawable.ic_replay);
                }
                else{
                    mediaPlayer.setLooping(true);
                    imgReplay.setImageResource(R.drawable.ic_replay_song);
                }


            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }
    @Override
    protected void onStop(){
        super.onStop();
        mediaPlayer.stop();
    }


    protected void createMediaPlayer(){
        String url = ApiUtils.getTrackUrl(urlTrack);
        //String url = "https://drive.google.com/uc?export=download&id=12WSdJFxUZrV-rX6vMQz47OTKeOpd1IC3";

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    setDurartion();
                    setSeekBar();
                    updateTime();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected  void setDurartion() {
        int duration = mediaPlayer.getDuration();
        SimpleDateFormat formatDuration = new SimpleDateFormat("mm:ss");
        txtDuration.setText(formatDuration.format(duration));
    }

    protected void updateTime(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int duration = mediaPlayer.getCurrentPosition();
                SimpleDateFormat formatDuration = new SimpleDateFormat("mm:ss");
                txtCurrentTime.setText(formatDuration.format(duration));
                skSong.setProgress(duration);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        imgPlay.setImageResource(R.drawable.ic_play_song);
                    }
                });
                handler.postDelayed(this,500);
            }
        },100);
    }

    protected void setSeekBar(){
        skSong.setMax(mediaPlayer.getDuration());
        skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(skSong.getProgress());
            }
        });
    }
    public void createProgressDialog(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading ...");
        progressDialog.setProgress(0);
    }

    public void createLike(final int postId){
        postService.createLike(currentUser,Integer.toString(postId)).enqueue(new Callback<LikeModel>() {
            @Override
            public void onResponse(Call<LikeModel> call, Response<LikeModel> response) {
                if(response.isSuccessful()) {
                    Log.d(tag, "create like from API");
                }else {
                    MultipleToast.showToast("Yêu thích không thành công");
                    int statusCode  = response.code();
                    Log.d(tag, "fail create like from API");
                    Log.d(tag, ((Integer)statusCode).toString());
                    // handle request errors depending on status code
                    MultipleToast.showToast(MainActivity.fail_request);
                }
            }
            @Override
            public void onFailure(Call<LikeModel> call, Throwable t) {
                MultipleToast.showToast(MainActivity.fail_request);
                Log.d(tag, "fail");
            }
        });
    }

    public void deleteLike(final int postId) {
        postService.deleteLike(currentUser, Integer.toString(postId)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(tag, "delete like from API");
                } else {
                    MultipleToast.showToast("Hủy yêu thích không thành công");
                    int statusCode = response.code();
                    Log.d(tag, "delete create like from API");
                    Log.d(tag, ((Integer) statusCode).toString());
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
    private Animation initRotateAnimation(){
        RotateAnimation rotateAnimation = new RotateAnimation(0,360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(13000);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        return rotateAnimation;
    }


}
