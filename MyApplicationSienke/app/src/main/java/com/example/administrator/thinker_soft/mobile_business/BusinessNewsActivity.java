package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/9.
 */
public class BusinessNewsActivity extends Activity {

    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_news);

        bindView();
        setViewClickListener();
    }

    public void bindView(){
        back= (ImageView) findViewById(R.id.back);
    }

    public void setViewClickListener(){
        back.setOnClickListener(clickListener);
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
