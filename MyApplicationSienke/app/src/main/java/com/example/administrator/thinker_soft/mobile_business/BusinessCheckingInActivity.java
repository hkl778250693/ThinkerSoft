package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.adapter.CheckingInAdapter;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/13.
 */
public class BusinessCheckingInActivity extends Activity {
    private ImageView back, outSignIn;
    private ListView listView;
    private CheckingInAdapter adapter;
    private List<String> stringList = new ArrayList<>();
    private SharedPreferences sharedPreferences_login;
    private SQLiteDatabase db;  //数据库
    private String outWorkCount;
    private Cursor cursorOutWork, cursorOutWorkCount;
    private TextView outWork;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    adapter = new CheckingInAdapter(BusinessCheckingInActivity.this, stringList);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    break;
                case 1:
                    outWork.setText(outWorkCount);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_checking_in);//考勤

        bindView();//绑定控件
        defaultSetting();//初始化设置
        setOnClickListener();//点击事件
    }

    private void defaultSetting() {
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        final MySqliteHelper helper = new MySqliteHelper(BusinessCheckingInActivity.this, 1);
        db = helper.getWritableDatabase();
        new Thread() {
            @Override
            public void run() {
                super.run();
                queryOaUserOutWorkInfo();
                if (cursorOutWork.getCount() != 0) {
                    handler.sendEmptyMessage(0);
                }
                queryOaUserOutWorkTime();
                if (cursorOutWorkCount.getCount() != 0) {
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();

    }

    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        outSignIn = (ImageView) findViewById(R.id.out_sign_in);
        listView = (ListView) findViewById(R.id.listview);
        outWork = (TextView) findViewById(R.id.outWork);
    }


    private void setOnClickListener() {
        adapter = new CheckingInAdapter(BusinessCheckingInActivity.this, stringList);
        listView.setAdapter(adapter);
        back.setOnClickListener(clickListener);
        outSignIn.setOnClickListener(clickListener);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    /**
     * 根据用户ID查询用户外勤信息并显示listview数据
     */
    private void queryOaUserOutWorkInfo() {
        stringList.clear();
        cursorOutWork = db.rawQuery("select * from OaUserOutWork where userId=?", new String[]{sharedPreferences_login.getString("userId", "")});
        Log.i("queryOaUserInfo", "集合长度为：" + cursorOutWork.getCount());
        if (cursorOutWork.getCount() == 0) {
            return;
        }
        while (cursorOutWork.moveToNext()) {
            stringList.add(cursorOutWork.getString(cursorOutWork.getColumnIndex("checkAddress")));
        }
    }

    /**
     * 根据用户ID查询用户外勤次数
     */
    private void queryOaUserOutWorkTime() {
        stringList.clear();
        cursorOutWorkCount = db.rawQuery("select * from OaUser where userId=?", new String[]{sharedPreferences_login.getString("userId", "")});
        Log.i("queryOaUserInfo", "集合长度为：" + cursorOutWorkCount.getCount());
        if (cursorOutWorkCount.getCount() == 0) {
            return;
        }
        while (cursorOutWorkCount.moveToNext()) {
            outWorkCount = cursorOutWorkCount.getString(cursorOutWorkCount.getColumnIndex("outWork"));
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.out_sign_in:
                    Intent intent = new Intent(BusinessCheckingInActivity.this, BusinessCheckingInInfoActivity.class);
                    startActivityForResult(intent, 100);
                    break;
                case R.id.sign_in:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                queryOaUserOutWorkTime();
                queryOaUserOutWorkInfo();
                Log.i("CheckingInActivity", "返回码为200 进来了 集合长度为：" + stringList.size());
                adapter = new CheckingInAdapter(BusinessCheckingInActivity.this, stringList);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursorOutWork != null) {
            cursorOutWork.close();
        }
        db.close();
    }
}
