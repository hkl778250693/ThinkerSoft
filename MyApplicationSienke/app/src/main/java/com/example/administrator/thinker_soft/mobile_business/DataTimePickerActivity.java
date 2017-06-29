package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.administrator.thinker_soft.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/6/27.
 */
public class DataTimePickerActivity extends Activity {

    private Button saveBtn;
    private DatePicker data;
    private TimePicker time;
    private int year, month, day, hour, minute;
    private String dataString, timeString, timeStringdefault;
    private SimpleDateFormat dateFormat;
    private boolean dateChanged = false;
    private boolean timeChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_datatimepicker);

        saveBtn = (Button) findViewById(R.id.save_btn);
        data = (DatePicker) findViewById(R.id.date);
        time = (TimePicker) findViewById(R.id.time);

        defaultSetting();


        data.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateChanged = true;
                DataTimePickerActivity.this.year = year;
                DataTimePickerActivity.this.month = monthOfYear;
                DataTimePickerActivity.this.day = dayOfMonth;
                dataString = year + "-" + month + "-" + day;
            }
        });

        time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timeChanged = true;
                DataTimePickerActivity.this.hour = hourOfDay;
                DataTimePickerActivity.this.minute = minute;
                timeString = hour + ":" + minute;
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (dateChanged) {
                    intent.putExtra("date", dataString);
                } else {
                    intent.putExtra("date", dateFormat.format(new Date()));
                }
                if (timeChanged) {
                    intent.putExtra("time", timeString);
                } else {
                    intent.putExtra("time", timeStringdefault);
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void defaultSetting() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
        timeStringdefault = hour + ":" + minute;
    }

}
