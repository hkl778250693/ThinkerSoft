package example.android_cbjactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

import java.util.Timer;
import java.util.TimerTask;

import cc.thksoft.myfirstpro.myactivitymanager.MyActivityManager;

public class ChaXunActivity extends Activity{
	private final String filepath = Environment.getDataDirectory().getPath() + "/data/"+"com.example.android_cbjactivity"+"/databases/";
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private String DBName;
	private LinearLayout by_met_num,by_user_name,by_user_num,by_user_met,by_user_minus;
	private MyActivityManager mam;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				by_user_minus.setEnabled(true);
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "��ѡ�񳭱�", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cx);
		mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		by_met_num = (LinearLayout) findViewById(R.id.by_met_num);
		by_user_name = (LinearLayout) findViewById(R.id.by_user_name);
		by_user_num = (LinearLayout) findViewById(R.id.by_user_num);
		by_user_met = (LinearLayout) findViewById(R.id.by_user_met);
		by_user_minus = (LinearLayout) findViewById(R.id.by_user_minus);
		sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
		editor = sharedPreferences.edit();
		DBName = sharedPreferences.getString("dbName", "");
	}
	@Override
	protected void onStart() {
		super.onStart();

	}
	@Override
	protected void onResume() {
		super.onResume();
		by_met_num.setEnabled(true);
		by_user_name.setEnabled(true);
		by_user_num.setEnabled(true);
		by_user_met.setEnabled(true);
		by_user_minus.setEnabled(true);
	}
	@Override
	protected void onPause() {
		super.onStop();
		by_met_num.setEnabled(false);
		by_user_name.setEnabled(false);
		by_user_num.setEnabled(false);
		by_user_met.setEnabled(false);
		by_user_minus.setEnabled(false);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.v("onSaveInstanceState:", "onSaveInstanceState()");
	}
	public void onAction(View v) {
		int id=v.getId();
		
		switch (id) {
		//ת������˳��Ų�ѯ
		case R.id.by_met_num:
			Intent intent1=new Intent();
			intent1.setClass(ChaXunActivity.this, CbbCXAction.class);
			startActivity(intent1);
			break;
		//���û����Ʋ�ѯ
		case R.id.by_user_name:
			Intent intent2=new Intent();
			intent2.setClass(ChaXunActivity.this, YongHuMingCX.class);
			startActivity(intent2);
			break;
		//���û���Ų�ѯ
		case R.id.by_user_num:
			Intent intent3=new Intent();
			intent3.setClass(ChaXunActivity.this, BianHaoCXActivity.class);
			startActivity(intent3);
			break;
		//���û���Ų�ѯ
		case R.id.by_user_met:
			Intent intent4=new Intent();
			intent4.setClass(ChaXunActivity.this, BiaoHaoCXActivity.class);
			startActivity(intent4);
			break;
			//�����û���ѯ
		case R.id.by_user_minus:
			by_user_minus.setEnabled(false);
			new Thread(){
				@Override
				public void run() {
					super.run();
					int signal = 10;
					int con_signal = 10;

					editor.putInt("signal", signal);
					editor.putInt("con_signal", con_signal);
					editor.commit();
					DBName = sharedPreferences.getString("dbName", "");
					if(DBName!=null&&!"".equals(DBName)){
						Intent intent = new Intent();
						intent.setClass(ChaXunActivity.this, ShowListviewActivity.class);
						startActivity(intent);
					}else{
						handler.sendEmptyMessage(1);
						return;
					}
					handler.sendEmptyMessage(0);
				}
			}.start();
				break;			
		default:
			break;
		}
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
	
}
