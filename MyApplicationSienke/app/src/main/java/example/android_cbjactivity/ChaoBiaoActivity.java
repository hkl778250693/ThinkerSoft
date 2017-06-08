package example.android_cbjactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cc.thksoft.myfirstpro.entity.UsersInfo;
import cc.thksoft.myfirstpro.myactivitymanager.MyActivityManager;

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
	private Button first_btn,second_btn,third_btn,fourth_btn,fifth_btn,sixth_btn;
	private MyActivityManager mam ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chaobiao);
		mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		filepath = Environment.getDataDirectory().getPath() + "/data/"
				+ "com.example.android_cbjactivity" + "/databases/";
		sharedPreferences = getApplication().getSharedPreferences(
				"IP_PORT_DBNAME", 0);
		editor = sharedPreferences.edit();
		DBName = sharedPreferences.getString("dbName", "");
		first_btn  = (Button) findViewById(R.id.first_btn);
		second_btn  = (Button) findViewById(R.id.second_btn);
		third_btn  = (Button) findViewById(R.id.third_btn);
		fourth_btn  = (Button) findViewById(R.id.fourth_btn);
		fifth_btn  = (Button) findViewById(R.id.fifth_btn);
		sixth_btn  = (Button) findViewById(R.id.sixth_btn);

	}
	
	@Override
	protected void onStart() {
		super.onStart();

	}
	@Override
	protected void onResume() {
		super.onResume();
		first_btn.setEnabled(true);
		second_btn.setEnabled(true);
		third_btn.setEnabled(true);
		fourth_btn.setEnabled(true);
		fifth_btn.setEnabled(true);
		sixth_btn.setEnabled(true);
	}
	@Override
	protected void onPause() {
		super.onStop();
		first_btn.setEnabled(false);
		second_btn.setEnabled(false);
		third_btn.setEnabled(false);
		fourth_btn.setEnabled(false);
		fifth_btn.setEnabled(false);
		sixth_btn.setEnabled(false);
	}
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
			Log.e("xxq", "�˳�����");
			finish();
			System.exit(0);

		}
		if (requestCode == 1 && resultCode == 2) {
			Log.e("xxq", "�����˺�");
			Intent intent = new Intent(ChaoBiaoActivity.this,
					LoginActivity.class);
			startActivity(intent);
			finish();

		}
	}

	public void action(View v) {
		int id = v.getId();
		Intent intent = new Intent();

		switch (id) {
		// ��������
		case R.id.first_btn:
			signal = 1;

			editor = sharedPreferences.edit();
			editor.putInt("signal", signal);
			editor.commit();
			DBName = sharedPreferences.getString("dbName", "");
			if (DBName != null && !"".equals(DBName)) {
				intent.setClass(ChaoBiaoActivity.this,
						ShowListviewActivity.class);
				startActivityForResult(intent, CODE);
			} else {
				Toast.makeText(getApplicationContext(), "��ѡ�񳭱�",
						Toast.LENGTH_SHORT).show();
				return;
			}
			break;
		// ��������
		case R.id.second_btn:
			signal = 2;
			con_signal = 2;

			editor = sharedPreferences.edit();
			editor.putInt("signal", signal);
			editor.putInt("con_signal", con_signal);
			editor.commit();
			DBName = sharedPreferences.getString("dbName", "");
			System.out.println("DBName:" + DBName);
			if (DBName != null && !"".equals(DBName)) {
				intent.putExtra("DBName", DBName);
				intent.setClass(ChaoBiaoActivity.this,
						ChaoBiaoXuanZeActivity.class);
				startActivityForResult(intent, CODE);
			} else {
				Toast.makeText(getApplicationContext(), "��ѡ�񳭱�",
						Toast.LENGTH_SHORT).show();
				return;
			}
			break;
		// ��ҳ����
		case R.id.third_btn:
			signal = 3;
			con_signal = 3;

			editor = sharedPreferences.edit();
			editor.putInt("signal", signal);
			editor.putInt("con_signal", con_signal);
			editor.commit();

			// SharedPreferences sharedPreferences =
			// getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
			DBName = sharedPreferences.getString("dbName", "");
			if (DBName != null && !"".equals(DBName)) {
				intent.setClass(ChaoBiaoActivity.this,
						ShowListviewActivity.class);
				startActivityForResult(intent, CODE);
			} else {
				Toast.makeText(getApplicationContext(), "��ѡ�񳭱�",
						Toast.LENGTH_SHORT).show();
				return;
			}
			break;
		// δ������
		case R.id.fourth_btn:
			signal = 4;
			con_signal = 4;

			editor.putInt("signal", signal);
			editor.putInt("con_signal", con_signal);
			editor.commit();
			DBName = sharedPreferences.getString("dbName", "");
			if (DBName != null && !"".equals(DBName)) {
				intent.putExtra("DBName", DBName);
				intent.setClass(ChaoBiaoActivity.this, WeiChaoBaoActivity.class);
				startActivityForResult(intent, CODE);
			} else {
				Toast.makeText(getApplicationContext(), "��ѡ�񳭱�",
						Toast.LENGTH_SHORT).show();
				return;
			}
			break;
		// ����ͳ��
		case R.id.fifth_btn:
			intent.setClass(ChaoBiaoActivity.this,
					ChaoBiaoBenTongjiActivity.class);
			startActivity(intent);
			break;
		// �ļ�ѡ��
		case R.id.sixth_btn:
			signal = 6;
			editor.putInt("signal", signal);
			editor.commit();
			intent.setClass(ChaoBiaoActivity.this, ChaoBiaoXuanZeActivity.class);
			startActivity(intent);
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
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// ����˫���˳�����
			exitBy2Click();
		}
		return false;
	}

	/**
	 * 
	 * ˫���˳�����
	 */
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			// ׼���˳�
			isExit = true;
			tExit = new Timer();
			tExit.schedule(new TimerTask() {

				@Override
				public void run() {
					// ȡ���˳�
					isExit = false;
				}
			}, 4000);// ���4������û�а��·��ؼ�����������ʱ��ȡ�����ղ�ִ�е�����
		} else {
			mam.finishAllActivity();
		}

	}
}
