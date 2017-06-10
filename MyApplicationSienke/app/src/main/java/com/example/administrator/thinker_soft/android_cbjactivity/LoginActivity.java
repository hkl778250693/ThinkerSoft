package com.example.administrator.thinker_soft.android_cbjactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.threadsocket.QucklySocketInteraction;
import com.example.administrator.thinker_soft.myfirstpro.util.AssembleUpmes;
import com.example.administrator.thinker_soft.myfirstpro.util.Gadget;
import com.example.administrator.thinker_soft.myfirstpro.util.JsonAnalyze;
import com.example.administrator.thinker_soft.myfirstpro.util.MyDialog;
import com.example.administrator.thinker_soft.myfirstpro.util.Mytoast;

import java.util.HashMap;
import java.util.Map;

import com.example.administrator.thinker_soft.myfirstpro.util.JaugeInternetState;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText edit_user;// �û���
	private EditText edit_passwrod;// ����
	private TextView set_IP_DK;// ����IP�����˿�
	private Button bt_cancel;
	private Button bt_sure;
	private LinearLayout login_back;
	private LinearLayout autoLonginll;
	private LinearLayout handLonginll;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private String username;
	private String password;
	private Intent intent;
	private String ip = "";
	private String port = "";
	private int clickCount;
	private String operName = "NANBU";
	private Map<Integer, Boolean> dialogControl;
	private Dialog downDialog;
	private String login_json;
	private String RoleName, COMPANY, SystemUserId, DEPARTMENT, USER_NAME;
	private MyActivityManager mam = MyActivityManager.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mam.pushOneActivity(this);
		guiWidget();
		init();
	}

	private void init() {
		dialogControl = new HashMap<Integer, Boolean>();

		preferences = getSharedPreferences("config", MODE_PRIVATE);
		editor = preferences.edit();
		username = preferences.getString("username", "");
		password = preferences.getString("password", "");
		// username = preferences.getString("username", "");
		// password = preferences.getString("password", "");
		if (username != null & !"".equals(username)) {
			edit_user.setText(username);
			edit_user.setSelection(username.length());
		}

		if (password != null & !"".equals(password)) {
			edit_passwrod.setText(password);
			edit_passwrod.setSelection(edit_passwrod.length());
		}
	}

	/**
	 * ���ؿؼ�
	 */
	private void guiWidget() {
		set_IP_DK = (TextView) findViewById(R.id.set_IP_DK);
		edit_user = (EditText) findViewById(R.id.edit_user);
		edit_passwrod = (EditText) findViewById(R.id.edit_passwrod);
		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_sure = (Button) findViewById(R.id.bt_sure);
		autoLonginll = (LinearLayout) findViewById(R.id.autoLonginll);
		handLonginll = (LinearLayout) findViewById(R.id.handLonginll);
		// login_back = (LinearLayout) findViewById(R.id.login_back);
		bt_cancel.setOnClickListener(this);
		bt_sure.setOnClickListener(this);
		set_IP_DK.setOnClickListener(this);
		/*
		 * login_back.setOnClickListener(this); MyImageView imageView = new
		 * MyImageView(getApplicationContext()); LinearLayout.LayoutParams
		 * params = new LinearLayout.LayoutParams(20, 20); params.topMargin =
		 * 100; autoLonginll.addView(imageView, params);
		 */
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
			mam.finishAllActivity();
		}
		return false;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ȡ��
		case R.id.bt_cancel:
			Gadget.closeKeybord(edit_user, LoginActivity.this);
			Gadget.closeKeybord(edit_passwrod, LoginActivity.this);
			this.finish();
			break;
		case R.id.set_IP_DK:// ����IP�Ͷ˿�
			intent = new Intent(LoginActivity.this, Ip_Port_activity.class);
			startActivity(intent);

			break;
		// ��¼
		case R.id.bt_sure:// ���ж��û����������Ƿ�Ϊ�ա�IP�Ͷ˿��Ƿ�Ϊ�ա������Ƿ�������
			// ��¼ǰ�û��������벻��Ϊ��
			SharedPreferences sharedPreferences = getApplication()
					.getSharedPreferences("IP_PORT_DBNAME", 0);
			if (sharedPreferences.contains("ip")) {
				ip = sharedPreferences.getString("ip", "");
			}
			if (sharedPreferences.contains("port")) {
				port = sharedPreferences.getString("port", "");
			}
			username = edit_user.getText().toString();
			username = username.toUpperCase();
			Log.e("xxq", "username=" + username);
			password = edit_passwrod.getText().toString();
			Log.e("xxq", "IP=" + ip + "     port=" + port);
			if (username == null || "".equals(username) || password == null
					|| "".equals(password)) {
				Mytoast.showToast(LoginActivity.this, "�û��������벻��Ϊ��", 1000);
				return;
			} else {
				if (ip == null || "".equals(ip) || port == null
						|| "".equals(port)) {
					Mytoast.showToast(this, "IP�Ͷ˿�Ϊ�գ�������", 1000);
				} else {
					if (!JaugeInternetState
							.isNetworkAvailable(getApplicationContext())) {
						Toast.makeText(this, "�����������WIFI������������",
								Toast.LENGTH_SHORT).show();
						break;
					}
					clickCount++;
					dialogControl.put(clickCount, true);
					Log.e("xxq", "MyDialog--------------");
					downDialog = MyDialog.createLoadingDialog(
							LoginActivity.this, "��¼��,���Ժ�....");
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
					    RequestLog();// �����¼
			
				/*  editor.putString("username", username);
					Log.e("xxq", "editor=" + editor);
					editor.putString("password", password);
					editor.commit();
				    intent = new Intent(LoginActivity.this,
							CBJActivity.class);
					startActivity(intent);
					finish();*/
				}
			}
			break;
		default:
			break;
		}

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
				boolean singal = false;// �ж���;�Ƿ����
				String parameter = AssembleUpmes.loginParameter(username,
						password);
				Log.e("xxq", "parameter=" + parameter);
				QucklySocketInteraction areaSocket = new QucklySocketInteraction(
						getApplicationContext(), Integer.parseInt(port), ip,
						operName, parameter, dataHandler);
				singal = areaSocket.DataDownLoadConn();// ����
				areaSocket.closeConn();// �Ͽ�����
				if (dialogControl.size() > 0) {
					if (dialogControl.get(loc) == true) {
						if (singal == true) {// �ɹ�
							dataHandler.post(new Runnable() {
								@Override
								public void run() {
									downDialog.dismiss();
								/*	editor.putString("username", username);
									Log.e("xxq", "editor=" + editor);
									editor.putString("password", password);
									editor.commit();
									intent = new Intent(LoginActivity.this,
											CBJActivity.class);
									startActivity(intent);
									finish();
*/
								}
							});
						} else {// ʧ��
							dataHandler.post(new Runnable() {
								@Override
								public void run() {
									downDialog.dismiss();
									Mytoast.showToast(LoginActivity.this,
											"������δ��Ӧ������֤IP�Ͷ˿��Ƿ�����", 1000);
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
//					Log.v("xxq", "login_json=" + login_json);
					// TODO
					if ("�û�������".equals(login_json) || "�������".equals(login_json)) {
						Mytoast.showToast(LoginActivity.this, login_json + "",
								1000);
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
						intent = new Intent(LoginActivity.this,
								CBJActivity.class);
						startActivity(intent);
						finish();

					}

				}
			}
		}
	}

	@Override
	protected void onResume() {
		Log.e("xxq", "onResume");
		super.onResume();
	}

	@Override
	protected void onStop() {
		Log.e("xxq", "onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.e("xxq", "onDestroy");
		super.onDestroy();
	}

}
