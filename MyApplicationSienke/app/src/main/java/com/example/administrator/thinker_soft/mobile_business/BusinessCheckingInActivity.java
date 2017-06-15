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
public class BusinessCheckingInActivity extends Activity {
    private ImageView back,outSignIn;
    private RelativeLayout signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_checking_in);

        bindView();//绑定控件
        setOnClickListener();//点击事件
    }

    public void bindView(){
        back = (ImageView) findViewById(R.id.back);
        outSignIn = (ImageView) findViewById(R.id.out_sign_in);
        signIn = (RelativeLayout) findViewById(R.id.sign_in);
    }

    public void setOnClickListener(){
        back.setOnClickListener(clickListener);
        outSignIn.setOnClickListener(clickListener);
        signIn.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
                case R.id.out_sign_in:
                    Intent intent = new Intent(BusinessCheckingInActivity.this,BusinessCheckingInInfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.sign_in:
                    break;
            }
        }
    };
}
