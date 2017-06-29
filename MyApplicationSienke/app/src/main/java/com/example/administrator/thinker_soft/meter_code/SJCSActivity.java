package com.example.administrator.thinker_soft.meter_code;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.entity.AreaInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.BookInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.UsersInfo;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.service.DBService;
import com.example.administrator.thinker_soft.myfirstpro.threadsocket.SocketInteraction;
import com.example.administrator.thinker_soft.myfirstpro.util.AssembleUpmes;
import com.example.administrator.thinker_soft.myfirstpro.util.JaugeInternetState;
import com.example.administrator.thinker_soft.myfirstpro.util.JsonAnalyze;
import com.example.administrator.thinker_soft.myfirstpro.util.MyDialog;
import com.example.administrator.thinker_soft.myfirstpro.util.Mytoast;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SJCSActivity extends Activity {
    private List<BookInfo> bookList;
    private List<AreaInfo> areaList;
    private Dialog upDialog;
    private Dialog downDialog;
    private String tt = "p";
    private TextView upload, download;
    private SharedPreferences sharedPreferences;
    //����ΪSoecket��������
    private String ip;
    private String defaultPort;
    private String operName = "NANBU";
    private Boolean signal = false;
    private DBService dbService;
    private String dbName;
    private final String filepath = Environment.getDataDirectory().getPath() + "/data/" + "com.example.android_cbjactivity" + "/databases/";
    private int upCount;
    private int minusCount;
    private Map<Integer, Boolean> dialogControl;
    private int clickCount;
    private static Handler dataHandler;
    private MyActivityManager mam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjcs);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        upload = (TextView) findViewById(R.id.upload);
        download = (TextView) findViewById(R.id.download);
    }

    //初始化设置
    private void defaultSetting() {
        mam = MyActivityManager.getInstance();
        mam.pushOneActivity(this);
        sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
        dialogControl = new HashMap<Integer, Boolean>();
    }

    //点击事件
    public void setViewClickListener() {
        upload.setOnClickListener(clickListener);
        download.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.upload:
                    clickCount++;
                    dialogControl.put(clickCount, true);
                    downDialog = MyDialog.createLoadingDialog(SJCSActivity.this, "���ݳ�ʼ����...");
                    downDialog.setCancelable(true);
                    downDialog.setCanceledOnTouchOutside(false);
                    downDialog.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            dialogControl.put(clickCount, false);
                        }
                    });
                    if (!JaugeInternetState.isWifiEnabled(getApplicationContext())) {
                        Toast.makeText(SJCSActivity.this, "�����������WIFI", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    ip = sharedPreferences.getString("ip", "");
                    defaultPort = sharedPreferences.getString("port", "");
                    if ("".equals(ip) || "".equals(defaultPort)) {
                        Mytoast.showToast(SJCSActivity.this, "��⵽��δ����IP��PORT", Toast.LENGTH_SHORT);
                        break;
                    }
                    downDialog.show();
                    DownLoadData();
/*			intent.putExtra("areaList", (Serializable)areaList);
            intent.putExtra("bookList", (Serializable)bookList);
			intent.putExtra("floatRange", floatRange);
			intent.putExtra("month", month);
			intent.setClass(SJCSActivity.this, MeterDataDownloadActivity.class);
			startActivity(intent);*/
                    break;
                case R.id.download:
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("signal", 11);
                    editor.commit();
                    Intent intent = new Intent();
                    intent = intent.setClass(SJCSActivity.this, MeterSelectActivity.class);
                    int requestCode = 1;
                    startActivityForResult(intent, requestCode);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v("onSaveInstanceState:", "onSaveInstanceState()");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ip = sharedPreferences.getString("ip", "");
        defaultPort = sharedPreferences.getString("port", "");
        if ("".equals(ip) || ip == null) {
            Toast.makeText(SJCSActivity.this, "������IP", Toast.LENGTH_LONG).show();
        } else if ("".equals(defaultPort) || defaultPort == null) {
            Toast.makeText(SJCSActivity.this, "�����ö˿�", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
            dbName = data.getAction();
        final Handler dataHandler = new Handler();
        if (requestCode == resultCode && !"".equals(dbName)) {
            if (!"û���ļ�".equals(dbName) && !"".equals(dbName)) {
                if (!JaugeInternetState.isWifiEnabled(getApplicationContext())) {
                    Toast.makeText(this, "�����������WIFI", Toast.LENGTH_SHORT).show();
                    return;
                }
                upDialog = MyDialog.createLoadingDialog(SJCSActivity.this, "�����ϴ�...");
                upDialog.setCancelable(false);
                upDialog.setCanceledOnTouchOutside(false);
                upDialog.show();
                new Thread() {
                    public void run() {
                        dbService = new DBService(filepath + dbName);
                        List<UsersInfo> usersInfos = dbService.queryAllUsersInfo();
                        final int totalcount = usersInfos.size();
                        upCount = 0;
                        minusCount = 0;
                        if (totalcount <= 0) {
                            dataHandler.post(new Runnable() {
                                @SuppressLint("NewApi")
                                @Override
                                public void run() {
                                    Toast.makeText(SJCSActivity.this, "�Բ��� û�п��ϴ�����", Toast.LENGTH_LONG).show();
                                    upDialog.dismiss();
                                }
                            });
                            return;
                        }
                        int length = 50;
                        int totalnum = totalcount / 50;
                        if (totalcount % 50 != 0) {
                            totalnum = totalnum + 1;
                        }
                        List<UsersInfo> infos = null;
                        for (int i = 0; i < totalnum; i++) {
                            if (i != totalnum - 1) {
                                infos = usersInfos.subList(length * i, length * (i + 1));
                            } else {
                                infos = usersInfos.subList(length * i, totalcount);
                            }
                            //��һ�� �޸�����Ҫ�ϴ������ݱ�־Ϊ"1" ���ϴ�
                            dbService = new DBService(filepath + dbName);
                            dbService.modifytheStateofUsersyes(infos);
                            String parameterBag = AssembleUpmes.upLoadParameter(infos);
                            SocketInteraction interaction = new SocketInteraction(getApplicationContext(), Integer.parseInt(defaultPort), ip, operName, parameterBag, dataHandler);
                            String result = interaction.DataUpLoadConn();
                            interaction.closeConn();
                            System.out.println("result:" + result);
                            if (!"���粻�ȶ������ݻ�ȡʧ��!".equals(result) && !"������δ��Ӧ".equals(result)
                                    && !"����Ϊ�գ���˶���������!".equals(result)
                                    && !"���Ĺؼ�������".equals(result) && !"".equals(result)) {
                                if (!"�ɹ�".equals(result)) {
                                    result = result.substring(1, result.length() - 1);
                                    String[] usIDS = result.split(",");
                                    dbService = new DBService(filepath + dbName);
                                    dbService.modifytheStateArrayofUserno(usIDS);
                                    upCount = upCount + (infos.size() - usIDS.length);
                                    System.out.println("+++++++++++++++++++++=���result��" + result);
                                    for (int j = 0; j < usIDS.length; j++) {
                                        System.out.println("+++++++++++++++++++++=���usIDS[" + j + "]��" + usIDS[j]);
                                    }
                                    tt = "p";
                                } else {
                                    upCount = upCount + infos.size();
                                    tt = "p";
/*									dbService = new DBService(filepath+dbName);
									dbService.modifytheStateofUsersyes(infos);*/
                                }
                            } else {
                                dbService = new DBService(filepath + dbName);
                                dbService.modifytheStateofUsersno(infos);
                                tt = "s";
                            }
/*							for (int j = 0; j < infos.size(); j++) {							
								int dosage =  Integer.parseInt(infos.get(j).getTHISMONTH_DOSAGE());
								if(dosage<0){	
									minusCount++;
								}
							}*/
                        }
                        dataHandler.post(new Runnable() {
                            @SuppressLint("NewApi")
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SJCSActivity.this);
                                if (tt.equals("s")) {

                                    builder.setMessage("�����쳣���������ϴ���");
                                } else {
                                    //"���γɹ��ϴ� "+(upCount)+" ��\nʧ���ϴ� "+(totalcount-upCount)+" ��\n��ˮ����(�����û�) "+minusCount+" ��"
                                    builder.setMessage("���γɹ��ϴ� " + (upCount) + " ��\nʧ���ϴ� " + (totalcount - upCount) + " ��");
                                }
                                builder.show();
                                upDialog.dismiss();

                            }
                        });
                    }

                    ;
                }.start();
            }
        }
    }

    private void DownLoadData() {
        final MyHandler dataHandler = new MyHandler();
        new Thread() {
            @Override
            public void run() {
                super.run();
                int loc = clickCount;
                boolean singal = false;
                String parameter = AssembleUpmes.initialParameter();
                SocketInteraction areaSocket = new SocketInteraction(getApplicationContext(), Integer.parseInt(defaultPort), ip, operName, parameter, dataHandler);
                singal = areaSocket.DataDownLoadConn();
                areaSocket.closeConn();
                if (dialogControl.size() > 0) {
                    if (dialogControl.get(loc) == true) {
                        if (singal == true) {
                            dataHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(SJCSActivity.this, "��ʼ���ݳɹ�", Toast.LENGTH_LONG).show();
                                    downDialog.dismiss();
                                }
                            });
                        } else {
                            dataHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(SJCSActivity.this, "��ʼ����ʧ��", Toast.LENGTH_LONG).show();
                                    downDialog.dismiss();
                                }
                            });
                        }
                    }
                }
            }
        }.start();
    }


    class MyHandler extends Handler {
        /**
         * ��ȡsocket�߳�����
         */
        @SuppressLint("NewApi")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int loc = clickCount;//��ǰ�̵߳����
            Bundle bundle = msg.getData();
            String data = bundle.getString("data");
            String temp[] = data.split("��");
            if (dialogControl.size() > 0) {
                Log.v("ZHOUTAO", "��ǰ�߳���ţ�" + loc);
                if (dialogControl.get(loc) == true) {
                    for (int i = 0; i < temp.length; i++) {
                        if (!"û������".equals(data)) {
                            if (temp[i].contains("AreaData")) {
                                areaList = JsonAnalyze.analyszeArea(temp[i]);
                            } else if (temp[i].contains("BookData")) {
                                bookList = JsonAnalyze.analyszeBook(temp[i]);
                            }
                        }
                    }
                    if (areaList != null && bookList != null) {
                        downDialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("areaList", (Serializable) areaList);
                        intent.putExtra("bookList", (Serializable) bookList);
                        intent.setClass(SJCSActivity.this, MeterDataDownloadActivity.class);
                        startActivity(intent);
                    } else if (areaList == null && bookList == null) {
                        AlertDialog.Builder builder = null;
                        builder = new AlertDialog.Builder(SJCSActivity.this);
                        builder.setMessage("���ݳ�ʼʧ��");
                        builder.show();
                        downDialog.dismiss();
                        return;
                    }
                }
            }
        }
    }
}
