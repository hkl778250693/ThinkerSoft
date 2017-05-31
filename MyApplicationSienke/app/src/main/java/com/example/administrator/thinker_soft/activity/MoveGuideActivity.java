package com.example.administrator.thinker_soft.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/5/27.
 */
public class MoveGuideActivity extends Activity {
    private ImageView wel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_guide);

        bindView();//绑定控件

        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Intent intent = new Intent(MoveGuideActivity.this,MoveLoginActivity.class);
                startActivity(intent);
                finish();
            }
        }.sendEmptyMessageDelayed(0,3500);

    }

    //绑定控件ID
    private void bindView(){
        wel = (ImageView) findViewById(R.id.wel);
    }
}
