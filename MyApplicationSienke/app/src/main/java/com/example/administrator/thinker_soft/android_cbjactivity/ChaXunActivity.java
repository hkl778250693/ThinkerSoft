package com.example.administrator.thinker_soft.android_cbjactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;

public class ChaXunActivity extends Activity {
    private final String filepath = Environment.getDataDirectory().getPath() + "/data/" + "com.example.android_cbjactivity" + "/databases/";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String DBName;
    private LinearLayout by_met_book, by_user_name, by_user_num, by_user_met, by_user_minus;
    private MyActivityManager mam;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    by_user_minus.setEnabled(true);
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), "��ѡ�񳭱�", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cx);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        by_met_book = (LinearLayout) findViewById(R.id.by_met_book);
        by_user_name = (LinearLayout) findViewById(R.id.by_user_name);
        by_user_num = (LinearLayout) findViewById(R.id.by_user_num);
        by_user_met = (LinearLayout) findViewById(R.id.by_user_met);
        by_user_minus = (LinearLayout) findViewById(R.id.by_user_minus);
    }

    //初始化设置
    private void defaultSetting() {
        mam = MyActivityManager.getInstance();
        mam.pushOneActivity(this);
        sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
        editor = sharedPreferences.edit();
        DBName = sharedPreferences.getString("dbName", "");
    }

    //点击事件
    public void setViewClickListener() {
        by_met_book.setOnClickListener(onClickListener);
        by_user_name.setOnClickListener(onClickListener);
        by_user_num.setOnClickListener(onClickListener);
        by_user_met.setOnClickListener(onClickListener);
        by_user_minus.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.by_met_book:
                    Intent intent1 = new Intent(ChaXunActivity.this, CbbCXAction.class);
                    startActivity(intent1);
                    break;
                case R.id.by_user_name:
                    Intent intent2 = new Intent(ChaXunActivity.this, YongHuMingCX.class);
                    startActivity(intent2);
                    break;
                case R.id.by_user_num:
                    Intent intent3 = new Intent(ChaXunActivity.this, BianHaoCXActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.by_user_met:
                    Intent intent4 = new Intent(ChaXunActivity.this, BiaoHaoCXActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.by_user_minus:
                    by_user_minus.setEnabled(false);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            int signal = 10;
                            int con_signal = 10;
                            editor.putInt("signal", signal);
                            editor.putInt("con_signal", con_signal);
                            editor.commit();
                            DBName = sharedPreferences.getString("dbName", "");
                            if (DBName != null && !"".equals(DBName)) {
                                Intent intent = new Intent();
                                intent.setClass(ChaXunActivity.this, ShowListviewActivity.class);
                                startActivity(intent);
                            } else {
                                handler.sendEmptyMessage(1);
                                return;
                            }
                            handler.sendEmptyMessage(0);
                        }
                    }.start();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v("onSaveInstanceState:", "onSaveInstanceState()");
    }
}
