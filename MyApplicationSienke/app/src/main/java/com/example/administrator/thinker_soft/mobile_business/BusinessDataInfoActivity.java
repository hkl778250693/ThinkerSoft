package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;

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
    private EditText title, customerName, address, detail;
    private Calendar c;//日历
    private Cursor cursor;
    private Button saveBtn;
    private SimpleDateFormat dateFormat, dateFormat1;
    private int res, current_res;
    private SharedPreferences sharedPreferences_login;
    private SQLiteDatabase db;  //数据库

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
        title = (EditText) findViewById(R.id.title);
        customerName = (EditText) findViewById(R.id.customer_name);
        address = (EditText) findViewById(R.id.address);
        detail = (EditText) findViewById(R.id.detail);
        slip = (CheckBox) findViewById(R.id.slip);
        startDate = (TextView) findViewById(R.id.start_date);
        endDate = (TextView) findViewById(R.id.end_date);
        saveBtn = (Button) findViewById(R.id.save_btn);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(BusinessDataInfoActivity.this, 1);
        db = helper.getWritableDatabase();
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        c = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
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
                    insertOaCalendar();
                    saveBtn.setClickable(false);
                    String str = endDate.getText().toString();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm");
                    String currentTime = formatter.format(new Date());
                    current_res = str.compareTo(currentTime);
                    if (current_res < 0) {
                        saveBtn.setClickable(true);
                        Toast.makeText(BusinessDataInfoActivity.this, "开始时间不能大于结束时间哦！", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent2 = new Intent();
                        setResult(Activity.RESULT_OK, intent2);
                        finish();
                    }
                    break;
            }
        }
    };

    /**
     * 将信息保存到本地数据库OA日程表
     */
    private void insertOaCalendar() {
        ContentValues values = new ContentValues();
        values.put("userId", sharedPreferences_login.getString("userId", ""));
        values.put("title", title.getText().toString().trim());
        values.put("isAllDay", slip.isChecked() + "");
        values.put("beginTime", startDate.getText().toString().trim());
        values.put("endTime", endDate.getText().toString().trim());
        values.put("participant", customerName.getText().toString().trim());
        values.put("address", address.getText().toString().trim());
        values.put("details", detail.getText().toString().trim());
        db.insert("OaCalendar", null, values);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    try {
                        startDate.setText(dateFormat2.format(dateFormat2.parse(data.getStringExtra("date") + data.getStringExtra("time"))));
                        Log.i("onActivityResult","获取的日期为："+data.getStringExtra("date"));
                        Log.i("onActivityResult","获取的时间为："+data.getStringExtra("time"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 200:
                    try {
                        endDate.setText(dateFormat1.format(dateFormat1.parse(data.getStringExtra("date") + data.getStringExtra("time"))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }
}

