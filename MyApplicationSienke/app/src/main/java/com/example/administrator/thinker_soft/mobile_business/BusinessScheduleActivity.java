package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/12.
 */
public class BusinessScheduleActivity extends Activity {
    private ImageView back,tj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_data);

        bindView();
        setViewClickListener();
    }

    public void bindView(){
        back = (ImageView) findViewById(R.id.back);
        tj = (ImageView) findViewById(R.id.tj);
    }

    public void setViewClickListener(){
        back.setOnClickListener(clickListener);
        tj.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
                case R.id.tj:
                    Intent intent = new Intent(BusinessScheduleActivity.this,BusinessDataInfo.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}
