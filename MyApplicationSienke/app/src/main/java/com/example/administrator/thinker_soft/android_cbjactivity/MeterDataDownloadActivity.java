package com.example.administrator.thinker_soft.android_cbjactivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.android_cbjactivity.modle.MeterAreaViewHolder;
import com.example.administrator.thinker_soft.android_cbjactivity.modle.MeterBookViewHolder;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myfirstpro.entity.AreaInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.BookInfo;
import com.example.administrator.thinker_soft.myfirstpro.lvadapter.AreaDataAdapter;
import com.example.administrator.thinker_soft.myfirstpro.lvadapter.BookDataAdapter;

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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MeterDataDownloadActivity extends Activity {
    private ImageView back;
    private ListView booklistView;
    private ListView arealistView;
    private BookDataAdapter bookAdapter;
    private AreaDataAdapter areaAdapter;
    private EditText begianNum;
    private EditText endNum;
    private Button downLoadBtn;
    private TextView selectBookNumber, selectAreaNumber, clear;
    private TextView bookSelectAll, bookReverse, bookSelectCancel, areaSelectAll, areaReverse, areaSelectCancel;
    private List<BookInfo> bookInfoList = new ArrayList<>();      //抄表本集合
    private List<AreaInfo> areaInfoList = new ArrayList<>();   //抄表分区集合
    private int bookNum = 0; // 记录抄表本选中的条目数量
    private int areaNum = 0; // 记录抄表分区选中的条目数量
    private HashMap<String, Integer> bookIDMap = new HashMap<String, Integer>();  //用于保存选中的抄表本ID
    private HashMap<String, Integer> areaIDMap = new HashMap<String, Integer>();  //用于保存选中的抄表分区ID
    private ArrayList<Integer> bookIDList = new ArrayList<>();  //用于保存抄表本ID值
    private ArrayList<Integer> areaIDLsit = new ArrayList<>();  //用于保存抄表分区ID值
    private SharedPreferences public_sharedPreferences, sharedPreferences_login;
    private String ip, port;  //接口ip地址   端口
    public int responseCode = 0;
    private String userResult;  //请求抄表用户数据结果
    private LayoutInflater layoutInflater;
    private PopupWindow loadingPopup, progressPopup;
    private ImageView frameAnimation;
    private AnimationDrawable animationDrawable;
    private LinearLayout rootLinearlayout;
    private View view;
    private TextView tips;  //加载进度的提示
    private String bookParams = null;  //抄表本参数
    private String areaParams = null;  //抄表分区参数
    private JSONObject userObject;     //用户object对象
    private SQLiteDatabase db;  //数据库

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_data_download);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件ID
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        clear = (TextView) findViewById(R.id.clear);
        selectBookNumber = (TextView) findViewById(R.id.select_book_number);
        selectAreaNumber = (TextView) findViewById(R.id.select_area_number);
        booklistView = (ListView) findViewById(R.id.meter_book_lv);
        arealistView = (ListView) findViewById(R.id.meter_area_lv);
        begianNum = (EditText) findViewById(R.id.begain_num);
        endNum = (EditText) findViewById(R.id.end_num);
        downLoadBtn = (Button) findViewById(R.id.downLoad_btn);
        bookSelectAll = (TextView) findViewById(R.id.book_select_all);
        bookReverse = (TextView) findViewById(R.id.book_reverse);
        bookSelectCancel = (TextView) findViewById(R.id.book_select_cancel);
        areaSelectAll = (TextView) findViewById(R.id.area_select_all);
        areaReverse = (TextView) findViewById(R.id.area_reverse);
        areaSelectCancel = (TextView) findViewById(R.id.area_select_cancel);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(MeterDataDownloadActivity.this, 1);
        db = helper.getWritableDatabase();
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        public_sharedPreferences = MeterDataDownloadActivity.this.getSharedPreferences("data", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle meterData = intent.getExtras();
            if (meterData != null) {
                Log.i("MeterDataDownload", "数据接收成功");
                ArrayList<BookInfo> bookInfoArrayList = meterData.getParcelableArrayList("bookInfoArrayList");
                ArrayList<AreaInfo> areaInfoArrayList = meterData.getParcelableArrayList("areaInfoArrayList");
                //初始化抄表本listview
                if (bookInfoArrayList != null) {
                    for (int i = 0; i < bookInfoArrayList.size(); i++) {
                        bookInfoList.add(bookInfoArrayList.get(i));
                    }
                    bookAdapter = new BookDataAdapter(MeterDataDownloadActivity.this, bookInfoList);
                    booklistView.setAdapter(bookAdapter);
                }
                //初始化抄表分区listview
                if (areaInfoArrayList != null) {
                    for (int i = 0; i < areaInfoArrayList.size(); i++) {
                        areaInfoList.add(areaInfoArrayList.get(i));
                    }
                    areaAdapter = new AreaDataAdapter(MeterDataDownloadActivity.this, areaInfoList);
                    arealistView.setAdapter(areaAdapter);
                }
            }
        }
    }

    //点击事件
    private void setViewClickListener() {
        back.setOnClickListener(onClickListener);
        clear.setOnClickListener(onClickListener);
        arealistView.setOnItemClickListener(new OnItemClickListener() {   //抄表分区item点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeterAreaViewHolder holder = (MeterAreaViewHolder) view.getTag();
                holder.checkedState.toggle();
                AreaDataAdapter.getIsCheck().put(position, holder.checkedState.isChecked());
                if (holder.checkedState.isChecked()) {
                    areaNum++;
                } else {
                    areaNum--;
                }
                //TextView显示
                selectAreaNumber.setText("(" + areaNum + ")");
            }
        });
        booklistView.setOnItemClickListener(new OnItemClickListener() {   //抄表本item点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeterBookViewHolder holder = (MeterBookViewHolder) view.getTag();
                holder.checkedState.toggle();
                BookDataAdapter.getIsCheck().put(position, holder.checkedState.isChecked());
                // 调整选定条目
                if (holder.checkedState.isChecked()) {
                    bookNum++;
                } else {
                    bookNum--;
                }
                //TextView显示
                selectBookNumber.setText("(" + bookNum + ")");
            }
        });
        bookSelectAll.setOnClickListener(onClickListener);
        bookReverse.setOnClickListener(onClickListener);
        bookSelectCancel.setOnClickListener(onClickListener);
        areaSelectAll.setOnClickListener(onClickListener);
        areaReverse.setOnClickListener(onClickListener);
        areaSelectCancel.setOnClickListener(onClickListener);
        downLoadBtn.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.clear:
                    db.delete("MeterUser", null, null);  //删除User表中当前用户的所有数据（官方推荐方法）
                    db.delete("MeterArea", null, null);
                    db.delete("MeterBook", null, null);
                    //设置id从1开始（sqlite默认id从1开始），若没有这一句，id将会延续删除之前的id
                    db.execSQL("update sqlite_sequence set seq=0 where name='MeterUser'");
                    db.execSQL("update sqlite_sequence set seq=0 where name='MeterArea'");
                    db.execSQL("update sqlite_sequence set seq=0 where name='MeterBook'");
                    Toast.makeText(MeterDataDownloadActivity.this, "清楚数据成功！", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.book_select_all:
                    bookSelectAll();
                    break;
                case R.id.book_reverse:
                    bookReverse();
                    break;
                case R.id.book_select_cancel:
                    bookSelectCancle();
                    break;
                case R.id.area_select_all:
                    areaSelectAll();
                    break;
                case R.id.area_reverse:
                    areaReverse();
                    break;
                case R.id.area_select_cancel:
                    areaSelectCancle();
                    break;
                case R.id.downLoad_btn:
                    showPopupwindow();  //数据加载
                    tips.setText("正在获取抄表数据...");
                    saveBookInfo();  //保存选中的抄表本ID信息
                    saveAreaInfo();  //保存选中的抄表分区ID信息
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                getMeterUserData();   //获取抄表用户数据
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            super.run();
                        }
                    }.start();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 获取抄表本参数
     */
    private void getBookParams() {
        for (int i = 0; i < bookIDList.size(); i++) {
            if (bookIDList.size() == 1) {  //两个编号不为空，抄表本选择了一条
                bookParams = bookIDMap.get("bookID" + bookIDList.get(0)) + "";
            } else {   //两个编号不为空，抄表本选择了多条
                if (i != (bookIDList.size() - 1)) {  //如果不是集合的最后一个元素后面就加逗号
                    bookParams += bookIDMap.get("bookID" + bookIDList.get(0)) + ",";
                } else {  //最后一个不加逗号
                    bookParams += bookIDMap.get("bookID" + bookIDList.get(0)) + "";
                }
            }
        }
    }

    /**
     * 获取抄表分区参数
     */
    private void getAreaParams() {
        for (int i = 0; i < areaIDLsit.size(); i++) {
            if (areaIDLsit.size() == 1) {  //两个编号不为空，抄表分区选择了一条
                areaParams = areaIDMap.get("areaID" + areaIDLsit.get(0)) + "";
            } else {  //两个编号不为空，抄表分区选择了多条
                if (i != (areaIDLsit.size() - 1)) {  //如果不是集合的最后一个元素后面就加逗号
                    areaParams += areaIDMap.get("areaID" + areaIDLsit.get(0)) + ",";
                } else { //最后一个不加逗号
                    areaParams += areaIDMap.get("areaID" + areaIDLsit.get(0)) + "";
                }
            }
        }
    }

    /**
     * 获取抄表用户数据
     */
    private void getMeterUserData() {
        if (!"".equals(begianNum.getText().toString().trim()) && !"".equals(endNum.getText().toString().trim())) { //首先判断两个编号都不为空
            if (bookIDMap.size() != 0 && areaIDMap.size() != 0) { //两个编号不为空，并且抄表本、抄表分区都已经选择
                //此时会下载三个部分的数据
                getBookParams();  //获取抄表本参数
                getAreaParams();  //获取抄表分区参数
                String params = "u.c_user_id between" + begianNum.getText().toString() +
                        " and " + endNum.getText().toString() + " and b.n_book_id in(" + bookParams +
                        ") and a.n_area_id in(" + areaParams + ")";
                try {
                    params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                    requireMeterUserData("findUserMeter.do", params);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {  //要么抄表本、抄表分区其中一个已经选择，要么两个都没选择
                if (bookIDMap.size() == 0 && areaIDMap.size() == 0) {//两个编号不为空，但是抄表本、抄表分区都未选择
                    //此时只会下载 编号范围 部分的数据
                    String params = "u.c_user_id between" + begianNum.getText().toString() +
                            " and " + endNum.getText().toString();
                    try {
                        params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                        requireMeterUserData("findUserMeter.do", params);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else if (bookIDMap.size() == 0) { //两个编号不为空，抄表分区已经选择，但是抄表本未选择
                    //此时会下载 编号范围、抄表分区 两个部分的数据
                    getAreaParams();  //获取抄表分区参数
                    String params = "u.c_user_id between" + begianNum.getText().toString() +
                            " and " + endNum.getText().toString() +
                            " and a.n_area_id in(" + areaParams + ")";
                    try {
                        params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                        requireMeterUserData("findUserMeter.do", params);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else if (areaIDMap.size() == 0) { //两个编号不为空，抄表本已经选择，但是抄表分区未选择
                    //此时会下载 编号范围、抄表本 两个部分的数据
                    getBookParams();  //获取抄表本参数
                    String params = "u.c_user_id between" + begianNum.getText().toString() +
                            " and " + endNum.getText().toString() + " and b.n_book_id in(" + bookParams + ")";
                    try {
                        params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                        requireMeterUserData("findUserMeter.do", params);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {  //要么两个编号都为空，要么其中一个为空
            if ("".equals(begianNum.getText().toString().trim()) && "".equals(endNum.getText().toString().trim())) { //两个编号都为空
                if (bookIDMap.size() != 0 && areaIDMap.size() != 0) {  //抄表本、抄表分区都已经选择
                    //此时会下载 抄表本、抄表分区 两个部分的数据
                    getBookParams();  //获取抄表本参数
                    getAreaParams();  //获取抄表分区参数
                    String params = "b.n_book_id in(" + bookParams + ") and a.n_area_id in(" + areaParams + ")";
                    try {
                        params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                        requireMeterUserData("findUserMeter.do", params);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (bookIDMap.size() == 0 && areaIDMap.size() == 0) { //两个编号都为空，并且抄表本、抄表分区都未选择
                        //此时提示选择其中之一
                        Toast.makeText(MeterDataDownloadActivity.this, "至少选择其中一个区域才能下载哦", Toast.LENGTH_SHORT).show();
                    } else if (bookIDMap.size() == 0) { //两个编号都为空，抄表分区已经选择，但是抄表本未选择
                        //此时只会下载 抄表分区 部分的数据
                        getAreaParams();  //获取抄表分区参数
                        String params = "a.n_area_id in(" + areaParams + ")";
                        try {
                            params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                            requireMeterUserData("findUserMeter.do", params);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else if (areaIDMap.size() == 0) { //两个编号都为空，抄表本已经选择，但是抄表分区未选择
                        //此时只会下载 抄表本 部分的数据
                        getBookParams();  //获取抄表本参数
                        String params = "b.n_book_id in(" + bookParams + ")";
                        try {
                            params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                            requireMeterUserData("findUserMeter.do", params);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else { //其中一个编号为空
                Toast.makeText(MeterDataDownloadActivity.this, "请您将区间填写完整！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //show加载动画
    public void showPopupwindow() {
        layoutInflater = LayoutInflater.from(MeterDataDownloadActivity.this);
        view = layoutInflater.inflate(R.layout.popupwindow_query_loading, null);
        loadingPopup = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameAnimation = (ImageView) view.findViewById(R.id.frame_animation);
        tips = (TextView) view.findViewById(R.id.tips);
        loadingPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        loadingPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        loadingPopup.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        loadingPopup.setAnimationStyle(R.style.camera);
        loadingPopup.update();
        loadingPopup.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        startFrameAnimation();
        loadingPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
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
        WindowManager.LayoutParams lp = MeterDataDownloadActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            MeterDataDownloadActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            MeterDataDownloadActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        MeterDataDownloadActivity.this.getWindow().setAttributes(lp);
    }

    //请求抄表用户数据
    private void requireMeterUserData(final String method, final String keyAndValue) {
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
                url = new URL(httpUrl + "?param1=" + keyAndValue);
                //没有参数传递
            } else {
                url = new URL(httpUrl);
            }
            Log.i("MeterDataDownloadAct", url + "");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(6000);
            httpURLConnection.setReadTimeout(6000);
            httpURLConnection.connect();
            //传回的数据解析成String
            Log.i("MeterDataDownloadAct>", "responseCode=" + httpURLConnection.getResponseCode());
            if ((responseCode = httpURLConnection.getResponseCode()) == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    stringBuilder.append(str);
                }
                userResult = stringBuilder.toString();
                Log.i("MeterDataDownloadAct", userResult);
                JSONArray jsonArray = new JSONArray(userResult);
                if (jsonArray.length() != 0) {
                    handler.sendEmptyMessage(0);
                } else {
                    handler.sendEmptyMessage(1);
                }
            } else {
                handler.sendEmptyMessage(2);
            }
        } catch (UnsupportedEncodingException | MalformedURLException | JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("IOException==========>", "网络请求异常!");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    try {
                        JSONArray jsonArray = new JSONArray(userResult);
                        tips.setText("正在下载" + jsonArray.length() + "条数据，请稍后...");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            userObject = jsonArray.getJSONObject(i);
                            insertMeterUserData();   //将抄表用户数据插入本地数据库
                        }
                        insertMeterBookData();   //将抄表本数据保存至本地 MeterBook 表
                        insertMeterAreaData();   //将抄表分区数据保存至本地 MeterArea 表
                        Toast.makeText(MeterDataDownloadActivity.this, "数据下载成功！", Toast.LENGTH_SHORT).show();
                        loadingPopup.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    loadingPopup.dismiss();
                    Toast.makeText(MeterDataDownloadActivity.this, "没有抄表用户数据哦！", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    loadingPopup.dismiss();
                    Toast.makeText(MeterDataDownloadActivity.this, "网络请求超时！", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 将抄表用户数据保存至本地 MeterUser 表
     */
    private void insertMeterUserData() {
        ContentValues values = new ContentValues();
        values.put("meter_reader_id", userObject.optInt("n_user_meter_reader_id", 0) + "");    //抄表员ID
        values.put("user_amount", userObject.optInt("n_amount", 0) + "");                      //用户余额
        values.put("meter_degrees", userObject.optInt("n_meter_degrees", 0) + "");             //上月读数
        values.put("meter_number", userObject.optString("c_meter_number", ""));                //表编号
        values.put("arrearage_months", userObject.optInt("cnt", 0) + "");                        //欠费月数
        values.put("mix_state", userObject.optInt("n_mix_state", 0) + "");                     //混合使用状态（0正常  1混合）
        values.put("meter_order_number", userObject.optInt("n_order_num1", 0) + "");           //抄表序号
        values.put("arrearage_amount", userObject.optInt("arrearageAmount", 0) + "");          //欠费金额
        values.put("area_id", userObject.optInt("n_area_id", 0) + "");                         //抄表本分区ID
        values.put("area_name", userObject.optString("c_area_name", ""));                      //抄表本分区名称
        values.put("user_name", userObject.optString("c_user_name", ""));                      //用户名
        values.put("last_month_dosage", userObject.optInt("n_dosage", 0) + "");                  //上月用量
        values.put("property_id", userObject.optInt("n_properties_id", 0) + "");                 //性质ID
        values.put("property_name", userObject.optString("c_properties_name", ""));            //性质名称
        values.put("user_id", userObject.optString("c_user_id", ""));                          //用户ID
        values.put("book_id", userObject.optInt("n_book_id", 0) + "");                           //抄表本ID
        values.put("float_range", userObject.optString("floatOver", ""));                      //浮动范围
        values.put("dosage_change", userObject.optInt("n_change_meter", 0) + "");                //更换量
        values.put("user_address", userObject.optString("c_user_address", ""));                //用户地址
        values.put("start_dosage", userObject.optInt("n_minimum", 0) + "");                      //启用量
        values.put("old_user_id", userObject.optString("c_old_user_id", ""));                  //用户老编号
        values.put("book_name", userObject.optString("c_book_name", ""));                      //抄表本名称
        values.put("meter_model", userObject.optString("c_model_name", ""));                   //表型号
        values.put("rubbish_cost", userObject.optInt("rubbishCost", 0) + "");                    //垃圾费
        values.put("remission", userObject.optInt("n_remission", 0) + "");                       //加减量
        //下面这五个字段待定
        values.put("this_month_dosage", "");
        values.put("this_month_end_degree", "");
        values.put("n_jw_x", "");
        values.put("n_jw_y", "");
        values.put("d_jw_time", "");
        db.insert("MeterUser", null, values);
    }

    /**
     * 将抄表本数据保存至本地 MeterBook 表
     */
    private void insertMeterBookData() {
        ContentValues values = new ContentValues();
        values.put("bookName", userObject.optString("c_area_name", ""));                 //抄表本名称
        values.put("bookId", userObject.optInt("n_book_id", 0) + "");                      //抄表本ID
        values.put("userId", sharedPreferences_login.getString("userId", ""));             //当前登录人的ID
        values.put("userName", sharedPreferences_login.getString("user_name", ""));       //当前登录人的名称
        db.insert("MeterBook", null, values);
    }

    /**
     * 将抄表分区数据保存至本地 MeterArea 表
     */
    private void insertMeterAreaData() {
        ContentValues values = new ContentValues();
        values.put("areaName", userObject.optString("c_book_name", ""));                 //抄表分区名称
        values.put("areaId", userObject.optInt("n_area_id", 0) + "");                    //抄表分区ID
        values.put("userId", sharedPreferences_login.getString("userId", ""));             //当前登录人的ID
        values.put("userName", sharedPreferences_login.getString("user_name", ""));       //当前登录人的名称
        db.insert("MeterArea", null, values);
    }

    // 刷新抄表本listview和TextView的显示
    private void bookDataChanged() {
        // 通知listView刷新
        bookAdapter.notifyDataSetChanged();
        // TextView显示最新的选中数目
        selectBookNumber.setText("(" + bookNum + ")");
    }

    // 刷新抄表分区listview和TextView的显示
    private void areaDataChanged() {
        // 通知listView刷新
        areaAdapter.notifyDataSetChanged();
        // TextView显示最新的选中数目
        selectAreaNumber.setText("(" + areaNum + ")");
    }

    //抄表本全选
    public void bookSelectAll() {
        for (int i = 0; i < bookInfoList.size(); i++) {
            BookDataAdapter.getIsCheck().put(i, true);
        }
        bookNum = bookInfoList.size();
        bookDataChanged();
    }

    //抄表本反选
    public void bookReverse() {
        for (int i = 0; i < bookInfoList.size(); i++) {
            if (BookDataAdapter.getIsCheck().get(i)) {
                BookDataAdapter.getIsCheck().put(i, false);
                bookNum--;//数量减一
            } else {
                BookDataAdapter.getIsCheck().put(i, true);
                bookNum++;//数量加一
            }
        }
        bookDataChanged();
    }

    //抄表本取消选择
    public void bookSelectCancle() {
        for (int i = 0; i < bookInfoList.size(); i++) {
            if (BookDataAdapter.getIsCheck().get(i)) {
                BookDataAdapter.getIsCheck().put(i, false);
                bookNum--; //数量减一
            }
        }
        bookDataChanged();
    }

    //抄表分区全选
    public void areaSelectAll() {
        for (int i = 0; i < areaInfoList.size(); i++) {
            AreaDataAdapter.getIsCheck().put(i, true);
        }
        areaNum = areaInfoList.size();
        areaDataChanged();
    }

    //抄表分区反选
    public void areaReverse() {
        for (int i = 0; i < areaInfoList.size(); i++) {
            if (AreaDataAdapter.getIsCheck().get(i)) {
                AreaDataAdapter.getIsCheck().put(i, false);
                areaNum--;
            } else {
                AreaDataAdapter.getIsCheck().put(i, true);
                areaNum++;
            }
        }
        areaDataChanged();
    }

    //抄表分区取消选择
    public void areaSelectCancle() {
        for (int i = 0; i < areaInfoList.size(); i++) {
            if (AreaDataAdapter.getIsCheck().get(i)) {
                AreaDataAdapter.getIsCheck().put(i, false);
                areaNum--;
            }
        }
        areaDataChanged();
    }

    //保存选中的抄表本ID信息
    public void saveBookInfo() {
        bookIDMap.clear();
        bookIDList.clear();
        for (int i = 0; i < bookInfoList.size(); i++) {
            if (BookDataAdapter.getIsCheck().get(i)) {
                BookInfo bookInfo = bookInfoList.get((int) bookAdapter.getItemId(i));
                bookIDMap.put("bookID" + i, Integer.parseInt(bookInfo.getID()));
                Log.i("bookID=========>", "这次被勾选第" + i + "个，抄表本ID为：" + bookInfo.getID());
                bookIDList.add(i);
            }
        }
    }

    //保存选中的抄表分区ID信息
    public void saveAreaInfo() {
        areaIDMap.clear();
        areaIDLsit.clear();
        for (int i = 0; i < areaInfoList.size(); i++) {
            if (AreaDataAdapter.getIsCheck().get(i)) {
                AreaInfo areaInfo = areaInfoList.get((int) areaAdapter.getItemId(i));
                areaIDMap.put("areaID" + i, Integer.parseInt(areaInfo.getID()));
                Log.i("areaID=========>", "这次被勾选第" + i + "个，抄表分区ID为：" + areaInfo.getID());
                areaIDLsit.add(i);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        if (loadingPopup.isShowing()) {
            loadingPopup.dismiss();
        }
    }
}
