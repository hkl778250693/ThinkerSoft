package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/14.
 */
public class BusinessSendEmailActivity extends Activity {
    private ImageView back;
    private RelativeLayout send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_send_email);

        bindView();
        setViewClickListener();
    }

    public void bindView(){
        back = (ImageView) findViewById(R.id.back);
        send = (RelativeLayout) findViewById(R.id.send);
    }

    public void setViewClickListener(){
        back.setOnClickListener(clickListener);
        send.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
                case R.id.send:
                    break;
            }
        }
    };
}
