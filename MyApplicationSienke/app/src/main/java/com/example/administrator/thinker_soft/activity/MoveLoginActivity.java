package com.example.administrator.thinker_soft.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/5/25.
 */
public class MoveLoginActivity extends Activity{
    private Button OkByt;
    private long exitTime = 0;//退出程序

    //强制竖屏
    @Override
    protected void onResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_login);

        bindView();//绑定控件
        setViewClickListener();//点击事件
    }

    //绑定控件
    public void bindView(){
        OkByt = (Button) findViewById(R.id.Ok_byt);
    }

    //点击事件
    public void setViewClickListener(){
        OkByt.setOnClickListener(onclickListener);
    }
    View.OnClickListener onclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.Ok_byt:
                    Intent intent = new Intent(MoveLoginActivity.this,MoveHomePageActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
