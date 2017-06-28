package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/6/13.
 */
public class BusinessDataInfoActivity extends Activity {
    private ImageView back;
    private CheckBox slip;
    private TextView startDate, endDate;
    private Calendar c;//日历
    private Button saveBtn;
    private SimpleDateFormat dateFormat, dateFormat1;
    private int res, current_res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_data_info);//日程详细

        bindView();//绑定控件
        defaultSetting();
        setOnClickListener();//点击事件
    }

    public void bindView() {
        back = (ImageView) findViewById(R.id.back);
        slip = (CheckBox) findViewById(R.id.slip);
        startDate = (TextView) findViewById(R.id.start_date);
        endDate = (TextView) findViewById(R.id.end_date);
        saveBtn = (Button) findViewById(R.id.save_btn);
    }

    //初始化设置
    private void defaultSetting() {
        c = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        startDate.setText(dateFormat1.format(new Date()));
        endDate.setText(dateFormat1.format(new Date()));
    }

    public void setOnClickListener() {
        back.setOnClickListener(clickListener);
        slip.setOnClickListener(clickListener);
        startDate.setOnClickListener(clickListener);
        endDate.setOnClickListener(clickListener);
        saveBtn.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.slip:
                    if (slip.isChecked()) {
                        startDate.setText(dateFormat.format(new Date()));
                        endDate.setText(dateFormat.format(new Date()));
                    } else {
                        startDate.setText(dateFormat1.format(new Date()));
                        endDate.setText(dateFormat1.format(new Date()));
                    }
                    break;
                case R.id.start_date:
                    Intent intent = new Intent(BusinessDataInfoActivity.this, DataTimePickerActivity.class);
                    startActivityForResult(intent, 100);
                    break;
                case R.id.end_date:
                    Intent intent1 = new Intent(BusinessDataInfoActivity.this, DataTimePickerActivity.class);
                    startActivityForResult(intent1, 200);
                    break;
                case R.id.save_btn:
                    saveBtn.setClickable(false);
                    String str1 = startDate.getText().toString();
                    String str2 = endDate.getText().toString();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String currentTime = formatter.format(new Date());
                    current_res = str2.compareTo(currentTime);
                    if (current_res >= 0){
                        res = str1.compareTo(str2);
                        saveBtn.setClickable(true);
                        Toast.makeText(BusinessDataInfoActivity.this, "结束时间不能小于当天时间哦！", Toast.LENGTH_SHORT).show();
                    }else {
                        saveBtn.setClickable(true);
                        Toast.makeText(BusinessDataInfoActivity.this, "开始时间不能大于结束时间哦！", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    try {
                        startDate.setText(dateFormat1.format(dateFormat1.parse(data.getStringExtra("date") + "  " + data.getStringExtra("time"))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 200:
                    try {
                        endDate.setText(dateFormat1.format(dateFormat1.parse(data.getStringExtra("date") + "  " + data.getStringExtra("time"))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}

