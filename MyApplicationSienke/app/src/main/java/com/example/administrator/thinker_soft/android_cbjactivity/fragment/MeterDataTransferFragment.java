package com.example.administrator.thinker_soft.android_cbjactivity.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.android_cbjactivity.ChaoBiaoXuanZeActivity;
import com.example.administrator.thinker_soft.android_cbjactivity.XiaZaiActivity;
import com.example.administrator.thinker_soft.myfirstpro.entity.AreaInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.BookInfo;
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

/**
 * Created by Administrator on 2017/6/12 0012.
 */
public class MeterDataTransferFragment extends Fragment {
    private View view;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_sjcs, null);

        bindView();
        defaultSetting();
        setViewClickListener();
        return view;
    }

    //绑定控件
    private void bindView() {
        upload = (TextView) view.findViewById(R.id.upload);
        download = (TextView) view.findViewById(R.id.download);
    }

    //初始化设置
    private void defaultSetting() {
        mam = MyActivityManager.getInstance();
        mam.pushOneActivity(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("IP_PORT_DBNAME", 0);
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
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("signal", 11);
                    editor.apply();
                    Intent intent = new Intent();
                    intent = intent.setClass(getActivity(), ChaoBiaoXuanZeActivity.class);
                    int requestCode = 1;
                    startActivityForResult(intent, requestCode);
                    break;
                case R.id.download:
                    clickCount++;
                    dialogControl.put(clickCount, true);
                    downDialog = MyDialog.createLoadingDialog(getActivity(), "���ݳ�ʼ����...");
                    downDialog.setCancelable(true);
                    downDialog.setCanceledOnTouchOutside(false);
                    downDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            dialogControl.put(clickCount, false);
                        }
                    });
                    if (!JaugeInternetState.isWifiEnabled(getActivity())) {
                        Toast.makeText(getActivity(), "�����������WIFI", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    ip = sharedPreferences.getString("ip", "");
                    defaultPort = sharedPreferences.getString("port", "");
                    if ("".equals(ip) || "".equals(defaultPort)) {
                        Mytoast.showToast(getActivity(), "��⵽��δ����IP��PORT", Toast.LENGTH_SHORT);
                        break;
                    }
                    downDialog.show();
                    DownLoadData();
/*			intent.putExtra("areaList", (Serializable)areaList);
            intent.putExtra("bookList", (Serializable)bookList);
			intent.putExtra("floatRange", floatRange);
			intent.putExtra("month", month);
			intent.setClass(SJCSActivity.this, XiaZaiActivity.class);
			startActivity(intent);*/
                    break;
                default:
                    break;
            }
        }
    };

    private void DownLoadData() {
        final MyHandler dataHandler = new MyHandler();
        new Thread() {
            @Override
            public void run() {
                super.run();
                int loc = clickCount;
                boolean singal = false;
                String parameter = AssembleUpmes.initialParameter();
                SocketInteraction areaSocket = new SocketInteraction(getActivity(), Integer.parseInt(defaultPort), ip, operName, parameter, dataHandler);
                singal = areaSocket.DataDownLoadConn();
                areaSocket.closeConn();
                if (dialogControl.size() > 0) {
                    if (dialogControl.get(loc) == true) {
                        if (singal == true) {
                            dataHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "��ʼ���ݳɹ�", Toast.LENGTH_LONG).show();
                                    downDialog.dismiss();
                                }
                            });
                        } else {
                            dataHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "��ʼ����ʧ��", Toast.LENGTH_LONG).show();
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
                        intent.setClass(getActivity(), XiaZaiActivity.class);
                        startActivity(intent);
                    } else if (areaList == null && bookList == null) {
                        AlertDialog.Builder builder = null;
                        builder = new AlertDialog.Builder(getActivity());
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
