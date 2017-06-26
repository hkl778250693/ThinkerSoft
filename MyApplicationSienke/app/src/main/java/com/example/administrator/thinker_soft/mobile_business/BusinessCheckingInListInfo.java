package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/24.
 */
public class BusinessCheckingInListInfo extends Activity {

    private ImageView back;
    private TextView time, dizhi, leixin, kehu, lxr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_info);

        bindView();
        setOnClickListener();
    }

    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        time = (TextView) findViewById(R.id.time);
        dizhi = (TextView) findViewById(R.id.dizhi);
        leixin = (TextView) findViewById(R.id.leixin);
        kehu = (TextView) findViewById(R.id.kehu);
        lxr = (TextView) findViewById(R.id.lxr);
    }

    private void setOnClickListener() {
        back.setOnClickListener(clickListener);
        time.setOnClickListener(clickListener);
        dizhi.setOnClickListener(clickListener);
        leixin.setOnClickListener(clickListener);
        kehu.setOnClickListener(clickListener);
        lxr.setOnClickListener(clickListener);
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
