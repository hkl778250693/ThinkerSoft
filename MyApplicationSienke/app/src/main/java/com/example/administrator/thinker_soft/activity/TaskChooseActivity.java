package com.example.administrator.thinker_soft.activity;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.adapter.TaskChooseAdapter;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.model.TaskChoose;
import com.example.administrator.thinker_soft.model.TaskChooseViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/3/15 0015.
 */
public class TaskChooseActivity extends Activity {
    private TextView save, noData;
    private ListView listView;
    private List<TaskChoose> taskChooseList = new ArrayList<>();
    private HashMap<String, Object> map = new HashMap<String, Object>();
    private TaskChooseAdapter adapter;   //适配器
    private SQLiteDatabase db;  //数据库
    private MySqliteHelper helper; //数据库帮助类
    private ArrayList<Integer> integers = new ArrayList<>();//保存选中任务的序号
    private ArrayList<String> stringList = new ArrayList<>();//保存任务编号参数
    private Intent intent;
    private String defaul = "";//默认的全部不勾选
    private SharedPreferences sharedPreferences,sharedPreferences_login;
    private SharedPreferences.Editor editor;
    private Cursor cursor;
    private int taskTotalUserNumber = 0;
    private String checkState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_choose);

        defaultSetting();//初始化设置
        new Thread() {
            @Override
            public void run() {
                getTaskData(sharedPreferences_login.getString("login_name",""));//读取下载到本地的任务数据
                handler.sendEmptyMessage(1);
                super.run();
            }
        }.start();
        bindView();//绑定控件
        setViewClickListener();//点击事件
    }

    //绑定控件ID
    private void bindView() {
        save = (TextView) findViewById(R.id.save);
        noData = (TextView) findViewById(R.id.no_data);
        listView = (ListView) findViewById(R.id.listview);
    }

    //初始化设置
    private void defaultSetting() {
        helper = new MySqliteHelper(TaskChooseActivity.this, 1);
        db = helper.getReadableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = this.getSharedPreferences(sharedPreferences_login.getString("login_name","")+"data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //初始化勾选框信息，默认都是以未勾选为单位
        for (int i = 0; i < taskChooseList.size(); i++) {
            defaul = defaul + "0";
        }
    }

    //获得保存在这个activity中的选择框选中状态信息
    public void getCheckStateInfo() {
        Log.i("getCheckStateInfo==>", "读取上次保存的状态方法进来了！循环次数为："+taskChooseList.size());
        for (int i = 0; i < checkState.length(); i++) {
            Log.i("getCheckStateInfo==>", "checkState的长度为："+checkState.length());
            if (checkState.charAt(i) == '1') {
                TaskChooseAdapter.getIsCheck().put(i, true);
                Log.i("getCheckStateInfo==>", "读取上次保存的状态进来了！");
            }
        }
    }

    //点击事件
    private void setViewClickListener() {
        save.setOnClickListener(onClickListener);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskChooseViewHolder holder = (TaskChooseViewHolder) view.getTag();
                holder.checkBox.toggle();
                TaskChooseAdapter.getIsCheck().put(position,holder.checkBox.isChecked());
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.save:
                    if (cursor.getCount() == 0) {
                        Intent intent1 = new Intent(TaskChooseActivity.this, SecurityChooseActivity.class);
                        intent1.putExtra("down", 1);  //传递一个 1 的参数到主页面，让他替换fragment
                        Log.i("TaskChooseActivity==>", "传递参数成功！");
                        intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent1);
                        finish();
                    } else {
                        saveTaskInfo(); //保存选中的任务编号信息
                        if(taskTotalUserNumber != 0){
                            saveCheckStateInfo();//保存选中状态，将信息写入preference保存以备下一次读取使用
                            Toast.makeText(TaskChooseActivity.this, "保存成功！您可以到用户列表查看哦~", Toast.LENGTH_SHORT).show();
                            intent = new Intent(TaskChooseActivity.this, SecurityChooseActivity.class);
                            transferParams(); //传递任务编号参数到主页面
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(TaskChooseActivity.this, "您还没有选择任务哦~", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    };

    //保存选中的任务编号信息
    public void saveTaskInfo() {
        int count = adapter.getCount();
        Log.i("count====>", "长度为：" + count);
        for (int i = 0; i < count; i++) {
            if (TaskChooseAdapter.getIsCheck().get(i)) {
                TaskChoose taskChoose = taskChooseList.get((int) adapter.getItemId(i));
                taskTotalUserNumber += Integer.parseInt(taskChoose.getTotalUserNumber());
                map.put("taskId" + i, taskChoose.getTaskNumber());
                Log.i("taskId=========>", "这次被勾选第" + i + "个，任务编号为：" + taskChoose.getTaskNumber());
                integers.add(i);
                Log.i("integers====>", "长度为：" + integers.size());
            }
        }
        editor.putInt("taskTotalUserNumber", taskTotalUserNumber);
        Log.i("taskTotalUserNumber=>", "任务总户数为：" + taskTotalUserNumber);
        editor.commit();
    }

    //保存选中状态，将信息写入preference保存以备下一次读取使用
    public void saveCheckStateInfo() {
        String flag = "";
        int count = adapter.getCount();
        Log.i("count====>", "长度为：" + count);
        for (int i = 0; i < count; i++) {
            if (TaskChooseAdapter.getIsCheck().get(i)) {  //判断如果此时是选中状态就保存到SharedPreferences，“1”表示选中，0表示没选中
                flag = flag + '1';
            } else {
                flag = flag + '0';
            }
        }
        editor.putString("checkState", flag);//将数据已字符串形式保存起来，下次读取再用
        Log.i("saveCheckStateInfo=>", "checkState状态为：" + flag);
        editor.commit();
    }

    //删除选中的任务编号信息
    public void deleteChecked() {
        HashMap<Integer, Boolean> isCheck_delete = adapter.getHashMap();
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            int position = i - (count - adapter.getCount());
            if (isCheck_delete.get(i) != null && isCheck_delete.get(i)) {
                isCheck_delete.remove(i);
                adapter.removeData(position);
                Log.i("removeData====>", "删除的位置是：" + position);
            }
        }
        adapter.notifyDataSetChanged();
    }

    //传递任务编号参数到主页面
    public void transferParams() {
        Bundle bundle = new Bundle();
        for (int j = 0; j < integers.size(); j++) {
            stringList.add(map.get("taskId" + integers.get(j)).toString());
            Log.i("bundle.putString====>", "传递的参数为：" + map.get("taskId" + integers.get(j)).toString());
        }
        bundle.putStringArrayList("taskId", stringList);
        bundle.putInt("task_total_numb", integers.size());
        bundle.putIntegerArrayList("integerList", integers);
        intent.putExtras(bundle);
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

    //读取下载到本地的任务数据
    public void getTaskData(String loginName) {
        cursor = db.rawQuery("select * from Task where loginName=?", new String[]{loginName});//查询并获得游标
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            save.setText("去下载");
            if (noData.getVisibility() == View.GONE) {
                noData.setVisibility(View.VISIBLE);
            }
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    super.run();
                }
            }.start();
            return;
        }
        save.setText("保存");
        if (noData.getVisibility() == View.VISIBLE) {
            noData.setVisibility(View.GONE);
        }
        while (cursor.moveToNext()) {
            TaskChoose taskChoose = new TaskChoose();
            taskChoose.setTaskName(cursor.getString(1));
            taskChoose.setTaskNumber(cursor.getString(2));
            taskChoose.setCheckType(cursor.getString(3));
            taskChoose.setTotalUserNumber(cursor.getString(4));
            taskChoose.setEndTime(cursor.getString(5));
            taskChoose.setRestCount("("+cursor.getString(7)+")");
            taskChooseList.add(taskChoose);
        }
        //cursor游标操作完成以后,一定要关闭
        cursor.close();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(TaskChooseActivity.this, "您还没有任务哦，快去下载吧！~", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    adapter = new TaskChooseAdapter(TaskChooseActivity.this, taskChooseList);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    checkState = sharedPreferences.getString("checkState", defaul); //如果没有获取到的话默认是0
                    if(!checkState.equals("")){
                        getCheckStateInfo();//获得保存在这个activity中的选择框选中状态信息
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放和数据库的连接
        db.close();
    }
}
