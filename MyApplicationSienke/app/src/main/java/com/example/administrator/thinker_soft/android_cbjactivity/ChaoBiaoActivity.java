package com.example.administrator.thinker_soft.android_cbjactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.entity.UsersInfo;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;

import java.util.List;

public class ChaoBiaoActivity extends Activity {
	private GridView gridView;
	private static final int CODE = 1;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private String filepath;
	private String DBName;
	private List<UsersInfo> list;
	private int signal = 0;// ��ʾ��־
	private int con_signal = 0;// ������ʾ��־
	private PopupWindow popupWindow;
	private int POPWINDOWWIDTH;
	private int POPWINDOWHEIGHT;
	private LinearLayout first_layout,second_layout,third_layout,fourth_layout,fifth_layout,sixth_layout;
	private MyActivityManager mam ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chaobiao);

		bindView();
		defaultSetting();
		setViewClickListener();
	}

	//绑定控件
	private void bindView() {
		first_layout  = (LinearLayout) findViewById(R.id.first_layout);
		second_layout = (LinearLayout) findViewById(R.id.second_layout);
		third_layout  = (LinearLayout) findViewById(R.id.third_layout);
		fourth_layout = (LinearLayout) findViewById(R.id.fourth_layout);
		fifth_layout  = (LinearLayout) findViewById(R.id.fifth_layout);
		sixth_layout = (LinearLayout) findViewById(R.id.sixth_layout);
	}

	//初始化设置
	private void defaultSetting() {
		mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		filepath = Environment.getDataDirectory().getPath() + "/data/"
				+ "com.example.android_cbjactivity" + "/databases/";
		sharedPreferences = getApplication().getSharedPreferences(
				"IP_PORT_DBNAME", 0);
		editor = sharedPreferences.edit();
		DBName = sharedPreferences.getString("dbName", "");
	}

	//点击事件
	public void setViewClickListener() {
		first_layout.setOnClickListener(clickListener);
		second_layout.setOnClickListener(clickListener);
		third_layout.setOnClickListener(clickListener);
		fourth_layout.setOnClickListener(clickListener);
		fifth_layout.setOnClickListener(clickListener);
		sixth_layout.setOnClickListener(clickListener);
	}

	View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.first_layout:
					signal = 1;
					editor = sharedPreferences.edit();
					editor.putInt("signal", signal);
					editor.apply();
					DBName = sharedPreferences.getString("dbName", "");
					if (DBName != null && !"".equals(DBName)) {
						Intent intent = new Intent(ChaoBiaoActivity.this, ShowListviewActivity.class);
						startActivityForResult(intent, CODE);
					} else {
						Toast.makeText(ChaoBiaoActivity.this, "请您选择抄表本！", Toast.LENGTH_SHORT).show();
						return;
					}
					break;
				case R.id.second_layout:
					signal = 2;
					con_signal = 2;
					editor = sharedPreferences.edit();
					editor.putInt("signal", signal);
					editor.putInt("con_signal", con_signal);
					editor.apply();
					DBName = sharedPreferences.getString("dbName", "");
					System.out.println("DBName:" + DBName);
					if (DBName != null && !"".equals(DBName)) {
						Intent intent = new Intent(ChaoBiaoActivity.this, ChaoBiaoXuanZeActivity.class);
						intent.putExtra("DBName", DBName);
						startActivityForResult(intent, CODE);
					} else {
						Toast.makeText(ChaoBiaoActivity.this, "请您选择抄表本！", Toast.LENGTH_SHORT).show();
						return;
					}
					break;
				case R.id.third_layout:
					signal = 3;
					con_signal = 3;
					editor = sharedPreferences.edit();
					editor.putInt("signal", signal);
					editor.putInt("con_signal", con_signal);
					editor.apply();
					// SharedPreferences sharedPreferences =
					// getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
					DBName = sharedPreferences.getString("dbName", "");
					if (DBName != null && !"".equals(DBName)) {
						Intent intent = new Intent(ChaoBiaoActivity.this, ShowListviewActivity.class);
						startActivityForResult(intent, CODE);
					} else {
						Toast.makeText(ChaoBiaoActivity.this, "请您选择抄表本！", Toast.LENGTH_SHORT).show();
						return;
					}
					break;
				case R.id.fourth_layout:
					signal = 4;
					con_signal = 4;
					editor.putInt("signal", signal);
					editor.putInt("con_signal", con_signal);
					editor.commit();
					DBName = sharedPreferences.getString("dbName", "");
					if (DBName != null && !"".equals(DBName)) {
						Intent intent = new Intent(ChaoBiaoActivity.this, WeiChaoBaoActivity.class);
						intent.putExtra("DBName", DBName);
						startActivityForResult(intent, CODE);
					} else {
						Toast.makeText(ChaoBiaoActivity.this, "请您选择抄表本！", Toast.LENGTH_SHORT).show();
						return;
					}
					break;
				case R.id.fifth_layout:
					Intent intent = new Intent(ChaoBiaoActivity.this, ChaoBiaoBenTongjiActivity.class);
					startActivity(intent);
					break;
				case R.id.sixth_layout:
					signal = 6;
					editor.putInt("signal", signal);
					editor.apply();
					Intent intent1 = new Intent(ChaoBiaoActivity.this, ChaoBiaoXuanZeActivity.class);
					startActivity(intent1);
					break;
				default:
					break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (CODE == requestCode) {
			System.out.println("����");
		}
		if (requestCode == 1 && resultCode == 1) {
			finish();
			System.exit(0);
		}
		if (requestCode == 1 && resultCode == 2) {
			Intent intent = new Intent(ChaoBiaoActivity.this,
					LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
