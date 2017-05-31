package com.example.administrator.thinker_soft.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Administrator on 2017/3/16.
 */
public class SecurityStatisticsActivity extends Activity {
    private ImageView securityStatisticsBack;
    private TextView checkedNumber, totalNumber, notCheckedNumber, finishRate, problemCheckedNumber;
    private SharedPreferences sharedPreferences,sharedPreferences_login;
    private SharedPreferences.Editor editor;
    private RadioButton userStatisticsBtn,taskStatisticsBtn;
    private int notChecked = 0;  //未安检的户数
    private SQLiteDatabase db;  //数据库
    private MySqliteHelper helper; //数据库帮助类
    private int task_total_numb = 0;
    private int totalUserNumber = 0;//所有任务总户数
    private int taskTotalUserNumber = 0;//任务分区总户数
    private ArrayList<String> stringList = new ArrayList<>();//保存字符串参数
    private int checkedUserNumber = 0;  //已检户数
    private int partProblemNumber = 0;  //按任务分区存在问题户数
    private int totalProblemNumber = 0;  // 存在问题总户数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_statistics);

        bindView();//绑定控件
        defaultSetting();//初始化设置
        setViewClickListener();//点击事件
    }

    //绑定控件
    private void bindView() {
        securityStatisticsBack = (ImageView) findViewById(R.id.security_statistics_back);
        checkedNumber = (TextView) findViewById(R.id.checked_number);
        totalNumber = (TextView) findViewById(R.id.total_number);
        notCheckedNumber = (TextView) findViewById(R.id.not_checked_number);
        finishRate = (TextView) findViewById(R.id.finish_rate);
        problemCheckedNumber = (TextView) findViewById(R.id.problem_checked_number);
        userStatisticsBtn= (RadioButton) findViewById(R.id.user_statistics_btn);
        taskStatisticsBtn= (RadioButton) findViewById(R.id.task_statistics_btn);
    }

    //初始化设置
    private void defaultSetting() {
        helper = new MySqliteHelper(SecurityStatisticsActivity.this, 1);
        db = helper.getWritableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = SecurityStatisticsActivity.this.getSharedPreferences(sharedPreferences_login.getString("login_name","")+"data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userStatisticsBtn.setChecked(true);
        getTaskParams();
        new Thread(){
            @Override
            public void run() {
                for (int i = 0; i < task_total_numb; i++) {
                    getCheckedNumber(stringList.get(i),sharedPreferences_login.getString("login_name","")); //根据当前登录的用户去获取已检用户户数
                }
                for (int i = 0; i < task_total_numb; i++) {
                    getPartProblemNumber(stringList.get(i),sharedPreferences_login.getString("login_name","")); //获取存在问题户数
                }
                getTotalProblemNumber(sharedPreferences_login.getString("login_name",""));   //获取所有存在问题户数
                getTotalUserNumber();
            }
        }.start();
    }

    //点击事件
    private void setViewClickListener() {
        securityStatisticsBack.setOnClickListener(onClickListener);
        userStatisticsBtn.setOnClickListener(onClickListener);
        taskStatisticsBtn.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.security_statistics_back:
                    SecurityStatisticsActivity.this.finish();
                    break;
                case R.id.user_statistics_btn:
                    getTotalUserNumber();
                    break;
                case R.id.task_statistics_btn:
                    getTaskUserNumber();
                    break;
            }
        }
    };

    //获取任务编号参数
    public void getTaskParams() {
        if (sharedPreferences.getStringSet("stringSet", null) != null && sharedPreferences.getInt("task_total_numb", 0) != 0) {
            Iterator iterator = sharedPreferences.getStringSet("stringSet", null).iterator();
            while (iterator.hasNext()) {
                stringList.add(iterator.next().toString());
            }
            task_total_numb = sharedPreferences.getInt("task_total_numb", 0);
        }
    }

    //获取已安检户数
    public void getCheckedNumber(String taskId,String loginName){
        Cursor cursor = db.rawQuery("select * from User where taskId=? and loginName=?", new String[]{taskId,loginName});//查询并获得游标
        //在页面finish之前，从上到下查询本地数据库没有安检的用户，相对应的item位置，查询到一个就break
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            if (cursor.getString(10).equals("true")) {
                checkedUserNumber++;
                Log.i("getCheckedNumber===>", "已安检" + checkedUserNumber + "户");
            }
        }
        cursor.close(); //游标关闭
    }

    //按任务分区获取存在问题户数
    public void getPartProblemNumber(String taskId,String loginName){
        Cursor cursor = db.rawQuery("select * from User where taskId=? and loginName=?", new String[]{taskId,loginName});//查询并获得游标
        taskTotalUserNumber += cursor.getCount();//获取任务分区用户总数
        //在页面finish之前，从上到下查询本地数据库没有安检的用户，相对应的item位置，查询到一个就break
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            if (cursor.getString(19).equals("false")) {
                partProblemNumber++;
            }
        }
        Log.i("getCheckedNumber===>", "部分存在问题" + partProblemNumber + "户");
        cursor.close(); //游标关闭
    }

    //获取所有存在问题户数
    public void getTotalProblemNumber(String loginName){
        Cursor  cursor = db.rawQuery("select * from User where loginName=?", new String[]{loginName});//查询并获得游标
        totalUserNumber = cursor.getCount(); //获取所有任务总户数
        //在页面finish之前，从上到下查询本地数据库没有安检的用户，相对应的item位置，查询到一个就break
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            if (cursor.getString(19).equals("false")) {
                totalProblemNumber++;
            }
        }
        Log.i("getCheckedNumber===>", "总的存在问题" + totalProblemNumber + "户");
        cursor.close(); //游标关闭
    }

    //总用户统计
    public void getTotalUserNumber (){
        Log.i("getTaskUserNumber===>", "所有任务总户数=" + totalUserNumber + "户");
        totalNumber.setText(String.valueOf(totalUserNumber));
        if (checkedUserNumber != 0) {
            checkedNumber.setText(String.valueOf(checkedUserNumber));
        } else {
            checkedNumber.setText("0");
        }
        notChecked = totalUserNumber - checkedUserNumber;
        notCheckedNumber.setText(String.valueOf(notChecked));
        if (totalUserNumber != 0) {
            double checkedNumber = (double) checkedUserNumber* 100;
            double totalCount = (double) totalUserNumber;
            double finishingRate = checkedNumber/totalCount;  //完成率
            Log.i("getTotalUserNumber===>", "完成率=" + finishingRate + "%");
            DecimalFormat df = new DecimalFormat("0.0");
            String result = df.format(finishingRate);
            Log.i("getTotalUserNumber===>", "完成率=" + result + "%");
            finishRate.setText(result);
        } else {
            finishRate.setText("0.0");
        }
        if (totalProblemNumber != 0) {
            Log.i("getTotalUserNumber===>", "存在问题的户数=" + totalProblemNumber + "户");
            problemCheckedNumber.setText(String.valueOf(totalProblemNumber));
        } else {
            problemCheckedNumber.setText("0");
        }
    }

    //任务分区统计
    public void getTaskUserNumber (){
        Log.i("getTaskUserNumber===>", "任务分区总户数=" + taskTotalUserNumber + "户");
        totalNumber.setText(String.valueOf(taskTotalUserNumber));
        if (checkedUserNumber != 0) {
            checkedNumber.setText(String.valueOf(checkedUserNumber));
        } else {
            checkedNumber.setText("0");
        }
        notChecked = taskTotalUserNumber - checkedUserNumber;
        notCheckedNumber.setText(String.valueOf(notChecked));
        if (taskTotalUserNumber != 0) {
            double checkedNumber = (double) checkedUserNumber* 100;
            double totalCount = (double) taskTotalUserNumber;
            double finishingRate = checkedNumber/totalCount;  //完成率
            Log.i("getTaskUserNumber===>", "完成率=" + finishingRate + "%");
            DecimalFormat df = new DecimalFormat("0.0");
            String result = df.format(finishingRate);
            Log.i("getTaskUserNumber===>", "完成率=" + result + "%");
            finishRate.setText(result);
        } else {
            finishRate.setText("0.0");
        }
        if (partProblemNumber != 0) {
            Log.i("getTaskUserNumber===>", "存在问题的户数=" + partProblemNumber + "户");
            problemCheckedNumber.setText(String.valueOf(partProblemNumber));
        } else {
            problemCheckedNumber.setText("0");
        }
    }
}
