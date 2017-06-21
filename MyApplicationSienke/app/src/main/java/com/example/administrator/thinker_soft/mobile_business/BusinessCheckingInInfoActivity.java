package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/13.
 */
public class BusinessCheckingInInfoActivity extends Activity {
    private ImageView back;
    private ImageView adress;
    private RelativeLayout linkman;
    private TextView dizhi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_business_checking_in_info);//考勤详细

        bindView();//绑定控件
        setOnClickListener();//点击事件
    }


    public void bindView() {
        back = (ImageView) findViewById(R.id.back);
        adress = (ImageView) findViewById(R.id.adress);
        linkman = (RelativeLayout) findViewById(R.id.linkman);
        dizhi = (TextView) findViewById(R.id.dizhi);
    }

    public void setOnClickListener() {
        back.setOnClickListener(clickListener);
        adress.setOnClickListener(clickListener);
        linkman.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.adress:
                    Intent intent1 = new Intent(BusinessCheckingInInfoActivity.this, BusinessCheckingIninfoMapActivity.class);
                    startActivityForResult(intent1, 200);
                    break;
                case R.id.linkman:
                    Intent intent = new Intent(BusinessCheckingInInfoActivity.this, BusinessNetPhoneBookActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                if (data != null) {
                    dizhi.setText(data.getStringExtra("location"));
                }
            }
        }
    }
}
