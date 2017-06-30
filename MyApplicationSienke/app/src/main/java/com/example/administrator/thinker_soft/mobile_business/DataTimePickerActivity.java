package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TimePicker;

import com.example.administrator.thinker_soft.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/6/27.
 */
public class DataTimePickerActivity extends Activity {
    private DatePicker data;
    private TimePicker time;
    private int year, month, day, hour, minutes;
    private String dataString, timeString;
    private SimpleDateFormat dateFormat;
    private boolean dateChanged = false;
    private boolean timeChanged = false;
    private LinearLayout dateLayout,timeLayout;
    private RadioButton cancelRb,saveRb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow_business_datatimepicker);

        bindView();
        defaultSetting();
        setOnClickListener();
    }

    //绑定控件
    private void bindView() {
        cancelRb = (RadioButton) findViewById(R.id.cancel_rb);
        saveRb = (RadioButton) findViewById(R.id.save_rb);
        data = (DatePicker) findViewById(R.id.date);
        time = (TimePicker) findViewById(R.id.time);
        dateLayout = (LinearLayout) findViewById(R.id.date_layout);
        timeLayout = (LinearLayout) findViewById(R.id.time_layout);
    }

    private void defaultSetting() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        hour = calendar.get(Calendar.AM_PM);
        minutes = calendar.get(Calendar.MINUTE);
    }

    //点击事件
    private void setOnClickListener() {
        data.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateChanged = true;
                DataTimePickerActivity.this.year = year;
                DataTimePickerActivity.this.month = monthOfYear;
                DataTimePickerActivity.this.day = dayOfMonth;
                dataString = year + "-" + (month+1) + "-" + day;
            }
        });

        time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timeChanged = true;
                DataTimePickerActivity.this.hour = hourOfDay;
                DataTimePickerActivity.this.minutes = minute;
                timeString = hour + ":" + minutes;
            }
        });
        cancelRb.setOnClickListener(clickListener);
        saveRb.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.cancel_rb:
                    DataTimePickerActivity.this.finish();
                    break;
                case R.id.save_rb:
                    if(dateLayout.getVisibility() == View.VISIBLE){
                        dateLayout.setVisibility(View.GONE);
                        timeLayout.setVisibility(View.VISIBLE);
                    }else {
                        Intent intent = new Intent();
                        if(dateChanged){   //日期改变了
                            if(timeChanged){  //时间改变了
                                try {
                                    intent.putExtra("dateAndTime", dateFormat.format(dateFormat.parse(dataString+" "+timeString)));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                StringBuffer sb = new StringBuffer(dateFormat.format(new Date()));
                                sb.replace(0,10,dataString);
                                intent.putExtra("dateAndTime", sb.toString());
                            }
                        }else {    //日期不改变
                            if(timeChanged){  //时间改变了
                                StringBuffer sb = new StringBuffer(dateFormat.format(new Date()));
                                sb.replace(11,16,timeString);
                                intent.putExtra("dateAndTime", sb.toString());
                            }else {
                                intent.putExtra("dateAndTime", dateFormat.format(new Date()));
                            }
                        }
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    break;
            }
        }
    };

}
