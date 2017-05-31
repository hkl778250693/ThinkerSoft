package com.example.administrator.thinker_soft.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.adapter.UserListviewAdapter;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.model.UserListviewItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */
public class NoCheckUserListActivity extends Activity {
    private ImageView back,editDelete;
    private ListView listView;
    private TextView name;
    private List<UserListviewItem> noCheckUserItemList = new ArrayList<>();
    private SQLiteDatabase db;  //数据库
    private MySqliteHelper helper; //数据库帮助类
    private SharedPreferences sharedPreferences,sharedPreferences_login;
    private SharedPreferences.Editor editor;
    private EditText etSearch;//搜索框
    private TextView noData;
    private ArrayList<String> stringList = new ArrayList<>();//保存字符串参数
    private int task_total_numb = 0;
    private UserListviewItem item;
    private int currentPosition = 0;  //点击listview  当前item的位置
    private UserListviewAdapter userListviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);

        bindView();//绑定控件
        defaultSetting();//初始化设置
        getTaskParams(); //获取任务编号参数
        new Thread() {
            @Override
            public void run() {
                if(task_total_numb != 0){
                    for (int i = 0; i < task_total_numb; i++) {
                        getUserInfo(stringList.get(i),sharedPreferences_login.getString("login_name",""));//读取未检任务数据
                    }
                    handler.sendEmptyMessage(0);
                }else {
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
        setViewClickListener();//点击事件
    }

    //绑定控件ID
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.listview);
        noData = (TextView) findViewById(R.id.no_data);
        name = (TextView) findViewById(R.id.name);
        etSearch = (EditText) findViewById(R.id.etSearch);
        editDelete = (ImageView) findViewById(R.id.edit_delete);
    }

    //初始化设置
    private void defaultSetting() {
        name.setText("未检用户");
        helper = new MySqliteHelper(NoCheckUserListActivity.this, 1);
        db = helper.getWritableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = this.getSharedPreferences(sharedPreferences_login.getString("login_name","")+"data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //点击事件
    private void setViewClickListener(){
        back.setOnClickListener(onClickListener);
        editDelete.setOnClickListener(onClickListener);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = noCheckUserItemList.get((int) parent.getAdapter().getItemId(position));
                currentPosition = position;
                Intent intent = new Intent(NoCheckUserListActivity.this, UserDetailInfoActivity.class);
                intent.putExtra("position", currentPosition);
                intent.putExtra("security_id",item.getSecurityNumber());
                intent.putExtra("user_number",item.getUserId());
                intent.putExtra("user_name",item.getUserName());
                intent.putExtra("meter_number",item.getNumber());
                intent.putExtra("user_phone_number",item.getPhoneNumber());
                intent.putExtra("user_address",item.getAdress());
                intent.putExtra("check_type",item.getSecurityType());
                intent.putExtra("if_checked",item.getIfChecked());
                intent.putExtra("if_upload",item.getIfUpload());
                startActivityForResult(intent, currentPosition);
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("UserListActivity", "onTextChanged进来了" );
                if(TextUtils.isEmpty(s.toString().trim())){
                    listView.clearTextFilter();    //搜索文本为空时，清除ListView的过滤
                    if(userListviewAdapter != null){
                        userListviewAdapter.getFilter().filter("");
                    }
                    if (editDelete.getVisibility() == View.VISIBLE) {
                        editDelete.setVisibility(View.GONE);  //当输入框为空时，叉叉消失
                    }
                }else {
                    if(userListviewAdapter != null){
                        userListviewAdapter.getFilter().filter(s);
                    }
                    if (editDelete.getVisibility() == View.GONE) {
                        editDelete.setVisibility(View.VISIBLE);  //反之则显示
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("UserListActivity_after", "afterTextChanged进来了" );
            }
        });
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    NoCheckUserListActivity.this.finish();
                    break;
                case R.id.edit_delete:
                    etSearch.setText("");
                    editDelete.setVisibility(View.GONE);
                    if(userListviewAdapter != null){
                        userListviewAdapter.getFilter().filter("");
                    }
                    break;
            }
        }
    };


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    userListviewAdapter = new UserListviewAdapter(NoCheckUserListActivity.this, noCheckUserItemList);
                    userListviewAdapter.notifyDataSetChanged();
                    listView.setAdapter(userListviewAdapter);
                    break;
                case 1:
                    if (noData.getVisibility() == View.GONE) {
                        Log.i("ContinueCheckActivity", "显示没有用户数据照片！");
                        noData.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //获取任务编号参数
    public void getTaskParams() {
        if (sharedPreferences.getStringSet("stringSet",null) != null && sharedPreferences.getInt("task_total_numb",0) != 0) {
            Iterator iterator = sharedPreferences.getStringSet("stringSet",null).iterator();
            while (iterator.hasNext()){
                stringList.add(iterator.next().toString());
            }
            task_total_numb = sharedPreferences.getInt("task_total_numb",0);
        }
    }

    //读取下载到本地的任务数据
    public void getUserInfo(String taskId,String loginName) {
        Cursor cursor = db.rawQuery("select * from User where taskId=? and loginName=?", new String[]{taskId,loginName});
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            if (noData.getVisibility() == View.GONE) {
                noData.setVisibility(View.VISIBLE);
            }
            return;
        }
        if (noData.getVisibility() == View.VISIBLE) {
            noData.setVisibility(View.GONE);
        }
        while (cursor.moveToNext()) {
            if(cursor.getString(10).equals("false")){
                UserListviewItem item = new UserListviewItem();
                item.setSecurityNumber(cursor.getString(1));
                item.setUserName(cursor.getString(2));
                item.setNumber(cursor.getString(3));
                item.setPhoneNumber(cursor.getString(4));
                item.setSecurityType(cursor.getString(5));
                item.setUserId(cursor.getString(6));
                item.setAdress(cursor.getString(8));
                item.setTaskId(cursor.getString(9));
                Log.i("NoCheckUserListActivity", "安检状态为false");
                item.setIfEdit(R.mipmap.userlist_red);
                item.setIfChecked("未检");
                if(cursor.getString(17).equals("true")){
                    item.setIfUpload("已上传");
                }else {
                    item.setIfUpload("");
                }
                noCheckUserItemList.add(item);
            }
        }
        //cursor游标操作完成以后,一定要关闭
        cursor.close();
    }

    //更新用户表是否安检状态
    public void updateUserCheckedState(){
        ContentValues values = new ContentValues();
        values.put("ifChecked","true");
        Log.i("UserList=update", "更新安检状态为true");
        db.update("User",values,"securityNumber=?",new String[]{item.getSecurityNumber()});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == currentPosition){
                updateUserCheckedState(); //更新本地数据库用户表安检状态
                item.setIfEdit(R.mipmap.userlist_gray);
                item.setIfChecked("已检");
                noCheckUserItemList.remove(currentPosition);
                userListviewAdapter.notifyDataSetChanged();
                Log.i("NoCheckUserListActivity", "页面回调进来了");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放和数据库的连接
        db.close();
    }
}
