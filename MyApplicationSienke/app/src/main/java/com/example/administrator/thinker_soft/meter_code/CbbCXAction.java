package com.example.administrator.thinker_soft.meter_code;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.entity.UsersInfo;
import com.example.administrator.thinker_soft.myfirstpro.lvadapter.BookNumberAdapter;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.service.DBService;
import com.example.administrator.thinker_soft.myfirstpro.util.Gadget;

import java.io.Serializable;
import java.util.List;

public class CbbCXAction extends Activity {
    private ImageView back;
    private ListView listView;
    private TextView selectMeterBook;
    private EditText editMeterNum;
    private TextView cancel, query;
    private List<String> meterList;
    private UsersInfo usInfo;
    private BookNumberAdapter bookAdapter;
    private DBService service;
    private String filepath;
    private String DBName;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbbcx);
        MyActivityManager mam = MyActivityManager.getInstance();
        mam.pushOneActivity(this);
        handler = new Handler();
        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.list_view);
        selectMeterBook = (TextView) findViewById(R.id.select_meter_book);
        editMeterNum = (EditText) findViewById(R.id.edit_meter_num);
        cancel = (TextView) findViewById(R.id.cancel);
        query = (TextView) findViewById(R.id.query);
    }

    //初始化设置
    private void defaultSetting() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
        DBName = sharedPreferences.getString("dbName", "");
        if (DBName != null && !"".equals(DBName)) {
            /*selectMeterBook.setEnabled(true);
            query.setEnabled(true);*/
            System.out.println("����");
            filepath = Environment.getDataDirectory().getPath() + "/data/" + "com.example.android_cbjactivity" + "/databases/";
            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int position,
                                        long arg3) {
                    String book = meterList.get(position);
                    selectMeterBook.setText(book);
                }
            });
        } else {
            /*selectMeterBook.setEnabled(false);
            query.setEnabled(false);*/
            Toast.makeText(getApplicationContext(), "请先完成首页文件选择哦！", Toast.LENGTH_SHORT).show();
        }
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(onClickListener);
        selectMeterBook.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);
        query.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    Gadget.closeKeybord(editMeterNum, CbbCXAction.this);
                    finish();
                    break;
                case R.id.select_meter_book:
                    Thread thread = new Thread() {
                        public void run() {
                            service = new DBService(filepath + DBName);
                            meterList = service.queryBookofUser();
                        }
                    };
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (String str : meterList)
                        System.out.println(str);
                    if (meterList.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "����Ϊ��", Toast.LENGTH_LONG).show();
                    } else {
                        bookAdapter = new BookNumberAdapter(getApplicationContext(), meterList);
                        listView.setAdapter(bookAdapter);
                        listView.setVisibility(listView.VISIBLE);
                    }
                    break;
                case R.id.cancel:
                    if (editMeterNum != null && editMeterNum.getText() != null) {
                        editMeterNum.setText("");
                    }
                    break;
                case R.id.query:
                    if ("����ѡ��".equals(selectMeterBook.getText().toString()) || "".equals(editMeterNum.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "����д��ѯ��Ϣ", Toast.LENGTH_LONG).show();
                    } else {
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                String book = selectMeterBook.getText().toString();
                                //book = book.substring(0, 1);
                                String DOMETERID = editMeterNum.getText().toString();
                                service = new DBService(filepath + DBName);
                                usInfo = service.queryUserInfobybook_meterid(book, DOMETERID);
                                System.out.println("usInfo:" + usInfo.getBOOK());
                                if (usInfo != null && null == usInfo.getUSID()) {
                                    handler.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            System.out.println("������û ��");
                                            Toast.makeText(CbbCXAction.this, "��˶Բ�ѯ��Ϣ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Intent intent = new Intent();
                                    intent.setClass(CbbCXAction.this, MeterUserDetailActivity.class);
                                    intent.putExtra("action", "CbbCXAction");
                                    intent.putExtra("usInfo", (Serializable) usInfo);
                                    startActivity(intent);

                                }
                            }
                        }.start();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
