package com.soft.nice.myrtspdemo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.rtsp.RtspMediaSource;
import androidx.media3.ui.PlayerView;

public class FullScreenPlayerActivity extends Activity {

    private PlayerView playerView;
    private ExoPlayer player;
    private String rtspUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_player);

        playerView = findViewById(R.id.fullscreen_player_view);

        // 获取传递过来的RTSP URL
        if (getIntent().hasExtra("rtsp_url")) {
            rtspUrl = getIntent().getStringExtra("rtsp_url");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (rtspUrl != null && !rtspUrl.isEmpty()) {
            initializePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    @OptIn(markerClass = UnstableApi.class)
    private void initializePlayer() {
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        RtspMediaSource.Factory mediaSourceFactory = new RtspMediaSource.Factory()
                .setForceUseRtpTcp(true);

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(rtspUrl));
        RtspMediaSource mediaSource = mediaSourceFactory.createMediaSource(mediaItem);

        player.setMediaSource(mediaSource);
        player.prepare();
        player.play();
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
}