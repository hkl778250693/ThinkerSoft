package com.example.administrator.thinker_soft.android_cbjactivity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.android_cbjactivity.adapter.MobileMeterViewPagerAdapter;
import com.example.administrator.thinker_soft.android_cbjactivity.fragment.CustomQueryFragment;
import com.example.administrator.thinker_soft.android_cbjactivity.fragment.MeterDataTransferFragment;
import com.example.administrator.thinker_soft.android_cbjactivity.fragment.MeterHomePageFragment;
import com.example.administrator.thinker_soft.android_cbjactivity.fragment.ScanCodeMeterFragment;
import com.example.administrator.thinker_soft.myfirstpro.appcation.MyApplication;
import com.example.administrator.thinker_soft.myfirstpro.entity.AreaInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.BookInfo;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.service.LocationService;
import com.example.administrator.thinker_soft.myfirstpro.threadsocket.SocketInteraction;
import com.example.administrator.thinker_soft.myfirstpro.util.AssembleUpmes;
import com.example.administrator.thinker_soft.myfirstpro.util.JaugeInternetState;
import com.example.administrator.thinker_soft.myfirstpro.util.JsonAnalyze;
import com.example.administrator.thinker_soft.myfirstpro.util.MyDialog;
import com.example.administrator.thinker_soft.myfirstpro.util.Mytoast;
import com.example.administrator.thinker_soft.viewbadger.BadgeView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CBJActivity extends FragmentActivity{
    private static final String LTAG = CBJActivity.class.getSimpleName();
    private TextView mapInfo;
    private SDKReceiver mReceiver;
    private RelativeLayout rootRelative;
    private ViewPager viewPager;
    private TextView titleName;
    private PopupWindow popupWindow;
    private ImageView back,more;
    private List<Fragment> fragmentList;
    private MobileMeterViewPagerAdapter adapter;
    private TabWidget mTabWidget;
    private RadioButton radio_button0, radio_button1, radio_button2, radio_button3;
    private List<BookInfo> bookList;
    private List<AreaInfo> areaList;
    private String DBName;
    private String filepath;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private LocationService mService;
    private boolean mBound = false;
    private MyApplication myapp;
    private String ip = "";
    private String port = "";
    private int clickCount;
    private Map<Integer, Boolean> dialogControl;
    private Dialog downDialog;
    private String operName = "NANBU";
    private String requestWorkJson;
    private List<Map<String, String>> ALLworkList_before = null;
    private String SystemUserId;
    private BadgeView badgeViewone;
    private BadgeView badgeViewTwo;
    private MyHandler dataHandler = new MyHandler();
    private MyActivityManager mam = MyActivityManager.getInstance();
    private boolean popbool;
    private boolean navsignal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbj);

        bindView();
        setViewPager();
        defaultSetting();
        setViewClickListener();
    }

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            Log.i(LTAG, "action: " + s);
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                mapInfo.setText("key 验证出错! 错误码 :" + intent.getIntExtra(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, 0)
                        +  " ; 请在 AndroidManifest.xml 文件中检查 key 设置");
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                mapInfo.setText("key 验证成功! 功能可以正常使用");
                mapInfo.setTextColor(Color.YELLOW);
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                mapInfo.setText("网络出错");
            }
        }
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        titleName = (TextView) findViewById(R.id.title_name);
        more = (ImageView) findViewById(R.id.more);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        radio_button0 = (RadioButton) findViewById(R.id.radio_button0);
        radio_button1 = (RadioButton) findViewById(R.id.radio_button1);
        radio_button2 = (RadioButton) findViewById(R.id.radio_button2);
        radio_button3 = (RadioButton) findViewById(R.id.radio_button3);
        rootRelative = (RelativeLayout) findViewById(R.id.root_relative);
    }

    //初始化设置
    private void defaultSetting() {
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        SystemUserId = sharedPreferences.getString("SystemUserId", "");
        sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
        editor = sharedPreferences.edit();
        DBName = sharedPreferences.getString("dbName", "");
        radio_button0.setChecked(true);
        titleName.setText("移动抄表");
    }

    //设置viewPager
    private void setViewPager() {
        fragmentList = new ArrayList<>();
        //添加fragment到list
        fragmentList.add(new MeterHomePageFragment());
        fragmentList.add(new ScanCodeMeterFragment());
        fragmentList.add(new CustomQueryFragment());
        fragmentList.add(new MeterDataTransferFragment());
        //避免报空指针
        if (fragmentList != null) {
            adapter = new MobileMeterViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        }
        viewPager.setAdapter(adapter);
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(onClickListener);
        more.setOnClickListener(onClickListener);
        radio_button0.setOnClickListener(onClickListener);
        radio_button1.setOnClickListener(onClickListener);
        radio_button2.setOnClickListener(onClickListener);
        radio_button3.setOnClickListener(onClickListener);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        titleName.setText("移动抄表");
                        radio_button0.setChecked(true);
                        radio_button1.setChecked(false);
                        radio_button2.setChecked(false);
                        radio_button3.setChecked(false);
                        break;
                    case 1:
                        titleName.setText("扫码抄表");
                        radio_button0.setChecked(false);
                        radio_button1.setChecked(true);
                        radio_button2.setChecked(false);
                        radio_button3.setChecked(false);
                        break;
                    case 2:
                        titleName.setText("自定义查询");
                        radio_button0.setChecked(false);
                        radio_button1.setChecked(false);
                        radio_button2.setChecked(true);
                        radio_button3.setChecked(false);
                        break;
                    case 3:
                        titleName.setText("数据传输");
                        radio_button0.setChecked(false);
                        radio_button1.setChecked(false);
                        radio_button2.setChecked(false);
                        radio_button3.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.more:
                    AddPopWindowenu();
                    break;
                case R.id.radio_button0:
                    titleName.setText("移动抄表");
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.radio_button1:
                    titleName.setText("扫码抄表");
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.radio_button2:
                    titleName.setText("自定义查询");
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.radio_button3:
                    titleName.setText("数据传输");
                    viewPager.setCurrentItem(3);
                    break;
            }
        }
    };

    private void AddPopWindowenu() {
        Log.i("CBJActivity","为空弹窗点击事件进来了");
        View contentView = getLayoutInflater().inflate(R.layout.popwindow_content, null);
        popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.home_page_more_shape));
        mapInfo = (TextView) contentView.findViewById(R.id.map_info);
        TextView sysSet = (TextView) contentView.findViewById(R.id.popwindow_content_sysSet);
        TextView gpscollector = (TextView) contentView.findViewById(R.id.popwindow_content_gpscollector);
        TextView tesseractOcr = (TextView) contentView.findViewById(R.id.tesseract_ocr);
        TextView mapMeter = (TextView) contentView.findViewById(R.id.popwindow_content_mapMeter);
        TextView tasks = (TextView) contentView.findViewById(R.id.popwindow_content_actualtask);
        /*// 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        LocalBroadcastManager.getInstance(CBJActivity.this).registerReceiver(mReceiver, iFilter);*/

        sysSet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CBJActivity.this, SheZiActivity.class);
                startActivityForResult(intent, 1);
                popupWindow.dismiss();
            }
        });
        gpscollector.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DBName = sharedPreferences.getString("dbName", "");
                if (DBName != null && !"".equals(DBName)) {
                    Intent intent = new Intent(CBJActivity.this, GPSCollectorActivity.class);
                    startActivity(intent);
                    popupWindow.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "��ѡ�񳭱�", Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                }
            }
        });
        tasks.setOnClickListener(new OnClickListener() {// ��������

            @Override
            public void onClick(View v) {
                if (myapp.getNewWorkList() == null || myapp.getNewWorkList().size() == 0 || badgeViewTwo.isShown()) {
                    requestWork();
                } else if (myapp.getNewWorkList() != null && myapp.getNewWorkList().size() != 0 && !badgeViewTwo.isShown()) {
                    popupWindow.dismiss();
                    Intent intent = new Intent(CBJActivity.this, ActualMissionNEW.class);
                    intent.setAction("");
                    startActivity(intent);
                }
            }
        });
        tesseractOcr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CBJActivity.this, TesseractOcrActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        mapMeter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CBJActivity.this, MapMeterActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            popupWindow.showAsDropDown(rootRelative, 0, 0, Gravity.END);
        }
        backgroundAlpha(0.6F);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = CBJActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            CBJActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            CBJActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        CBJActivity.this.getWindow().setAttributes(lp);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消监听 SDK 广播
        LocalBroadcastManager.getInstance(CBJActivity.this).unregisterReceiver(mReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v("onSaveInstanceState", "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        Log.v("onRestoreInstanceState", "onSaveInstanceState");
    }

    public void requestWork() {
        popupWindow.dismiss();
        if (!JaugeInternetState.isNetworkAvailable(getApplicationContext())) {
            Toast.makeText(this, "WIFI", Toast.LENGTH_SHORT).show();
            return;
        } else {
            SharedPreferences sharedPreferences = getApplication()
                    .getSharedPreferences("IP_PORT_DBNAME", 0);
            if (sharedPreferences.contains("ip")) {
                ip = sharedPreferences.getString("ip", "");
            }
            if (sharedPreferences.contains("port")) {
                port = sharedPreferences.getString("port", "");
            }
            clickCount++;
            dialogControl.put(clickCount, true);
            Log.e("xxq", "MyDialog--------------");
            downDialog = MyDialog.createLoadingDialog(CBJActivity.this,
                    "������������...");
            downDialog.setCancelable(true);
            downDialog.setCanceledOnTouchOutside(false);
            downDialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    dialogControl.put(clickCount, false);
                }
            });

            if (downDialog != null)
                downDialog.show();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    int loc = clickCount;
                    boolean singal = false;// �ж���;�Ƿ����
                    String parameter = AssembleUpmes
                            .reqtTasksParameter(SystemUserId);
                    Log.e("xxq", "parameter=" + parameter);
                    SocketInteraction areaSocket = new SocketInteraction(
                            getApplicationContext(), Integer.parseInt(port),
                            ip, operName, parameter, dataHandler);
                    singal = areaSocket.DataDownLoadConn();// ����
                    areaSocket.closeConn();// �Ͽ�����
                    if (dialogControl.size() > 0) {
                        if (dialogControl.get(loc) == true) {
                            if (singal == true) {// �ɹ�
                                dataHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        downDialog.dismiss();
                                    }
                                });
                            } else {// ʧ��
                                dataHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        downDialog.dismiss();
                                        Mytoast.showToast(CBJActivity.this, "������δ��Ӧ������֤IP�Ͷ˿��Ƿ�����", 1000);
                                    }
                                });
                            }
                        }
                    }
                }
            }.start();
        }
    }

    class MyHandler extends Handler {
        /**
         * ��ȡsocket�߳�����
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int loc = clickCount;// ��ǰ�̵߳����
            Bundle bundle = msg.getData();
            int key = bundle.getInt("key");
            if (key == 1) {
                if (badgeViewone != null) {
                    badgeViewone.show();
                }
                return;
            }
            String data = bundle.getString("data");
            if (data != null && "û������".equals(data)) {
                myapp.setNewWorkList(null);
                Intent intent = new Intent(CBJActivity.this,
                        ActualMissionNEW.class);
                if (badgeViewone.isShown() == true || badgeViewTwo.isShown() == true) {
                    intent.setAction("show");
                } else {
                    intent.setAction("");
                }
                startActivity(intent);
                return;
            }
            if (dialogControl.size() > 0) {
                Log.v("ZHOUTAO", "��ǰ�߳���ţ�" + loc);
                if (dialogControl.get(loc) == true) {
                    requestWorkJson = JsonAnalyze.analyzeData(data);// ����������Ľ��
                    // Log.v("xxq", "login_json=" + login_json);
                    // TODO
                    if ("�û�������".equals(requestWorkJson)
                            || "�������".equals(requestWorkJson)) {
                        Mytoast.showToast(CBJActivity.this,
                                requestWorkJson + "", 1000);
                    } else {
                        try {
                            ALLworkList_before = JsonAnalyze
                                    .jsonTaskData(requestWorkJson);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ;
                        myapp.setNewWorkList(ALLworkList_before);
                        // ȡ�����ݳɹ�����ֵ����һ��Activity
                        Intent intent = new Intent(CBJActivity.this,
                                ActualMissionNEW.class);
                        if (badgeViewone.isShown() == true || badgeViewTwo.isShown() == true) {
                            intent.setAction("show");
                        } else {
                            intent.setAction("");
                        }
                        // intent.putExtra("ALLworkList_before",
                        // (Serializable) ALLworkList_before);
                        startActivity(intent);
                        if (badgeViewone != null) {
                            badgeViewone.hide();
                        }
                        if (badgeViewTwo != null) {
                            badgeViewTwo.hide();
                        }
                    }

                }
            }
        }
    }

}
