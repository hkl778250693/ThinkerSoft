package com.example.administrator.thinker_soft.meter_code.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.activity.MeterDataDownloadActivity;
import com.example.administrator.thinker_soft.meter_code.adapter.MeterFileSelectListAdapter;
import com.example.administrator.thinker_soft.meter_code.model.MeterSingleSelectItem;
import com.example.administrator.thinker_soft.mode.MyAnimationUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.meter_code.model.AreaInfo;
import com.example.administrator.thinker_soft.meter_code.model.BookInfo;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/12 0012.
 */
public class MeterDataTransferFragment extends Fragment {
    private View view,fileSelectView, uploadView,loadingView;
    private TextView upload, download;
    private MyActivityManager mam;
    private LayoutInflater layoutInflater;
    private PopupWindow fileWindow, uploadWindow,loadingWindow;
    private ListView fileListView;
    private ImageView frameAnimation;
    private AnimationDrawable animationDrawable;
    private LinearLayout rootLinearlayout, noData;
    private MeterSingleSelectItem fileItem;
    private MeterFileSelectListAdapter fileAdapter;
    private List<MeterSingleSelectItem> fileList = new ArrayList<>();
    private SQLiteDatabase db;  //数据库
    private SharedPreferences public_sharedPreferences, sharedPreferences_login;
    private String ip, port;  //接口ip地址   端口
    private String resultBook, resultArea; //抄表本结果，抄表分区结果
    public int responseCode = 0;
    private ArrayList<BookInfo> bookInfoArrayList = new ArrayList<>();   //抄表本集合
    private ArrayList<AreaInfo> areaInfoArrayList = new ArrayList<>();   //抄表分区集合
    private TextView tips;  //加载进度的提示

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_data_transfer, null);

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
        MySqliteHelper helper = new MySqliteHelper(getActivity(), 1);
        db = helper.getWritableDatabase();
        public_sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        sharedPreferences_login = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
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
                    showFileSelectWindow();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            getFileInfo();
                        }
                    }.start();
                    break;
                case R.id.download:
                    showPopupwindow();
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                requireMeterBookData("findAllsBook.do", "companyId=" + sharedPreferences_login.getInt("company_id", 0));
                                requireMeterAreaData("qureyAreaAll.do", "companyid=" + sharedPreferences_login.getInt("company_id", 0));
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

    /**
     * 文件选择窗口
     */
    public void showFileSelectWindow() {
        layoutInflater = LayoutInflater.from(getActivity());
        fileSelectView = layoutInflater.inflate(R.layout.popupwindow_meter_single_select, null);
        fileWindow = new PopupWindow(fileSelectView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        TextView back = (TextView) fileSelectView.findViewById(R.id.back);
        fileListView = (ListView) fileSelectView.findViewById(R.id.list_view);
        TextView tips = (TextView) fileSelectView.findViewById(R.id.tips);
        noData = (LinearLayout) fileSelectView.findViewById(R.id.no_data);
        //设置点击事件
        tips.setText("请选择文件夹");
        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取选中的参数
                fileItem = (MeterSingleSelectItem) fileAdapter.getItem(position);
                Log.i("meterHomePage", "当前点击的item为：" + fileItem.getName());
                fileWindow.dismiss();
                showUploadTipsWindow(fileItem.getName());
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileWindow.dismiss();
            }
        });
        fileWindow.update();
        fileWindow.setFocusable(true);
        fileWindow.setTouchable(true);
        fileWindow.setOutsideTouchable(true);
        fileWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        fileWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        backgroundAlpha(0.6F);   //背景变暗
        fileWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        fileWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //弹出上传数据前提示窗口
    public void showUploadTipsWindow(String fileName) {
        layoutInflater = LayoutInflater.from(getActivity());
        uploadView = layoutInflater.inflate(R.layout.popupwindow_user_detail_info_save, null);
        uploadWindow = new PopupWindow(uploadView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //绑定控件ID
        TextView tips = (TextView) uploadView.findViewById(R.id.tips);
        RadioButton cancelRb = (RadioButton) uploadView.findViewById(R.id.cancel_rb);
        RadioButton saveRb = (RadioButton) uploadView.findViewById(R.id.save_rb);
        //设置点击事件
        tips.setText("确定要上传名为 '" + fileName + "' 的文件吗？");
        saveRb.setText("确定");
        cancelRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadWindow.dismiss();
            }
        });
        saveRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadWindow.dismiss();
            }
        });
        uploadWindow.update();
        uploadWindow.setFocusable(true);
        uploadWindow.setTouchable(true);
        uploadWindow.setOutsideTouchable(true);
        uploadWindow.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
        uploadWindow.setAnimationStyle(R.style.camera);
        backgroundAlpha(0.6F);   //背景变暗
        uploadWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        uploadWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //查询抄表文件信息
    public void getFileInfo() {
        fileList.clear();
        Cursor cursor = db.rawQuery("select * from MeterFile where login_user_id=?", new String[]{sharedPreferences_login.getString("userId", "")});//查询并获得游标
        Log.i("meterHomePage", "所有表册ID个数为：" + cursor.getCount());
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(6);
            return;
        }
        while (cursor.moveToNext()) {
            MeterSingleSelectItem item = new MeterSingleSelectItem();
            item.setName(cursor.getString(cursor.getColumnIndex("fileName")));
            fileList.add(item);
        }
        handler.sendEmptyMessage(0);
        cursor.close();
    }

    //show加载动画
    public void showPopupwindow() {
        layoutInflater = LayoutInflater.from(getActivity());
        loadingView = layoutInflater.inflate(R.layout.popupwindow_query_loading, null);
        loadingWindow = new PopupWindow(loadingView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameAnimation = (ImageView) loadingView.findViewById(R.id.frame_animation);
        tips = (TextView) loadingView.findViewById(R.id.tips);
        tips.setText("数据初始化中......");
        loadingWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        loadingWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        loadingWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        loadingWindow.setAnimationStyle(R.style.camera);
        loadingWindow.update();
        loadingWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        startFrameAnimation();
        loadingWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
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
                ip = "88.88.88.66:";
            }
            if (!public_sharedPreferences.getString("security_port", "").equals("")) {
                port = public_sharedPreferences.getString("security_port", "");
            } else {
                port = "8088";
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
        } catch (UnsupportedEncodingException | MalformedURLException | JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("IOException==========>", "网络请求异常!");
            handler.sendEmptyMessage(3);
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
                ip = "88.88.88.66:";
            }
            if (!public_sharedPreferences.getString("security_port", "").equals("")) {
                port = public_sharedPreferences.getString("security_port", "");
            } else {
                port = "8088";
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
        } catch (UnsupportedEncodingException | MalformedURLException | JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("IOException==========>", "网络请求异常!");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    fileAdapter = new MeterFileSelectListAdapter(getActivity(),fileList,1);
                    fileListView.setAdapter(fileAdapter);
                    MyAnimationUtils.viewGroupOutAnimation(getActivity(),fileListView,0.1F);
                    break;
                case 1:
                    try {
                        JSONArray jsonArray = new JSONArray(resultBook);
                        bookInfoArrayList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            BookInfo item = new BookInfo();
                            item.setID(object.optInt("n_book_id", 0) + "");
                            item.setBOOK(object.optString("c_book_name", ""));
                            bookInfoArrayList.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    loadingWindow.dismiss();
                    Toast.makeText(getActivity(), "没有抄表本数据哦！", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    loadingWindow.dismiss();
                    Toast.makeText(getActivity(), "网络请求超时！", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    try {
                        JSONArray jsonArray = new JSONArray(resultArea);
                        areaInfoArrayList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            AreaInfo item = new AreaInfo();
                            item.setArea(object.optString("areaName", ""));
                            item.setID(object.optInt("areaId", 0) + "");
                            areaInfoArrayList.add(item);
                        }
                        if (bookInfoArrayList.size() != 0 || areaInfoArrayList.size() != 0) {
                            Intent intent = new Intent(getActivity(), MeterDataDownloadActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("bookInfoArrayList", bookInfoArrayList);
                            bundle.putParcelableArrayList("areaInfoArrayList", areaInfoArrayList);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            loadingWindow.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    loadingWindow.dismiss();
                    Toast.makeText(getActivity(), "没有抄表分区数据哦！", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    noData.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

}
