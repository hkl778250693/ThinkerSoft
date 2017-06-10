package com.example.administrator.thinker_soft.android_cbjactivity;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.appcation.MyApplication;
import com.example.administrator.thinker_soft.myfirstpro.entity.AreaInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.BookInfo;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.service.LocationService;
import com.example.administrator.thinker_soft.myfirstpro.service.LocationService.LocationBinder;
import com.example.administrator.thinker_soft.myfirstpro.threadsocket.SocketInteraction;
import com.example.administrator.thinker_soft.myfirstpro.util.AnimationTabHost;
import com.example.administrator.thinker_soft.myfirstpro.util.AssembleUpmes;
import com.example.administrator.thinker_soft.myfirstpro.util.JaugeInternetState;
import com.example.administrator.thinker_soft.myfirstpro.util.JsonAnalyze;
import com.example.administrator.thinker_soft.myfirstpro.util.MyDialog;
import com.example.administrator.thinker_soft.myfirstpro.util.Mytoast;
import com.example.administrator.thinker_soft.viewbadger.BadgeView;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class CBJActivity extends TabActivity implements OnTabChangeListener {
    private LinearLayout systemSet,rootLinearlayout;
    private TextView systemSet_badget_tv;
    private TextView homepageTitle;
    private PopupWindow popupWindow;
    private ImageView back;
    private EditText et;
    private int POPWINDOWWIDTH;
    private GestureDetector gestureDetector;
    private AnimationTabHost mTabHost;
    @SuppressWarnings("unused")
    private TabWidget mTabWidget;
    private RadioButton radio_button0, radio_button1, radio_button2, radio_button3;
    private List<BookInfo> bookList;
    private List<AreaInfo> areaList;
    private String DBName;
    private String filepath;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    //private BDLocationModel bdLocationModel;
    private int currentTabID = 0;
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
    private BroadcastReceiver receiver;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            LocationBinder binder = (LocationBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
        public void engineInitSuccess() {
            // mIsEngineInitSuccess = true;
            Log.i("engineInitSuccess", "�ɹ���");
        }
        public void engineInitStart() {
            Log.i("engineInitStart", "��ʼ��");
        }

        public void engineInitFail() {
            Log.i("engineInitFail", "ʧ�ܣ�");
        }
    };

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return "";
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbj);
        /*String sdpath = getSdcardDir();
		if ("".equals(sdpath)) {
			navsignal = false;
		} else {
			navsignal = true;
			BaiduNaviManager.getInstance().initEngine(this, sdpath,
					this.mNaviEngineInitListener, "1RUeGi5DkoqZVZYgv0DT9M1x",
					null);
		}
		MyApplication.bdLocationModel.startLoaction();
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				System.out.println("�Ѿ����յ�������Ϣ:"+intent.getAction());
				System.out.println("�Ѿ����յ�������Ϣ:"+intent.getStringExtra("json"));
				System.out.println("�Ѿ����յ�������Ϣ");
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putInt("key", 1);
				msg.setData(data);
				dataHandler.sendMessage(msg);
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction("CBJActivityBroadcastReceiver");
		registerReceiver(receiver, filter);
		mam.pushOneActivity(this);

		Log.v("onCreate() ", "onCreate()");
		myapp = (MyApplication) getApplication();
		dialogControl = new HashMap<Integer, Boolean>();
		WindowManager manager = getWindowManager();
		Display display = manager.getDefaultDisplay();
		POPWINDOWWIDTH = display.getWidth();
		display.getHeight();
		
		filepath = Environment.getDataDirectory().getPath() + "/data/"
				+ "com.example.android_cbjactivity" + "/databases/";
		Intent intent = new Intent(this, LocationService.class);
		// TODO
		System.out.println("�����Ƿ������¹���"
				+ bindService(intent, mConnection,
						Context.BIND_AUTO_CREATE));*/
        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        homepageTitle = (TextView) findViewById(R.id.homepageTitle);
        systemSet_badget_tv = (TextView) findViewById(R.id.systemSet_badget_tv);
        systemSet = (LinearLayout) findViewById(R.id.systemSet);
        radio_button0 = (RadioButton) findViewById(R.id.radio_button0);
        radio_button1 = (RadioButton) findViewById(R.id.radio_button1);
        radio_button2 = (RadioButton) findViewById(R.id.radio_button2);
        radio_button3 = (RadioButton) findViewById(R.id.radio_button3);
        mTabHost = (AnimationTabHost) findViewById(android.R.id.tabhost);
        mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
    }

    //初始化设置
    private void defaultSetting() {
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        SystemUserId = sharedPreferences.getString("SystemUserId", "");
        sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
        editor = sharedPreferences.edit();
        DBName = sharedPreferences.getString("dbName", "");
        homepageTitle.setText("移动抄表");
        badgeViewone = new BadgeView(this, systemSet_badget_tv);
        badgeViewone.setText("");
        badgeViewone.setTextSize(9);
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(onClickListener);
        radio_button0.setOnClickListener(onClickListener);
        radio_button1.setOnClickListener(onClickListener);
        radio_button2.setOnClickListener(onClickListener);
        radio_button3.setOnClickListener(onClickListener);
        systemSet.setOnClickListener(onClickListener);
        mTabHost.setOnTabChangedListener(this);
        mTabHost.addTab(mTabHost.newTabSpec("ONE").setIndicator("ONE").setContent(new Intent(CBJActivity.this, ChaoBiaoActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("TWO").setIndicator("TWO").setContent(new Intent(CBJActivity.this, CaptureActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("THREE").setIndicator("THREE").setContent(new Intent(CBJActivity.this, ChaXunActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("FOUR").setIndicator("FOUR").setContent(new Intent(CBJActivity.this, SJCSActivity.class)));
        mTabHost.setOpenAnimation(true);
        gestureDetector = new GestureDetector(new TabHostTouch());
        new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.systemSet:
                    AddPopWindowenu();
                    Log.i("CBJActivity","弹窗点击事件执行");
                    break;
                case R.id.radio_button0:
                    homepageTitle.setText("移动抄表");
                    mTabHost.setCurrentTabByTag("ONE");
                    radio_button0.setChecked(true);
                    radio_button1.setChecked(false);
                    radio_button2.setChecked(false);
                    radio_button3.setChecked(false);
                    break;
                case R.id.radio_button1:
                    homepageTitle.setText("扫码抄表");
                    mTabHost.setCurrentTabByTag("TWO");
                    radio_button0.setChecked(false);
                    radio_button1.setChecked(true);
                    radio_button2.setChecked(false);
                    radio_button3.setChecked(false);
                    break;
                case R.id.radio_button2:
                    homepageTitle.setText("自定义查询");
                    mTabHost.setCurrentTabByTag("THREE");
                    radio_button0.setChecked(false);
                    radio_button1.setChecked(false);
                    radio_button2.setChecked(true);
                    radio_button3.setChecked(false);
                    break;
                case R.id.radio_button3:
                    homepageTitle.setText("数据传输");
                    mTabHost.setCurrentTabByTag("FOUR");
                    radio_button0.setChecked(false);
                    radio_button1.setChecked(false);
                    radio_button2.setChecked(false);
                    radio_button3.setChecked(true);
                    break;
            }
        }
    };

    private void AddPopWindowenu() {
        if (popupWindow == null) {
            Log.i("CBJActivity","为空弹窗点击事件进来了");
            View contentView = getLayoutInflater().inflate(R.layout.popwindow_content, null);
            popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.home_page_more_shape));
            TextView sysSet = (TextView) contentView.findViewById(R.id.popwindow_content_sysSet);
            TextView gpscollector = (TextView) contentView.findViewById(R.id.popwindow_content_gpscollector);
            TextView mapMeter = (TextView) contentView.findViewById(R.id.popwindow_content_mapMeter);
            TextView tasks = (TextView) contentView.findViewById(R.id.popwindow_content_actualtask);
            LinearLayout popwindow_content_actualtask_ll = (LinearLayout) contentView.findViewById(R.id.popwindow_content_actualtask_ll);
            badgeViewTwo = new BadgeView(CBJActivity.this, popwindow_content_actualtask_ll);
            badgeViewTwo.setText("����");
            badgeViewTwo.setTextSize(9);
            if (badgeViewone != null && badgeViewone.isShown()) {
                badgeViewTwo.show();
            }
            View logout = contentView.findViewById(R.id.popwindow_content_logout);
            sysSet.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
					/*
					 * Typeface typeface =
					 * Typeface.create(Typeface.DEFAULT,Typeface.BOLD);
					 * ((TextView) v).setTypeface(typeface); Typeface
					 * mtypeface =
					 * Typeface.create(Typeface.DEFAULT,Typeface
					 * .NORMAL); mapMeter.setTypeface(mtypeface);
					 */
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
            mapMeter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBName = sharedPreferences.getString("dbName", "");
                    if (DBName != null && !"".equals(DBName)) {
                        Intent intent = new Intent(CBJActivity.this, MapMeterActivity.class);
                        startActivity(intent);
                        popupWindow.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "��ѡ�񳭱�", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                    }
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                popupWindow.showAsDropDown(rootLinearlayout, 0, 0, Gravity.END);
            }
        } else {
            Log.i("CBJActivity","不为空弹窗点击事件进来了");
            if (badgeViewone != null && badgeViewone.isShown()) {
                if (badgeViewTwo != null) {
                    badgeViewTwo.show();
                }
            }
            if (!popupWindow.isShowing()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    popupWindow.showAsDropDown(rootLinearlayout, 0, 0, Gravity.END);
                }
            } else {
                popupWindow.dismiss();
            }
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
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            event.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(event);
    }

    /*	@Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                if(event.getAction()==KeyEvent.ACTION_DOWN){
                       // ��������������������
                    if(popbool==false){
                        popbool = true;
                        AddPopWindowenu();
                    }else if(popbool==true){
                        popbool = false;
                        if(popupWindow!=null)
                            popupWindow.dismiss();
                    }
                }
             }
            return false;
        }*/
    @Override
    protected void onResume() {
        super.onResume();
        Log.v("onResume()", "onResume()");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v("onRestart() ", "onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("onStart() ", "onStart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
		/*unregisterReceiver(receiver);*/
        JaugeInternetState.closeGPSSettings(getApplicationContext());
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
/*
		MyApplication.bdLocationModel.stopLocation();
*/
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

    @Override
    public void openOptionsMenu() {
        super.openOptionsMenu();

    }

    @Override
    public void closeOptionsMenu() {
        super.closeOptionsMenu();
    }

    @Override
    public void onTabChanged(String tabId) {
        if ("ONE".equals(tabId)) {
            mTabHost.setCurrentTabByTag("ONE");
            radio_button0.setChecked(true);
            radio_button1.setChecked(false);
            radio_button2.setChecked(false);
            radio_button3.setChecked(false);
        } else if ("TWO".equals(tabId)) {
            mTabHost.setCurrentTabByTag("TWO");
            radio_button0.setChecked(false);
            radio_button1.setChecked(true);
            radio_button2.setChecked(false);
            radio_button3.setChecked(false);
        } else if ("THREE".equals(tabId)) {
            mTabHost.setCurrentTabByTag("THREE");
            radio_button0.setChecked(false);
            radio_button1.setChecked(false);
            radio_button2.setChecked(true);
            radio_button3.setChecked(false);
        } else if ("FOUR".equals(tabId)) {
            mTabHost.setCurrentTabByTag("FOUR");
            radio_button0.setChecked(false);
            radio_button1.setChecked(false);
            radio_button2.setChecked(false);
            radio_button3.setChecked(true);
        }
    }


    private class TabHostTouch extends SimpleOnGestureListener {
        private static final int ON_TOUCH_DISTANCE = 120;
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() <= (-ON_TOUCH_DISTANCE)) {
                currentTabID = mTabHost.getCurrentTab() - 1;
                if (currentTabID < 0) {
                    currentTabID = mTabHost.getTabCount() - 1;
                }
            } else if (e1.getX() - e2.getX() >= ON_TOUCH_DISTANCE) {
                currentTabID = mTabHost.getCurrentTab() + 1;
                if (currentTabID >= mTabHost.getTabCount()) {
                    currentTabID = 0;
                }
            }
            mTabHost.setCurrentTab(currentTabID);
            return false;
        }
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
