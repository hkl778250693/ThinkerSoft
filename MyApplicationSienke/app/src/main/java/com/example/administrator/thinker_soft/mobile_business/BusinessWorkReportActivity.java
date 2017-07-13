package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/6/12.
 */
public class BusinessWorkReportActivity extends Activity {
    private ImageView back, userPhoto;
    private RelativeLayout save;
    private TextView userName, weekReport, time;
    private SharedPreferences sharedPreferences_login;
    private SQLiteDatabase db;  //数据库
    private EditText createReport, summarize, nextPlant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report);//工作汇报

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        userPhoto = (ImageView) findViewById(R.id.user_photo);
        save = (RelativeLayout) findViewById(R.id.save);
        userName = (TextView) findViewById(R.id.user_name);
        weekReport = (TextView) findViewById(R.id.week_report);
        time = (TextView) findViewById(R.id.time);
        createReport = (EditText) findViewById(R.id.create_report);
        summarize = (EditText) findViewById(R.id.summarize);
        nextPlant = (EditText) findViewById(R.id.next_plant);
    }

    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(BusinessWorkReportActivity.this, 1);
        db = helper.getWritableDatabase();
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        userName.setText(sharedPreferences_login.getString("user_name", ""));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        time.setText(dateFormat.format(new Date()));
    }

    private void setViewClickListener() {
        back.setOnClickListener(clickListener);
        save.setOnClickListener(clickListener);
        weekReport.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.save:
                    if (!"".equals(createReport.getText().toString()) && !"".equals(summarize.getText().toString()) && !"".equals(nextPlant.getText().toString())) {
                        insertoaReport();
                        Toast.makeText(BusinessWorkReportActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        createReport.setText("");
                        summarize.setText("");
                        nextPlant.setText("");
                    }else {
                        Toast.makeText(BusinessWorkReportActivity.this,"请完成本页内容",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.week_report:
                    Intent intent = new Intent(BusinessWorkReportActivity.this, WorkReportInfoActivity.class);
                    startActivity(intent);
                    break;

            }
        }
    };

    /**
     * 将信息保存到本地数据库OA工作汇报表
     */
    private void insertoaReport() {
        ContentValues values = new ContentValues();
        values.put("userId", sharedPreferences_login.getString("userId", ""));
        values.put("userName", sharedPreferences_login.getString("user_name", ""));
        values.put("time", time.getText().toString().trim());
        values.put("createReport", createReport.getText().toString().trim());
        values.put("summarizeReport", summarize.getText().toString().trim());
        values.put("nextReport", nextPlant.getText().toString().trim());
        db.insert("oaReport", null, values);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
