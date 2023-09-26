package com.java.fangzheng.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;

import com.bumptech.glide.Glide;
import com.java.fangzheng.R;
import com.java.fangzheng.utils.StatusBarUtils;

public class MainActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGHT = 2000;
    private Handler handler;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarUtils.setWindowStatusBarColor(this, R.color.transparent);
        imageView = findViewById(R.id.splash);
        Glide.with(this).load(R.drawable.splash).into(imageView);

        handler = new Handler();
        // 延迟SPLASH_DISPLAY_LENGHT时间然后跳转到MainActivity
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,
                        HomeActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}