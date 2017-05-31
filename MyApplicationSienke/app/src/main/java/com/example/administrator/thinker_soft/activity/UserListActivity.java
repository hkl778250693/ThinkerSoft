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
 * Created by Administrator on 2017/3/16.
 */
public class UserListActivity extends Activity {
    private ImageView back,editDelete;
    private ListView listView;
    private TextView noData,name;
    private List<UserListviewItem> userListviewItemList = new ArrayList<>();
    private ArrayList<String> stringList = new ArrayList<>();//保存字符串参数
    private int task_total_numb = 0;
    private SQLiteDatabase db;  //数据库
    private MySqliteHelper helper; //数据库帮助类
    private int currentPosition;  //点击listview
    private UserListviewAdapter userListviewAdapter;
    private UserListviewItem item;
    private SharedPreferences sharedPreferences,sharedPreferences_login;
    private SharedPreferences.Editor editor;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);

        bindView();//绑定控件
        defaultSetting();//初始化设置
        getTaskParams();//获取任务编号参数
        Log.i("UserListActivity----", "查询的任务个数为：" + task_total_numb);
        new Thread() {
            @Override
            public void run() {
                if (task_total_numb != 0) {
                    for (int i = 0; i < task_total_numb; i++) {
                        getUserData(stringList.get(i),sharedPreferences_login.getString("login_name",""));//读取所有安检用户数据
                        Log.i("UserListActivity----", "查询的任务编号是：" + stringList.get(i));
                    }
                    handler.sendEmptyMessage(0);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
        setOnClickListener();//点击事件
    }

    //绑定控件ID
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        name = (TextView) findViewById(R.id.name);
        listView = (ListView) findViewById(R.id.listview);
        noData = (TextView) findViewById(R.id.no_data);
        etSearch = (EditText) findViewById(R.id.etSearch);
        editDelete = (ImageView) findViewById(R.id.edit_delete);
    }

    //初始化设置
    private void defaultSetting() {
        name.setText("用户列表");
        helper = new MySqliteHelper(UserListActivity.this, 1);
        db = helper.getWritableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = this.getSharedPreferences(sharedPreferences_login.getString("login_name","")+"data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //点击事件
    private void setOnClickListener() {
        back.setOnClickListener(onClickListener);
        editDelete.setOnClickListener(onClickListener);
        listView.setTextFilterEnabled(true);  // 开启过滤功能
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = userListviewItemList.get((int) parent.getAdapter().getItemId(position));
                currentPosition = position;
                Intent intent = new Intent(UserListActivity.this, UserDetailInfoActivity.class);
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
                    //listView.setFilterText(s.toString().trim());  //设置过滤关键字
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
                    UserListActivity.this.finish();
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    userListviewAdapter = new UserListviewAdapter(UserListActivity.this, userListviewItemList);
                    userListviewAdapter.notifyDataSetChanged();
                    listView.setAdapter(userListviewAdapter);
                    break;
                case 1:
                    if (noData.getVisibility() == View.GONE) {
                        Log.i("UserListActivity_noData", "显示没有用户数据照片！");
                        noData.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };


    //获取任务编号参数
    public void getTaskParams() {
        if (sharedPreferences.getStringSet("stringSet", null) != null && sharedPreferences.getInt("task_total_numb", 0) != 0) {
            Iterator iterator = sharedPreferences.getStringSet("stringSet", null).iterator();
            while (iterator.hasNext()) {
                stringList.add(iterator.next().toString());
                Log.i("UserListActivity====>", "得到的参数为：" +stringList);
            }
            task_total_numb = sharedPreferences.getInt("task_total_numb", 0);
        }
    }

    /**
     * 查询方法参数详解
     * <p/>
     * public Cursor query (boolean distinct, String table, String[] columns,
     * String selection, String[] selectionArgs,
     * String groupBy, String having,
     * String orderBy, String limit,
     * CancellationSignal cancellationSignal)
     * <p/>
     * 参数介绍 :
     * <p/>
     * -- 参数① distinct : 是否去重复, true 去重复;
     * <p/>
     * -- 参数② table : 要查询的表名;
     * <p/>
     * -- 参数③ columns : 要查询的列名, 如果为null, 就会查询所有的列;
     * <p/>
     * -- 参数④ whereClause : 条件查询子句, 在这里可以使用占位符 "?";
     * <p/>
     * -- 参数⑤ whereArgs : whereClause查询子句中的传入的参数值, 逐个替换 "?" 占位符;
     * <p/>
     * -- 参数⑥ groupBy: 控制分组, 如果为null 将不会分组;
     * <p/>
     * -- 参数⑦ having : 对分组进行过滤;
     * <p/>
     * -- 参数⑧ orderBy : 对记录进行排序;
     * <p/>
     * -- 参数⑨ limite : 用于分页, 如果为null, 就认为不进行分页查询;
     * <p/>
     * -- 参数⑩ cancellationSignal : 进程中取消操作的信号, 如果操作被取消, 当查询命令执行时会抛出 OperationCanceledException 异常;
     */

    //读取下载到本地的用户数据
    public void getUserData(String taskId,String loginName) {
        Log.i("UserListActivityget=", "查询用户数据进来了：！");
        Cursor cursor = db.rawQuery("select * from User where taskId=? and loginName=?", new String[]{taskId,loginName});//查询并获得游标
        Log.i("UserListActivityget=", "数据库进来了：！");
        Log.i("UserListActivityget=", "任务编号是：" + taskId);
        Log.i("UserListActivityget=", "有" + cursor.getCount() + "条数据！");
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            if (noData.getVisibility() == View.GONE) {
                Log.i("UserList_getUserData", "显示没有用户数据照片！");
                noData.setVisibility(View.VISIBLE);
            }
            return;
        }
        if (noData.getVisibility() == View.VISIBLE) {
            noData.setVisibility(View.GONE);
        }

        while (cursor.moveToNext()) {
            UserListviewItem userListviewItem = new UserListviewItem();
            userListviewItem.setSecurityNumber(cursor.getString(1));
            userListviewItem.setUserName(cursor.getString(2));
            userListviewItem.setTaskId(cursor.getString(9));
            userListviewItem.setNumber(cursor.getString(3));
            userListviewItem.setPhoneNumber(cursor.getString(4));
            userListviewItem.setSecurityType(cursor.getString(5));
            userListviewItem.setUserId(cursor.getString(6));
            userListviewItem.setAdress(cursor.getString(8));
            Log.i("UserList=cursor", "安检状态为 = " + cursor.getString(10));
            if (cursor.getString(10).equals("true")) {
                Log.i("UserList=cursor", "安检状态为true");
                userListviewItem.setIfEdit(R.mipmap.userlist_gray);
                userListviewItem.setIfChecked("已检");
            } else {
                Log.i("UserList=cursor", "安检状态为false");
                userListviewItem.setIfEdit(R.mipmap.userlist_red);
                userListviewItem.setIfChecked("未检");
            }
            if(cursor.getString(17).equals("true")){
                userListviewItem.setIfUpload("已上传");
            }else {
                userListviewItem.setIfUpload("");
            }
            userListviewItemList.add(userListviewItem);
            Log.i("UserListActivityget=", "用户列表的长度为：" + userListviewItemList.size());
        }
        cursor.close(); //游标关闭
    }

    //更新用户表是否安检状态
    public void updateUserCheckedState() {
        ContentValues values = new ContentValues();
        values.put("ifChecked", "true");
        Log.i("UserList=update", "更新安检状态为true");
        db.update("User", values, "securityNumber=? and loginName=?", new String[]{item.getSecurityNumber(),sharedPreferences_login.getString("login_name","")});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == currentPosition) {
                updateUserCheckedState(); //更新本地数据库用户表安检状态
                item.setIfEdit(R.mipmap.userlist_gray);
                item.setIfChecked("已检");
                userListviewAdapter.notifyDataSetChanged();
                Log.i("UserList=ActivityResult", "页面回调进来了");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close(); //释放和数据库的连接
    }
}
