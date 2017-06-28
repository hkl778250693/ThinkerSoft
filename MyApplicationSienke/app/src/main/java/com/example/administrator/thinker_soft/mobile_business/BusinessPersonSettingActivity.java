package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/12.
 */
public class BusinessPersonSettingActivity extends Activity {
    private ImageView back;
    private RelativeLayout personMessage,news,general;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_person_setting);//个人设置

        bindView();//绑定控件
        setOnClickListener();//点击事件
    }

    public void bindView(){
        back = (ImageView) findViewById(R.id.back);
        personMessage = (RelativeLayout) findViewById(R.id.person_message);
        news = (RelativeLayout) findViewById(R.id.news);
        general = (RelativeLayout) findViewById(R.id.general);
    }

    public void setOnClickListener(){
        back.setOnClickListener(clickListener);
        personMessage.setOnClickListener(clickListener);
        news.setOnClickListener(clickListener);
        general.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
            }
        }
    };
}
