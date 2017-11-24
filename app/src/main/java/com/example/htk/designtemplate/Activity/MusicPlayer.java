package com.example.htk.designtemplate.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.htk.designtemplate.R;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class MusicPlayer extends AppCompatActivity {
    private ImageView imageTrack;
    private TextView titleSong;
    private TextView titleTextView;
    private ImageView imgPlay;
    private ImageView imgReplay;
    private MediaPlayer mediaPlayer;
    private TextView txtDuration;
    private SeekBar skSong;
    private TextView txtCurrentTime;
    private ImageView backImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        imageTrack = (ImageView) findViewById(R.id.trackImageView);
        String url_image="https://images.vexels.com/media/users/6821/74972/raw/1054e351afe112bca797a70d67d93f9e-purple-daisies-blue-background.jpg";
        Glide.with(this).load(url_image).apply(RequestOptions.circleCropTransform().placeholder(R.mipmap.ic_launcher)).into(imageTrack);



        imgPlay = (ImageView) findViewById(R.id.imgPlay);
        imgReplay = (ImageView) findViewById(R.id.imgReplay);
        txtDuration = (TextView) findViewById(R.id.txtDuration);
        txtCurrentTime = (TextView) findViewById(R.id.txtCurrentTime);
        skSong = (SeekBar) findViewById(R.id.skSong);
        backImage = (ImageView) findViewById(R.id.backImage_playMusicActivity) ;
        titleSong = (TextView) findViewById(R.id.titleSong) ;



        createMediaPlayer();

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    imgPlay.setImageResource(R.drawable.ic_play_song);
                }
                else{
                    mediaPlayer.start();
                    imgPlay.setImageResource(R.drawable.ic_pause_song);

                }
            }
        });

        imgReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                imgPlay.setImageResource(R.drawable.ic_pause_song);
                createMediaPlayer();
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                mediaPlayer.stop();
            }
        });
    }

    protected void createMediaPlayer(){

        String url = "https://drive.google.com/uc?export=download&id=1KtxkUdHorVT5OkjlclrrKgrcLijfJfBW";
        //String url = "https://drive.google.com/uc?export=download&id=1jPBGvhgR7L0Ja9RoFvvHNPMjNeGDCy2k";
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


}
