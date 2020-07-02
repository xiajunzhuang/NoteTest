package com.xiajunzhuang.hmstest.notetest;


import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        (new Thread() {
            public void run() {
                try {
                    sleep(5000);
                    Intent intent = new Intent(SplashActivity.this.getApplicationContext(), MainActivity.class);
//                    this(SplashActivity.this.getApplicationContext(), MainActivity.class);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }).start();
    }
}