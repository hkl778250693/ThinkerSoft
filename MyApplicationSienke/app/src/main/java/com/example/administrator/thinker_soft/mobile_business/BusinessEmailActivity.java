package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/13.
 */
public class BusinessEmailActivity extends Activity {
    private ImageView back;
    private RelativeLayout inbox, send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_email);

        bindView();//绑定控件
        setOnClickListener();//点击事件
    }

    public void bindView() {
        back = (ImageView) findViewById(R.id.back);
        inbox = (RelativeLayout) findViewById(R.id.inbox);
        send = (RelativeLayout) findViewById(R.id.send);
    }

    public void setOnClickListener() {
        back.setOnClickListener(clickListener);
        inbox.setOnClickListener(clickListener);
        send.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.inbox:
                    Intent intent = new Intent(BusinessEmailActivity.this, BusinessEmailInfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.send:
                    Intent intent1 = new Intent(BusinessEmailActivity.this, BusinessSendEmailActivity.class);
                    startActivity(intent1);
                    break;
            }
        }
    };
}
