package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/13.
 */
public class BusinessDataInfo extends Activity {
    private ImageView back;
    private CheckBox slip,slip2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_data_info);

        bindView();//绑定控件
        setOnClickListener();//点击事件
    }

    public void bindView() {
        back = (ImageView) findViewById(R.id.back);
        slip = (CheckBox) findViewById(R.id.slip);
        slip2 = (CheckBox) findViewById(R.id.slip2);
    }

    public void setOnClickListener() {
        back.setOnClickListener(clickListener);
        slip.setOnClickListener(clickListener);
        slip2.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
            }
        }
    };
}
