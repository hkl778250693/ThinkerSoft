package com.example.administrator.thinker_soft.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/5/31.
 */
public class MoveHomePageActivity extends Activity{
    private ImageView set;
    private LinearLayout zsbg,gzl,qwx,ydaj,ydcx;
    private long exitTime = 0;//退出程序

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_homepage);

        bindView();//绑定控件
        setViewClickListener();//设置点击事件

    }

    private void bindView(){
        set = (ImageView) findViewById(R.id.set);
        ydaj = (LinearLayout) findViewById(R.id.ydaj);
        ydcx = (LinearLayout) findViewById(R.id.ydcx);
    }

    private void setViewClickListener(){
        set.setOnClickListener(clickListener);
        ydaj.setOnClickListener(clickListener);
        ydcx.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.set:

                    break;
                case R.id.ydaj:
                    Intent intent = new Intent(MoveHomePageActivity.this,SecurityChooseActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ydcx:
                    Intent intent1 = new Intent(MoveHomePageActivity.this,QueryActivity.class);
                    startActivity(intent1);
                    break;
            }
        }
    };

    /**
     * 捕捉返回事件按钮
     * 因为此 Activity继承 TabActivity,用 onKeyDown无响应，
     * 所以改用 dispatchKeyEvent
     * <p/>
     * 一般的 Activity 用 onKeyDown就可以了
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    /**
     * 退出程序
     */
    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Log.i("exitTime==========>", System.currentTimeMillis() - exitTime + "");
            //-------------Activity.this的context 返回当前activity的上下文，属于activity，activity 摧毁他就摧毁
            //-------------getApplicationContext() 返回应用的上下文，生命周期是整个应用，应用摧毁它才摧毁
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
