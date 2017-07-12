package com.example.administrator.thinker_soft.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.thinker_soft.R;

public class MoveGuideActivity extends Activity {
    private ImageView imageViewGif;
    private SharedPreferences sharedPreferences_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_guide);

        imageViewGif = (ImageView) findViewById(R.id.image_gif);
        Glide.with(MoveGuideActivity.this).load(R.mipmap.welcome1).asGif().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViewGif);
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (sharedPreferences_login.getBoolean("have_logined", false)) {
                    Intent intent = new Intent(MoveGuideActivity.this, MoveHomePageActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(MoveGuideActivity.this, MoveLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                super.handleMessage(msg);
            }
        }.sendEmptyMessageDelayed(0, 4000);
    }
}
