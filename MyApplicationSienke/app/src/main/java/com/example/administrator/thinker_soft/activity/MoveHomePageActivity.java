package com.example.administrator.thinker_soft.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/5/31.
 */
public class MoveHomePageActivity extends Activity{
    private ImageView set;
    private LinearLayout zsbg,gzl,qwx,ydaj,ydcx;

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
}
