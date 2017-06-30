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
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.adapter.ReportInfoAdapter;
import com.example.administrator.thinker_soft.mobile_business.model.ReportInfoItem;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
public class WorkReportInfoActivity extends Activity {

    private ImageView back;
    private ListView listView;
    private ReportInfoItem item;
    private SharedPreferences sharedPreferences_login;
    private SQLiteDatabase db;  //数据库
    private List<ReportInfoItem> itemList = new ArrayList<>();
    private Cursor cursor;
    private ReportInfoAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.i("WorkReportInfo", "设置适配器成功");
                    adapter = new ReportInfoAdapter(WorkReportInfoActivity.this, itemList);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_info);

        bindView();//绑定控件
        defaultSetting();
        setOnClickListener();//点击事件

    }

    private void defaultSetting() {
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        final MySqliteHelper helper = new MySqliteHelper(WorkReportInfoActivity.this, 1);
        db = helper.getWritableDatabase();
        new Thread() {
            @Override
            public void run() {
                super.run();
                queryoaReport();
                if (cursor.getCount() != 0) {
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();
    }

    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.listview);
    }

    private void setOnClickListener() {
        back.setOnClickListener(clickListener);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = (ReportInfoItem) adapter.getItem(position);
                Intent intent = new Intent(WorkReportInfoActivity.this, ReportInfoInfoActivity.class);
                intent.putExtra("time", item.getTime());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                item = (ReportInfoItem) adapter.getItem(position);
                db.delete("oaReport", "time=?", new String[]{item.getTime()});  //删除OaUser表中所有数据（官方推荐方法）
                db.execSQL("update sqlite_sequence set seq=0 where name='oaReport'");
                itemList.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(WorkReportInfoActivity.this, "清除数据成功！", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    /**
     * 根据用户ID查询工作汇报并显示listview数据
     */
    private void queryoaReport() {
        itemList.clear();
        cursor = db.rawQuery("select * from oaReport where userId=?", new String[]{sharedPreferences_login.getString("userId", "")});
        Log.i("queryOaUserInfo", "集合长度为：" + cursor.getCount());
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            ReportInfoItem item = new ReportInfoItem();
            item.setName(cursor.getString(cursor.getColumnIndex("userName")));
            item.setTime(cursor.getString(cursor.getColumnIndex("time")));
            item.setContent(cursor.getString(cursor.getColumnIndex("createReport")));
            itemList.add(item);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                queryoaReport();
                adapter = new ReportInfoAdapter(WorkReportInfoActivity.this, itemList);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
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
