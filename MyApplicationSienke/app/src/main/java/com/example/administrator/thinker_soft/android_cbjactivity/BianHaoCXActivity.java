package com.example.administrator.thinker_soft.android_cbjactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

import java.util.List;

import com.example.administrator.thinker_soft.myfirstpro.entity.UsersInfo;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.util.Gadget;

public class BianHaoCXActivity extends Activity {
	private EditText et;
	private Button btn;
	private String filepath;
	private String DBName;
	private List<UsersInfo> usList;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		setContentView(R.layout.activity_bianhao);
		et = (EditText) findViewById(R.id.editbianhao);
		btn = (Button) findViewById(R.id.button_quedin);
		SharedPreferences sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
		editor = sharedPreferences.edit();
		DBName = sharedPreferences.getString("dbName", "");
		if(DBName!=null&&!"".equals(DBName)){
			btn.setEnabled(true);
			filepath = Environment.getDataDirectory().getPath() + "/data/"+"com.example.android_cbjactivity"+"/databases/";
		}else{
			btn.setEnabled(false);
			Toast.makeText(getApplicationContext(), "�������ҳ�ļ�ѡ��", Toast.LENGTH_SHORT).show();
		}
	}
	public void onAction(View view){
		int id = view.getId();
		switch (id) {
		case R.id.button_quedin:
			String usID = et.getText().toString();
			if("".equals(usID)||usID==null){
				Toast.makeText(getApplicationContext(), "����д��ѯ��Ϣ", Toast.LENGTH_LONG).show();
			}else{
				Thread thread = new Thread(){
					@Override
					public void run() {
						super.run();
						int signal = 8;
						int con_signal = 8;
						String usId = et.getText().toString();
						editor.putInt("signal", signal);
						editor.putInt("con_signal", con_signal);
						editor.putString("usId", usId);
						editor.commit();
						Intent intent = new Intent();
						intent.setClass(BianHaoCXActivity.this, ShowListviewActivity.class);
						startActivity(intent);
					}
				};
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				btn.setEnabled(true);
			}
			break;
		case R.id.button_quxiao:
			if(et!=null&&et.getText()!=null){
				et.setText("");
			}
			break;
		case R.id.bianhao_back_btn:
			Gadget.closeKeybord(et, BianHaoCXActivity.this);
			finish();
			break;
		default:
			break;
		}
	}
}
