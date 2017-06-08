package example.android_cbjactivity;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.example.administrator.thinker_soft.R;

import org.json.JSONException;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cc.thksoft.myfirstpro.appcation.MyApplication;
import cc.thksoft.myfirstpro.entity.AreaInfo;
import cc.thksoft.myfirstpro.entity.BookInfo;
import cc.thksoft.myfirstpro.myactivitymanager.MyActivityManager;
import cc.thksoft.myfirstpro.service.LocationService;
import cc.thksoft.myfirstpro.service.LocationService.LocationBinder;
import cc.thksoft.myfirstpro.threadsocket.SocketInteraction;
import cc.thksoft.myfirstpro.util.AnimationTabHost;
import cc.thksoft.myfirstpro.util.AssembleUpmes;
import cc.thksoft.myfirstpro.util.Gadget;
import cc.thksoft.myfirstpro.util.JaugeInternetState;
import cc.thksoft.myfirstpro.util.JsonAnalyze;
import cc.thksoft.myfirstpro.util.MyDialog;
import cc.thksoft.myfirstpro.util.Mytoast;
import readystatesoftware.viewbadger.BadgeView;

@SuppressWarnings("deprecation")
public class CBJActivity extends TabActivity implements RadioGroup.OnCheckedChangeListener,OnTabChangeListener{
	private FrameLayout tv_set;	
	private TextView systemSet_badget_tv;
	private TextView homepageTitle;
	private PopupWindow popupWindow;
	private LinearLayout cbjBackBtn;
	private EditText et;
	private int POPWINDOWWIDTH;
	private GestureDetector gestureDetector;
	private AnimationTabHost mTabHost;
    @SuppressWarnings("unused")
	private TabWidget mTabWidget;
	private RadioGroup radioderGroup;
	private RadioButton radio_button0,radio_button1,radio_button2,radio_button3;
	//
	private List<BookInfo>  bookList;
	private List<AreaInfo> areaList;
	private String DBName;
	private String filepath;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	//private BDLocationModel bdLocationModel;
	
	/*** ��¼��ǰ��ҳID */
    private int currentTabID = 0;
	private LocationService mService;
	private boolean mBound = false;
	private MyApplication myapp;
	//���Ӳ���
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
			// �����Ѿ��󶨵���LocalService����IBinder����ǿ������ת�����һ�ȡLocalServiceʵ����
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
			// ������ʼ�����첽�ģ���ҪһС��ʱ�䣬�������־��ʶ�������Ƿ��ʼ���ɹ���Ϊtrueʱ����ܷ��𵼺�
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
		String sdpath = getSdcardDir();
		/*if ("".equals(sdpath)) {
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
		
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		SystemUserId = sharedPreferences.getString("SystemUserId", "");

		sharedPreferences = getApplication().getSharedPreferences(
				"IP_PORT_DBNAME", 0);
		editor = sharedPreferences.edit();
		DBName = sharedPreferences.getString("dbName", "");
		Intent intent = new Intent(this, LocationService.class);
		// TODO
		System.out.println("�����Ƿ������¹���"
				+ bindService(intent, mConnection,
						Context.BIND_AUTO_CREATE));
		//ʵ����RadioButton��RadioGroup*/
		radio_button0=(RadioButton) findViewById(R.id.radio_button0);
		radio_button1=(RadioButton) findViewById(R.id.radio_button1);
		radio_button2=(RadioButton) findViewById(R.id.radio_button2);
		radio_button3=(RadioButton) findViewById(R.id.radio_button3);
		radioderGroup=(RadioGroup) findViewById(R.id.main_radio);
		cbjBackBtn = (LinearLayout) findViewById(R.id.cbj_back_btn);

		cbjBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		//ʵ����TabHost
		mTabHost =  (AnimationTabHost)findViewById(android.R.id.tabhost);
		mTabWidget = (TabWidget) findViewById(android.R.id.tabs);

	    mTabHost.setOnTabChangedListener(this);
	        
		//��ȡTabhost��ѡ�
		//����ѡ��
		mTabHost.addTab(mTabHost.newTabSpec("ONE").setIndicator("ONE")
				.setContent(new Intent(CBJActivity.this, ChaoBiaoActivity.class)));
		//ɨ�볭��
		mTabHost.addTab(mTabHost.newTabSpec("TWO").setIndicator("TWO")
				.setContent(new Intent(CBJActivity.this, CaptureActivity.class)));
		//�Զ����ѯ
		mTabHost.addTab(mTabHost.newTabSpec("THREE").setIndicator("THREE")
				.setContent(new Intent(CBJActivity.this, ChaXunActivity.class)));
		//���ݴ���
		mTabHost.addTab(mTabHost.newTabSpec("FOUR").setIndicator("FOUR")
				.setContent(new Intent(CBJActivity.this, SJCSActivity.class)));
		mTabHost.setOpenAnimation(true);
		radioderGroup.setOnCheckedChangeListener(this);
		radioderGroup.setClickable(true);
		gestureDetector = new GestureDetector(new TabHostTouch());

        new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                if (gestureDetector.onTouchEvent(event)){
                    return true;
                }
                return false;
            }
        };

        
        homepageTitle = (TextView) findViewById(R.id.homepageTitle);
        homepageTitle.setText("����ѡ��");
        systemSet_badget_tv = (TextView) findViewById(R.id.systemSet_badget_tv);
        badgeViewone = new BadgeView(this, systemSet_badget_tv);
        badgeViewone.setText("����");
        badgeViewone.setTextSize(9);
        tv_set = (FrameLayout) findViewById(R.id.systemSet);
		tv_set.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AddPopWindowenu();
			}
		});
	}
	//�˵�������
	private void AddPopWindowenu(){
		if (popupWindow == null) {
			View contentView = getLayoutInflater().inflate(
					R.layout.popwindow_content, null);
			popupWindow = new PopupWindow(contentView,
					POPWINDOWWIDTH / 2,LayoutParams.WRAP_CONTENT);
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setBackgroundDrawable(getResources()
					.getDrawable(R.mipmap.popwindow_main_bg));
			final TextView sysSet = (TextView) contentView
					.findViewById(R.id.popwindow_content_sysSet);
			final TextView gpscollector = (TextView) contentView
					.findViewById(R.id.popwindow_content_gpscollector);
			final TextView mapMeter = (TextView) contentView
					.findViewById(R.id.popwindow_content_mapMeter);
			final TextView tasks = (TextView) contentView
					.findViewById(R.id.popwindow_content_actualtask);
			LinearLayout popwindow_content_actualtask_ll = (LinearLayout) contentView
					.findViewById(R.id.popwindow_content_actualtask_ll);
			badgeViewTwo = new BadgeView(CBJActivity.this, popwindow_content_actualtask_ll);
			badgeViewTwo.setText("����");
			badgeViewTwo.setTextSize(9);
			if(badgeViewone!=null&&badgeViewone.isShown()){
				badgeViewTwo.show();
			}
			View logout = contentView
					.findViewById(R.id.popwindow_content_logout);
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
					Intent intent1 = new Intent();
					intent1.setClass(CBJActivity.this,
							SheZiActivity.class);
					startActivityForResult(intent1, 1);
					popupWindow.dismiss();
				}
			});
			gpscollector.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					DBName = sharedPreferences.getString("dbName", "");
					if (DBName != null && !"".equals(DBName)) {
						Intent intent1 = new Intent();
						intent1.setClass(CBJActivity.this,
								GPSCollectorActivity.class);
						startActivity(intent1);
						popupWindow.dismiss();
					} else {
						Toast.makeText(getApplicationContext(),
								"��ѡ�񳭱�", Toast.LENGTH_SHORT).show();
						popupWindow.dismiss();
					}

				}
			});
			tasks.setOnClickListener(new OnClickListener() {// ��������

				@Override
				public void onClick(View v) {
					if (myapp.getNewWorkList() == null
							|| myapp.getNewWorkList().size() == 0||badgeViewTwo.isShown()) {
						requestWork();
					} else if (myapp.getNewWorkList() != null
							&& myapp.getNewWorkList().size() != 0&&!badgeViewTwo.isShown()) {
						popupWindow.dismiss();
						Intent	intent = new Intent(CBJActivity.this,
								ActualMissionNEW.class);
						intent.setAction("");
						startActivity(intent);
					}
				}
			});
			mapMeter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					/*
					 * Typeface typeface =
					 * Typeface.create(Typeface.DEFAULT,Typeface.BOLD);
					 * ((TextView) v).setTypeface(typeface); Typeface
					 * stypeface =
					 * Typeface.create(Typeface.DEFAULT,Typeface
					 * .NORMAL); sysSet.setTypeface(stypeface);
					 */
					// �ж��Ƿ�ѡ�������ݿ�
					DBName = sharedPreferences.getString("dbName", "");
					if (DBName != null && !"".equals(DBName)) {
						Intent intent1 = new Intent();
						intent1.setClass(CBJActivity.this,
								MapMeterActivity.class);
						startActivity(intent1);
						popupWindow.dismiss();
					} else {
						Toast.makeText(getApplicationContext(),
								"��ѡ�񳭱�", Toast.LENGTH_SHORT).show();
						popupWindow.dismiss();
					}
				}
			});
			logout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					/*
					 * Typeface typeface =
					 * Typeface.create(Typeface.DEFAULT,Typeface.BOLD);
					 * ((TextView) v).setTypeface(typeface); Typeface
					 * stypeface =
					 * Typeface.create(Typeface.DEFAULT,Typeface
					 * .NORMAL); sysSet.setTypeface(stypeface);
					 */
					popupWindow.dismiss();
					finish();
					System.exit(0);
				}
			});
			popupWindow.showAsDropDown(tv_set);
		} else {
			if(badgeViewone!=null&&badgeViewone.isShown()){
				if(badgeViewTwo!=null){
					badgeViewTwo.show();
				}
			}
			if (!popupWindow.isShowing()) {
				popupWindow.showAsDropDown(tv_set, -50, 0);
			}else{
				popupWindow.dismiss();
			}
		}
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
	
	/**
	 * 
	 * �˵������ؼ���Ӧ
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			//����˫���˳�����
			exitBy2Click();
		} 
		return false;
	}
	/**
	 * 
	 * ˫���˳�����
	 */
	private static Boolean isExit=false;
	
	private void exitBy2Click() {
		Timer tExit=null;
		if(isExit==false){
			//׼���˳�
			isExit=true;
			Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
			tExit=new Timer();
			tExit.schedule(new TimerTask() {
				
				@Override
				public void run() {
					//ȡ���˳�
					isExit=false;
				}
			}, 4000);//���4������û�а��·��ؼ�����������ʱ��ȡ�����ղ�ִ�е�����
			
		}else{
			mam.finishAllActivity();
		}
		
	}
	//�����ҳ ʱ ��ǩ�л�
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
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
		case R.id.cbj_back_btn:
			Gadget.closeKeybord(et, CBJActivity.this);
			finish();
		}
		
	}
	//�໬��ҳʱ ��ǩ�л�
	@Override
	public void onTabChanged(String tabId) {
		if("ONE".equals(tabId)){
			mTabHost.setCurrentTabByTag("ONE");
			radio_button0.setChecked(true);
			radio_button1.setChecked(false);
			radio_button2.setChecked(false);
			radio_button3.setChecked(false);
		}else if("TWO".equals(tabId)){
			mTabHost.setCurrentTabByTag("TWO");
			radio_button0.setChecked(false);
			radio_button1.setChecked(true);
			radio_button2.setChecked(false);
			radio_button3.setChecked(false);
		}else if("THREE".equals(tabId)){
			mTabHost.setCurrentTabByTag("THREE");
			radio_button0.setChecked(false);
			radio_button1.setChecked(false);
			radio_button2.setChecked(true);
			radio_button3.setChecked(false);
		}else if("FOUR".equals(tabId)){
			mTabHost.setCurrentTabByTag("FOUR");
			radio_button0.setChecked(false);
			radio_button1.setChecked(false);
			radio_button2.setChecked(false);
			radio_button3.setChecked(true);
		}
	}
	
	
	
	 private class TabHostTouch extends SimpleOnGestureListener {

	        /** ������ҳ������� */

	        private static final int ON_TOUCH_DISTANCE = 120;

	 

	        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,

	                float velocityY) {

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
	 
	 /**
		 * ������������ ���ж��Ƿ�������
		 */
		public void requestWork() {
			popupWindow.dismiss();
			if (!JaugeInternetState.isNetworkAvailable(getApplicationContext())) {
				Toast.makeText(this, "�����������WIFI������������", Toast.LENGTH_SHORT)
						.show();
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
											Mytoast.showToast(
													CBJActivity.this,
													"������δ��Ӧ������֤IP�Ͷ˿��Ƿ�����", 1000);
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
				if(key==1){
					if(badgeViewone!=null){
						badgeViewone.show();
					}
					return;
				}
				String data = bundle.getString("data");
				if(data!=null&&"û������".equals(data)){
					myapp.setNewWorkList(null);
					Intent intent = new Intent(CBJActivity.this,
							ActualMissionNEW.class);
					if(badgeViewone.isShown()==true||badgeViewTwo.isShown()==true){
						intent.setAction("show");
					}else{
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
							};
							myapp.setNewWorkList(ALLworkList_before);
							// ȡ�����ݳɹ�����ֵ����һ��Activity
							Intent intent = new Intent(CBJActivity.this,
									ActualMissionNEW.class);
							if(badgeViewone.isShown()==true||badgeViewTwo.isShown()==true){
								intent.setAction("show");
							}else{
								intent.setAction("");
							}
							// intent.putExtra("ALLworkList_before",
							// (Serializable) ALLworkList_before);
							startActivity(intent);
							if(badgeViewone!=null){
								badgeViewone.hide();
							}
							if(badgeViewTwo!=null){
								badgeViewTwo.hide();
							}											
						}

					}
				}
			}
		}
		
}
