package com.example.administrator.thinker_soft.meter_code.activity;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.adapter.MeterUserListviewAdapter;
import com.example.administrator.thinker_soft.meter_code.model.MeterUserListviewItem;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;

import java.util.ArrayList;

public class MeterUserListviewActivity extends Activity {
    private ImageView back;
    private LinearLayout selectPage;
    private ListView listview;
    private TextView noData, lastPage, nextPage, currentPageTv, totalPageTv;
    private ArrayList<MeterUserListviewItem> userLists = new ArrayList<>();
    private SQLiteDatabase db;  //数据库
    private Cursor totalCountCursor, areaLimitCursor, bookLimitCursor;
    private SharedPreferences sharedPreferences_login;
    private int dataStartCount = 0;   //用于分页查询，表示从第几行开始
    private int currentPage = 1;  //当前页数
    private int totalPage;    //总页数
    private MeterUserListviewAdapter adapter;
    private MeterUserListviewItem item;
    private int currentPosition;  //点击当前抄表用户的item位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_user_listview);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        noData = (TextView) findViewById(R.id.no_data);
        selectPage = (LinearLayout) findViewById(R.id.select_page);
        listview = (ListView) findViewById(R.id.listview);
        lastPage = (TextView) findViewById(R.id.last_page);
        nextPage = (TextView) findViewById(R.id.next_page);
        currentPageTv = (TextView) findViewById(R.id.current_page_tv);
        totalPageTv = (TextView) findViewById(R.id.total_page_tv);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(MeterUserListviewActivity.this, 1);
        db = helper.getWritableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        new Thread() {
            @Override
            public void run() {
                super.run();
                getAreaUserData(dataStartCount);//读取本地的抄表分区用户数据
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(clickListener);
        lastPage.setOnClickListener(clickListener);
        nextPage.setOnClickListener(clickListener);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                item = (MeterUserListviewItem) adapter.getItem(position);
                Intent intent = new Intent(MeterUserListviewActivity.this, MeterUserDetailActivity.class);
                intent.putExtra("user_id",item.getUserID());
                startActivityForResult(intent,currentPosition);
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    MeterUserListviewActivity.this.finish();
                    break;
                case R.id.last_page:
                    lastPage.setClickable(false);
                    if (currentPageTv.getText().equals("1")) {
                        Toast.makeText(MeterUserListviewActivity.this, "已经是第一页哦！", Toast.LENGTH_SHORT).show();
                    } else {
                        currentPageTv.setText(String.valueOf(Integer.parseInt(currentPageTv.getText().toString())-1));
                        dataStartCount -= 50;
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                lastPage.setClickable(false);
                                getAreaUserData(dataStartCount);//读取本地的抄表分区用户数据
                                handler.sendEmptyMessage(1);
                            }
                        }.start();
                    }
                    break;
                case R.id.next_page:
                    nextPage.setClickable(false);
                    Log.i("MeterUserLVActivity", "总页数为：" + totalCountCursor.getCount() / 50);
                    Log.i("MeterUserLVActivity", "开始行数是：" + dataStartCount);
                    if (currentPageTv.getText().equals(totalPageTv.getText())) {
                        Toast.makeText(MeterUserListviewActivity.this, "已经是最后一页哦！", Toast.LENGTH_SHORT).show();
                    } else {
                        currentPageTv.setText(String.valueOf(Integer.parseInt(currentPageTv.getText().toString())+1));
                        dataStartCount += 50;
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                nextPage.setClickable(false);
                                getAreaUserData(dataStartCount);//读取本地的抄表分区用户数据
                                handler.sendEmptyMessage(1);
                            }
                        }.start();
                    }
                    break;
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    adapter = new MeterUserListviewAdapter(MeterUserListviewActivity.this, userLists);
                    adapter.notifyDataSetChanged();
                    listview.setAdapter(adapter);
                    currentPageTv.setText(String.valueOf(currentPage));
                    if (totalCountCursor.getCount() % 50 != 0) {
                        totalPage = totalCountCursor.getCount() / 50 + 1;
                    }else {
                        totalPage = totalCountCursor.getCount() / 50;
                    }
                    totalPageTv.setText(String.valueOf(totalPage));
                    break;
                case 1:
                    adapter = new MeterUserListviewAdapter(MeterUserListviewActivity.this, userLists);
                    adapter.notifyDataSetChanged();
                    listview.setAdapter(adapter);
                    lastPage.setClickable(true);
                    nextPage.setClickable(true);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //读取本地的抄表分区用户数据
    public void getAreaUserData(int dataStartCount) {
        userLists.clear();
        totalCountCursor = db.rawQuery("select * from MeterUser where login_user_id=?", new String[]{sharedPreferences_login.getString("userId", "")});//查询并获得游标
        areaLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? limit " + dataStartCount + ",50", new String[]{sharedPreferences_login.getString("userId", "")});//查询并获得游标
        Log.i("MeterUserLVActivity", "总的查询到" + totalCountCursor.getCount() + "条数据！");
        Log.i("MeterUserLVActivity", "分页查询到" + areaLimitCursor.getCount() + "条数据！");
        //如果游标为空，则显示没有数据图片
        if (areaLimitCursor.getCount() == 0) {
            if (noData.getVisibility() == View.GONE) {
                noData.setVisibility(View.VISIBLE);
            }
            selectPage.setVisibility(View.GONE);
            return;
        }
        if (noData.getVisibility() == View.VISIBLE) {
            noData.setVisibility(View.GONE);
        }
        selectPage.setVisibility(View.VISIBLE);
        while (areaLimitCursor.moveToNext()) {
            MeterUserListviewItem item = new MeterUserListviewItem();
            item.setMeterID(areaLimitCursor.getString(areaLimitCursor.getColumnIndex("meter_order_number")));
            item.setUserName(areaLimitCursor.getString(areaLimitCursor.getColumnIndex("user_name")));
            item.setUserID(areaLimitCursor.getString(areaLimitCursor.getColumnIndex("user_id")));
            if(!areaLimitCursor.getString(areaLimitCursor.getColumnIndex("meter_number")).equals("null")){
                item.setMeterNumber(areaLimitCursor.getString(areaLimitCursor.getColumnIndex("meter_number")));
            }else {
                item.setMeterNumber("无");
            }
            item.setLastMonth(areaLimitCursor.getString(areaLimitCursor.getColumnIndex("last_month_dosage")));
            item.setThisMonth(areaLimitCursor.getString(areaLimitCursor.getColumnIndex("this_month_dosage")));
            item.setAddress(areaLimitCursor.getString(areaLimitCursor.getColumnIndex("user_address")));
            if(areaLimitCursor.getString(areaLimitCursor.getColumnIndex("meterState")).equals("false")){
                item.setMeterState("未抄");
                item.setIfEdit(R.mipmap.userlist_red);
            }else {
                item.setMeterState("已抄");
                item.setIfEdit(R.mipmap.userlist_gray);
            }
            userLists.add(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == currentPosition){
                if(data != null){
                    item.setThisMonth(data.getStringExtra("this_month_dosage"));
                    item.setIfEdit(R.mipmap.userlist_gray);
                    item.setMeterState("已抄");
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //cursor游标操作完成以后,一定要关闭
        if (totalCountCursor != null) {
            totalCountCursor.close();
        }
        if (areaLimitCursor != null) {
            areaLimitCursor.close();
        }
        if (bookLimitCursor != null) {
            bookLimitCursor.close();
        }
        db.close();
    }
}
