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
public class BusinessWorkReportActivity extends Activity {
    private ImageView back;
    private RelativeLayout save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report);

        bindView();
        setViewClickListener();
    }

    public void bindView(){
        back = (ImageView) findViewById(R.id.back);
        save = (RelativeLayout) findViewById(R.id.save);
    }

    public void setViewClickListener(){
        back.setOnClickListener(clickListener);
        save.setOnClickListener(clickListener);
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
