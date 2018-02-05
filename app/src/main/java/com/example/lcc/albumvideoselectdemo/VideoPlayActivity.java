package com.example.lcc.albumvideoselectdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class VideoPlayActivity extends AppCompatActivity {
    private JZVideoPlayerStandard jzVideoPlayerStandard;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        path=getIntent().getStringExtra("path");
        jzVideoPlayerStandard = (JZVideoPlayerStandard)findViewById(R.id.videoplayer);
        jzVideoPlayerStandard.setUp(path
                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();

    }
}
