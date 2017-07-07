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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.adapter.EmailInfoAdapter;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */
public class BusinessSentActivity extends Activity {
    private ImageView back;
    private Button checked,delete;
    private ListView listViewEmail;
    private BusinessEmailListviewItem item;
    private EmailInfoAdapter adapter;
    private List<BusinessEmailListviewItem> businessEmailListviewItemList = new ArrayList<>();
    private SharedPreferences sharedPreferences_login;
    private SQLiteDatabase db;  //数据库
    private Cursor cursor;
    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    adapter = new EmailInfoAdapter(BusinessSentActivity.this, businessEmailListviewItemList);
                    adapter.notifyDataSetChanged();
                    listViewEmail.setAdapter(adapter);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_email);

        bindView();//绑定控件
        defaultSetting();
        setViewClickListener();//点击事件
    }

    private void defaultSetting() {
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        final MySqliteHelper helper = new MySqliteHelper(BusinessSentActivity.this, 1);
        db = helper.getWritableDatabase();
        new Thread(){
            @Override
            public void run(){
                super.run();
                queryOaEmail();
                if (cursor.getCount() != 0) {
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();
    }


    public void bindView(){
        listViewEmail = (ListView) findViewById(R.id.listview_email);
        delete = (Button) findViewById(R.id.delete);
        back = (ImageView) findViewById(R.id.back);
        checked = (Button) findViewById(R.id.checked);

    }

    public void setViewClickListener(){
        adapter= new EmailInfoAdapter(BusinessSentActivity.this,businessEmailListviewItemList);
        listViewEmail.setAdapter(adapter);
        back.setOnClickListener(clickListener);
        listViewEmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = (BusinessEmailListviewItem) adapter.getItem(position);
                Intent intent = new Intent(BusinessSentActivity.this, BusinessEmailInfoActivity.class);
                intent.putExtra("time", item.getTime());
                startActivity(intent);
            }
        });
    }

    /**
     * 根据用户ID查询日程安排并显示listview数据*/
    private void queryOaEmail() {
        businessEmailListviewItemList.clear();
        cursor = db.rawQuery("select * from OaEmail where userId=?", new String[]{sharedPreferences_login.getString("userId", "")});
        Log.i("queryOaUserInfo", "集合长度为：" + cursor.getCount());
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            BusinessEmailListviewItem item = new BusinessEmailListviewItem();
            item.setEmailAdress(cursor.getString(cursor.getColumnIndex("inboxAddress")));
            item.setContent(cursor.getString(cursor.getColumnIndex("content")));
            item.setTime(cursor.getString(cursor.getColumnIndex("time")));
            item.setTitle(cursor.getString(cursor.getColumnIndex("type")));
            businessEmailListviewItemList.add(item);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
                case R.id.check:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode ==RESULT_OK){
            if (requestCode ==100){
                queryOaEmail();
                adapter = new EmailInfoAdapter(BusinessSentActivity.this, businessEmailListviewItemList);
                adapter.notifyDataSetChanged();
                listViewEmail.setAdapter(adapter);
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
