package com.example.administrator.thinker_soft.meter_code;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.entity.UsersInfo;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.util.Gadget;

import java.util.List;

public class BianHaoCXActivity extends Activity {
	private EditText editUserNumber;
	private TextView cancel,query;
	private ImageView back;
	private String filepath;
	private String DBName;
	private List<UsersInfo> usList;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bianhao);

		bindView();
		defaultSetting();
		setViewClickListener();
	}

	//绑定控件
	private void bindView() {
		back = (ImageView) findViewById(R.id.back);
		editUserNumber = (EditText) findViewById(R.id.edit_user_number);
		cancel = (TextView) findViewById(R.id.cancel);
		query = (TextView) findViewById(R.id.query);
	}

	//初始化设置
	private void defaultSetting() {
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		SharedPreferences sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
		editor = sharedPreferences.edit();
		DBName = sharedPreferences.getString("dbName", "");
		if(DBName!=null&&!"".equals(DBName)){
			filepath = Environment.getDataDirectory().getPath() + "/data/"+"com.example.android_cbjactivity"+"/databases/";
		}else{
			Toast.makeText(getApplicationContext(), "请先完成首页文件选择哦！", Toast.LENGTH_SHORT).show();
		}
	}

	//点击事件
	public void setViewClickListener() {
		back.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		query.setOnClickListener(onClickListener);
	}

	View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.back:
					Gadget.closeKeybord(editUserNumber, BianHaoCXActivity.this);
					finish();
					break;
				case R.id.cancel:
					if (editUserNumber != null && editUserNumber.getText() != null) {
						editUserNumber.setText("");
					}
					break;
				case R.id.query:
					String usID = editUserNumber.getText().toString();
					if ("".equals(usID) || usID == null) {
						Toast.makeText(getApplicationContext(), "����д��ѯ��Ϣ", Toast.LENGTH_LONG).show();
					} else {
						Thread thread = new Thread() {
							@Override
							public void run() {
								super.run();
								int signal = 8;
								int con_signal = 8;
								String usId = editUserNumber.getText().toString();
								editor.putInt("signal", signal);
								editor.putInt("con_signal", con_signal);
								editor.putString("usId", usId);
								editor.commit();
								Intent intent = new Intent();
								intent.setClass(BianHaoCXActivity.this, MeterUserListviewActivity.class);
								startActivity(intent);
							}
						};
						thread.start();
						try {
							thread.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					break;
				default:
					break;
			}
		}
	};

}
