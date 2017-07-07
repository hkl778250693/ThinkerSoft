package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/7/7.
 */
public class BusinessFlowWorkerActivity extends Activity {
    private TextView name,section,data,save;
    private ImageView back;
    private SimpleDateFormat dateFormat;
    private SharedPreferences sharedPreferences_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_flow_worker);

        bindView();//绑定控件
        defaultSetting();
        setOnClickListener();//点击事件
    }

    private void bindView(){
        back = (ImageView) findViewById(R.id.back);
        section = (TextView) findViewById(R.id.section);
        data = (TextView) findViewById(R.id.data);
        section = (TextView) findViewById(R.id.section);
        save = (TextView) findViewById(R.id.save);
        name = (TextView) findViewById(R.id.name);
    }
    private void defaultSetting() {
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        name.setText(sharedPreferences_login.getString("user_name", ""));
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        data.setText(dateFormat.format(new Date()));
    }

    private void setOnClickListener(){
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
                case R.id.save:
                    finish();
                    break;
            }
        }
    };
}
