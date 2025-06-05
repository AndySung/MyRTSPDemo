package com.soft.nice.myrtspdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.rtsp.RtspMediaSource;
import androidx.media3.ui.PlayerView;

public class MainActivity extends Activity {

    private PlayerView playerView1, playerView2, playerView3, playerView4;
    private ExoPlayer player1, player2, player3, player4;
    private Button settingsButton;

    private static final String PREFS_NAME = "RTSP_PREFS";
    private static final String KEY_RTSP_URL_1 = "rtsp_url_1";
    private static final String KEY_RTSP_URL_2 = "rtsp_url_2";
    private static final String KEY_RTSP_URL_3 = "rtsp_url_3";
    private static final String KEY_RTSP_URL_4 = "rtsp_url_4";
    private static final String DEFAULT_RTSP_URL = "rtsp://192.168.1.136/stream2"; // 默认地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView1 = findViewById(R.id.player_view_1);
        playerView2 = findViewById(R.id.player_view_2);
        playerView3 = findViewById(R.id.player_view_3);
        playerView4 = findViewById(R.id.player_view_4);
        settingsButton = findViewById(R.id.settings_button);

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // 设置点击监听器以进行全屏播放
        playerView1.setOnClickListener(v -> openFullScreenPlayer(getRTSPUrl(KEY_RTSP_URL_1)));
        playerView2.setOnClickListener(v -> openFullScreenPlayer(getRTSPUrl(KEY_RTSP_URL_2)));
        playerView3.setOnClickListener(v -> openFullScreenPlayer(getRTSPUrl(KEY_RTSP_URL_3)));
        playerView4.setOnClickListener(v -> openFullScreenPlayer(getRTSPUrl(KEY_RTSP_URL_4)));
    }

    private String getRTSPUrl(String key) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(key, DEFAULT_RTSP_URL);
    }

    @OptIn(markerClass = UnstableApi.class)
    private void initializePlayer(PlayerView playerView, String rtspUrl) {
        if (rtspUrl == null || rtspUrl.isEmpty()) {
            // 如果URL为空，则不初始化播放器
            return;
        }
        ExoPlayer player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        playerView.setUseController(false); // 在四分屏时不显示控制器

        RtspMediaSource.Factory mediaSourceFactory = new RtspMediaSource.Factory()
                .setForceUseRtpTcp(true); // 强制使用TCP，提高稳定性

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(rtspUrl));
        RtspMediaSource mediaSource = mediaSourceFactory.createMediaSource(mediaItem);

        player.setMediaSource(mediaSource);
        player.prepare();
        player.play();

        // 根据PlayerView的ID分配播放器实例
        if (playerView.getId() == R.id.player_view_1) {
            player1 = player;
        } else if (playerView.getId() == R.id.player_view_2) {
            player2 = player;
        } else if (playerView.getId() == R.id.player_view_3) {
            player3 = player;
        } else if (playerView.getId() == R.id.player_view_4) {
            player4 = player;
        }
    }

    private void openFullScreenPlayer(String rtspUrl) {
        Intent intent = new Intent(this, FullScreenPlayerActivity.class);
        intent.putExtra("rtsp_url", rtspUrl);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 从SharedPreferences加载URL并初始化播放器
        initializePlayer(playerView1, getRTSPUrl(KEY_RTSP_URL_1));
        initializePlayer(playerView2, getRTSPUrl(KEY_RTSP_URL_2));
        initializePlayer(playerView3, getRTSPUrl(KEY_RTSP_URL_3));
        initializePlayer(playerView4, getRTSPUrl(KEY_RTSP_URL_4));
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayers();
    }

    private void releasePlayers() {
        if (player1 != null) {
            player1.release();
            player1 = null;
        }
        if (player2 != null) {
            player2.release();
            player2 = null;
        }
        if (player3 != null) {
            player3.release();
            player3 = null;
        }
        if (player4 != null) {
            player4.release();
            player4 = null;
        }
    }
}