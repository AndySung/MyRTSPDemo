package com.soft.nice.myrtspdemo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {

    private EditText etRtspUrl1, etRtspUrl2, etRtspUrl3, etRtspUrl4;
    private Button btnSaveSettings;

    private static final String PREFS_NAME = "RTSP_PREFS";
    private static final String KEY_RTSP_URL_1 = "rtsp_url_1";
    private static final String KEY_RTSP_URL_2 = "rtsp_url_2";
    private static final String KEY_RTSP_URL_3 = "rtsp_url_3";
    private static final String KEY_RTSP_URL_4 = "rtsp_url_4";
    private static final String DEFAULT_RTSP_URL = "rtsp://192.168.1.136/stream2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etRtspUrl1 = findViewById(R.id.et_rtsp_url_1);
        etRtspUrl2 = findViewById(R.id.et_rtsp_url_2);
        etRtspUrl3 = findViewById(R.id.et_rtsp_url_3);
        etRtspUrl4 = findViewById(R.id.et_rtsp_url_4);
        btnSaveSettings = findViewById(R.id.btn_save_settings);

        loadSettings();

        btnSaveSettings.setOnClickListener(v -> saveSettings());
    }

    private void loadSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        etRtspUrl1.setText(prefs.getString(KEY_RTSP_URL_1, DEFAULT_RTSP_URL));
        etRtspUrl2.setText(prefs.getString(KEY_RTSP_URL_2, DEFAULT_RTSP_URL));
        etRtspUrl3.setText(prefs.getString(KEY_RTSP_URL_3, DEFAULT_RTSP_URL));
        etRtspUrl4.setText(prefs.getString(KEY_RTSP_URL_4, DEFAULT_RTSP_URL));
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY_RTSP_URL_1, etRtspUrl1.getText().toString());
        editor.putString(KEY_RTSP_URL_2, etRtspUrl2.getText().toString());
        editor.putString(KEY_RTSP_URL_3, etRtspUrl3.getText().toString());
        editor.putString(KEY_RTSP_URL_4, etRtspUrl4.getText().toString());
        editor.apply();

        Toast.makeText(this, "设置已保存", Toast.LENGTH_SHORT).show();
        finish(); // 保存后返回上一页
    }
}