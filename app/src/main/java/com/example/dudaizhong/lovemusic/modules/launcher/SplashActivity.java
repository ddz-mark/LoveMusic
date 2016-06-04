package com.example.dudaizhong.lovemusic.modules.launcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.dudaizhong.lovemusic.activity.MainActivity;
import com.example.dudaizhong.lovemusic.common.PLog;

import java.lang.ref.WeakReference;


/**
 * Created by Dudaizhong on 2016/6/2.
 */
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private SwitchHandler handler = new SwitchHandler(Looper.getMainLooper(),this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler.sendEmptyMessageDelayed(1,2000);
    }

    class SwitchHandler extends Handler{
        private WeakReference<SplashActivity> mWeakReference;

        public SwitchHandler(Looper mLooper, SplashActivity activity) {
            super(mLooper);
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(i);
            //activity切换的淡入淡出效果
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            SplashActivity.this.finish();
        }
    }
}
