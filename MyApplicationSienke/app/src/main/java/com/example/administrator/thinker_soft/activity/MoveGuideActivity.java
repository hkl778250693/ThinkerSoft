package com.example.administrator.thinker_soft.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.thinker_soft.R;

public class MoveGuideActivity extends Activity {
    private ImageView imageViewGif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_guide);

        imageViewGif = (ImageView) findViewById(R.id.image_gif);

        Glide.with(MoveGuideActivity.this).load(R.mipmap.welcome1).asGif().diskCacheStrategy(DiskCacheStrategy.NONE).into(imageViewGif);

        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Intent intent = new Intent(MoveGuideActivity.this,MoveLoginActivity.class);
                startActivity(intent);
                finish();
            }
        }.sendEmptyMessageDelayed(0,4000);

    }
}
