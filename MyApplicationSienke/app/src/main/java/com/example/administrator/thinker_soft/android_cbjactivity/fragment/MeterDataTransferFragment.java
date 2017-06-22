package com.example.administrator.thinker_soft.android_cbjactivity.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.android_cbjactivity.ChaoBiaoXuanZeActivity;
import com.example.administrator.thinker_soft.android_cbjactivity.MeterDataDownloadActivity;
import com.example.administrator.thinker_soft.myfirstpro.entity.AreaInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.BookInfo;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.service.DBService;
import com.example.administrator.thinker_soft.myfirstpro.threadsocket.SocketInteraction;
import com.example.administrator.thinker_soft.myfirstpro.util.AssembleUpmes;
import com.example.administrator.thinker_soft.myfirstpro.util.JsonAnalyze;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private ImageView frameAnimation;
    private AnimationDrawable animationDrawable;
    private LinearLayout rootLinearlayout;
    private SharedPreferences public_sharedPreferences;
    private String ip, port;  //接口ip地址   端口
    private String resultBook,resultArea; //抄表本结果，抄表分区结果
    public int responseCode = 0;
    private ArrayList<String> meterBookList = new ArrayList<>();   //抄表本集合
    private ArrayList<String> meterAreaList = new ArrayList<>();   //抄表分区集合

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
        rootLinearlayout = (LinearLayout) view.findViewById(R.id.root_linearlayout);
    }

    //初始化设置
    private void defaultSetting() {
        mam = MyActivityManager.getInstance();
        mam.pushOneActivity(getActivity());
        public_sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
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
                    SharedPreferences.Editor editor = public_sharedPreferences.edit();
                    editor.putInt("signal", 11);
                    editor.apply();
                    Intent intent = new Intent();
                    intent = intent.setClass(getActivity(), ChaoBiaoXuanZeActivity.class);
                    int requestCode = 1;
                    startActivityForResult(intent, requestCode);
                    break;
                case R.id.download:
                    showPopupwindow();
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                requireMeterBookData("findAllsBook.do","companyId=1");
                                requireMeterAreaData("qureyAreaAll.do","companyid=1");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    break;
                default:
                    break;
            }
        }
    };

    //show弹出框
    public void showPopupwindow() {
        layoutInflater = LayoutInflater.from(getActivity());
        view = layoutInflater.inflate(R.layout.popupwindow_query_loading, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameAnimation = (ImageView) view.findViewById(R.id.frame_animation);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        //popupWindow.update();
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        startFrameAnimation();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //开始帧动画
    public void startFrameAnimation() {
        frameAnimation.setBackgroundResource(R.drawable.frame_animation_list);
        animationDrawable = (AnimationDrawable) frameAnimation.getDrawable();
        animationDrawable.start();
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getActivity().getWindow().setAttributes(lp);
    }

    //请求抄表本的数据
    private void requireMeterBookData(final String method, final String keyAndValue) {
        try {
            URL url;
            HttpURLConnection httpURLConnection;
            Log.i("sharedPreferences====>", public_sharedPreferences.getString("IP", ""));
            if (!public_sharedPreferences.getString("security_ip", "").equals("")) {
                ip = public_sharedPreferences.getString("security_ip", "");
            } else {
                ip = "88.88.88.31:";
            }
            if (!public_sharedPreferences.getString("security_port", "").equals("")) {
                port = public_sharedPreferences.getString("security_port", "");
            } else {
                port = "8080";
            }
            String httpUrl = "http://" + ip + port + "/SMDemo/" + method;
            //有参数传递
            if (!keyAndValue.equals("")) {
                url = new URL(httpUrl + "?" + keyAndValue);
                //没有参数传递
            } else {
                url = new URL(httpUrl);
            }
            Log.i("MeterDataTransferFrag", url + "");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(6000);
            httpURLConnection.setReadTimeout(6000);
            httpURLConnection.connect();
            //传回的数据解析成String
            Log.i("responseCode====>", httpURLConnection.getResponseCode() + "");
            if ((responseCode = httpURLConnection.getResponseCode()) == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    stringBuilder.append(str);
                }
                resultBook = stringBuilder.toString();
                Log.i("MeterDataTransferFrag", resultBook);
                JSONArray jsonArray = new JSONArray(resultBook);
                if (jsonArray.length() != 0) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(2);
                }
            } else {
                handler.sendEmptyMessage(3);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("IOException==========>", "网络请求异常!");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //请求抄表分区的数据
    private void requireMeterAreaData(final String method, final String keyAndValue) {
        try {
            URL url;
            HttpURLConnection httpURLConnection;
            Log.i("sharedPreferences====>", public_sharedPreferences.getString("IP", ""));
            if (!public_sharedPreferences.getString("security_ip", "").equals("")) {
                ip = public_sharedPreferences.getString("security_ip", "");
            } else {
                ip = "88.88.88.31:";
            }
            if (!public_sharedPreferences.getString("security_port", "").equals("")) {
                port = public_sharedPreferences.getString("security_port", "");
            } else {
                port = "8080";
            }
            String httpUrl = "http://" + ip + port + "/SMDemo/" + method;
            //有参数传递
            if (!keyAndValue.equals("")) {
                url = new URL(httpUrl + "?" + keyAndValue);
                //没有参数传递
            } else {
                url = new URL(httpUrl);
            }
            Log.i("MeterDataTransferFrag", url + "");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(6000);
            httpURLConnection.setReadTimeout(6000);
            httpURLConnection.connect();
            //传回的数据解析成String
            Log.i("responseCode====>", httpURLConnection.getResponseCode() + "");
            if ((responseCode = httpURLConnection.getResponseCode()) == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    stringBuilder.append(str);
                }
                resultArea = stringBuilder.toString();
                Log.i("MeterDataTransferFrag", resultArea);
                JSONArray jsonArray = new JSONArray(resultArea);
                if (jsonArray.length() != 0) {
                    handler.sendEmptyMessage(4);
                } else {
                    handler.sendEmptyMessage(5);
                }
            } else {
                handler.sendEmptyMessage(3);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("IOException==========>", "网络请求异常!");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    try {
                        JSONArray jsonArray = new JSONArray(resultBook);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            meterBookList.add(object.optString("c_book_name",""));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    popupWindow.dismiss();
                    Toast.makeText(getActivity(), "没有抄表本数据哦！", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    popupWindow.dismiss();
                    Toast.makeText(getActivity(), "网络请求超时！", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    try {
                        JSONArray jsonArray = new JSONArray(resultArea);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            meterAreaList.add(object.optString("areaName",""));
                        }
                        if(meterBookList.size() != 0 || meterAreaList.size() != 0){
                            Intent intent = new Intent(getActivity(),MeterDataDownloadActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putStringArrayList("meterBookList",meterBookList);
                            bundle.putStringArrayList("meterAreaList",meterAreaList);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            popupWindow.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    popupWindow.dismiss();
                    Toast.makeText(getActivity(), "没有抄表分区数据哦！", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
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
                        intent.setClass(getActivity(), MeterDataDownloadActivity.class);
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
