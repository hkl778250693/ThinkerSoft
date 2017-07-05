package com.example.administrator.thinker_soft.meter_code;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.activity.MeterHomePageActivity;
import com.example.administrator.thinker_soft.myfirstpro.util.AssembleUpmes;
import com.example.administrator.thinker_soft.myfirstpro.util.JsonAnalyze;

import java.util.HashMap;
import java.util.Map;

import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.threadsocket.QucklySocketInteraction;
import com.example.administrator.thinker_soft.myfirstpro.util.JaugeInternetState;

public class WelcomeActivity extends Activity {
	// private boolean mIsEngineInitSuccess = false;
	private SharedPreferences.Editor editor;
	private String login_json;
	private Intent intent;
	private int clickCount;
	private String operName = "NANBU";
	private SharedPreferences preferences;
	private SharedPreferences preferences_IP;
	private String username;
	private String password;
	private String ip = "";
	private String port = "";
	private Map<Integer, Boolean> dialogControl;
	private String RoleName, COMPANY, SystemUserId, DEPARTMENT, USER_NAME;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		preferences_IP = getApplication().getSharedPreferences(
				"IP_PORT_DBNAME", 0);
		preferences = getSharedPreferences("config", MODE_PRIVATE);
		username = preferences.getString("username", "");
		password = preferences.getString("password", "");
		editor = preferences.edit();
		if (preferences_IP.contains("ip")) {
			ip = preferences_IP.getString("ip", "");
		}
		if (preferences_IP.contains("port")) {
			port = preferences_IP.getString("port", "");
		}
		dialogControl = new HashMap<Integer, Boolean>();

		// intent = new Intent(WelcomeActivity.this, MeterHomePageActivity.class);
		// startActivity(intent);
		// finish();
		// myThread();
		if (username != null
				& !"".equals(username)// ������֡����롢IP���˿ڡ�����
				&& password != null & !"".equals(password) && ip != null
				& !"".equals(ip) && password != null & !"".equals(password)
				&& JaugeInternetState.isNetworkAvailable(getApplicationContext())) {
			dialogControl.put(clickCount, true);
			RequestLog();// �����¼
		} else {
			myThread();
		}
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	/**
	 * �����¼
	 */
	private void RequestLog() {
		final MyHandler dataHandler = new MyHandler();
		new Thread() {
			@Override
			public void run() {
				super.run();
				int loc = clickCount;
				boolean singal = false;
				String parameter = AssembleUpmes.loginParameter(username,
						password);
				Log.e("xxq", "parameter=" + parameter);
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				QucklySocketInteraction areaSocket = new QucklySocketInteraction(
						getApplicationContext(), Integer.parseInt(port), ip,
						operName, parameter, dataHandler);
				singal = areaSocket.DataDownLoadConn();
				areaSocket.closeConn();
				if (dialogControl.size() > 0) {
					Log.e("ZHOUTAO", "----------��ǰ�߳���ţ�" + loc);
					if (dialogControl.get(loc) == true) {
						if (singal == true) {
							dataHandler.post(new Runnable() {
								@Override
								public void run() {

								}
							});
						} else {
							dataHandler.post(new Runnable() {

								@Override
								public void run() {
									turnToCBJActivity();
									finish();

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
			int loc = clickCount;// ��ǰ�̵߳����
			Bundle bundle = msg.getData();
			String data = bundle.getString("data");
			if (dialogControl.size() > 0) {
				Log.v("ZHOUTAO", "��ǰ�߳���ţ�" + loc);
				if (dialogControl.get(loc) == true) {
					login_json = JsonAnalyze.analyzeData(data);// ����������Ľ��
					Log.e("xxq", "---------login_json=" + login_json);
					// TODO
					if ("�û�������".equals(login_json) || "�������".equals(login_json)) {
						turnToCBJActivity();
						finish();
					} else {
						// ��¼�ɹ�
						Map<String, String> logmap = JsonAnalyze
								.jsonServiceBackMsg(login_json);

						RoleName = logmap.get("RoleName");
						COMPANY = logmap.get("COMPANY");
						SystemUserId = logmap.get("SystemUserId");
						DEPARTMENT = logmap.get("DEPARTMENT");
						USER_NAME = logmap.get("USER_NAME");
						Log.e("xxq", "RoleName=" + RoleName + "  COMPANY="
								+ COMPANY + " SystemUserId=" + SystemUserId
								+ "  DEPARTMENT=" + DEPARTMENT + "  USER_NAME="
								+ USER_NAME);
						editor.putString("RoleName", RoleName);
						editor.putString("COMPANY", COMPANY);
						editor.putString("SystemUserId", SystemUserId);
						editor.putString("DEPARTMENT", DEPARTMENT);
						editor.putString("USER_NAME", USER_NAME);

						// usernameΪ��¼�������� USER_NAMEΪ��¼�ɹ����غ����ʵ����
						editor.putString("username", username);
						Log.e("xxq", "editor=" + editor);
						editor.putString("password", password);
						editor.commit();
						
						intent = new Intent(WelcomeActivity.this,
								MeterHomePageActivity.class);
						startActivity(intent);
						finish();
					}

				}
			}
		}
	}

	/**
	 * ����
	 */
	private void myThread() {
		Thread thread = new Thread() {
			public void run() {
				try {
					Thread.sleep(2000);
					turnToCBJActivity();

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				finish();
			};
		};
		thread.start();
	}

	private void turnToCBJActivity() {
		Log.e("xxq", "---------turnToCBJActivity=");
		Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
		startActivity(intent);
	}
}