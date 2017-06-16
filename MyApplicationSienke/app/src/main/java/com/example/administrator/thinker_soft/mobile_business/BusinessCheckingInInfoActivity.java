package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/13.
 */
public class BusinessCheckingInInfoActivity extends Activity {
    private ImageView back;
    private LinearLayout adress;
    private RelativeLayout linkman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_business_checking_in_info);//考勤详细

        bindView();//绑定控件
        setOnClickListener();//点击事件
    }

    public void bindView(){
        back = (ImageView) findViewById(R.id.back);
        adress = (LinearLayout) findViewById(R.id.adress);
        linkman = (RelativeLayout) findViewById(R.id.linkman);
    }

    public void setOnClickListener(){
        back.setOnClickListener(clickListener);
        adress.setOnClickListener(clickListener);
        linkman.setOnClickListener(clickListener);
    }
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
                case R.id.adress:
                    break;
                case R.id.linkman:
                    Intent intent = new Intent(BusinessCheckingInInfoActivity.this,BusinessNetPhoneBookActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}
