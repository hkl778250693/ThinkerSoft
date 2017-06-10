package com.example.administrator.thinker_soft.myfirstpro.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.thinker_soft.myfirstpro.util.AssembleUpmes;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.example.administrator.thinker_soft.myfirstpro.util.SocketGetData;

public class LocationService extends Service {
	private final IBinder mBinder = new LocationBinder();
	private final Random mGenerator = new Random();
	private Thread mythread;
	private SharedPreferences preferences;
	private SharedPreferences.Editor newsEditor;
	private boolean signal;
	private int disConCount;

	public class LocationBinder extends Binder {
		public LocationService getService() {
			// ���ر�service��ʵ�����ͻ��ˣ����ǿͻ��˿��Ե��ñ�service�Ĺ�������
			return LocationService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v("LOCATIONSERVICE:", "�����Ѿ�����");
		System.out.println("�������񣡣�������");
		preferences = getSharedPreferences("config", 0);
		final String operName = preferences.getString("USER_NAME", "");
		final String usId = preferences.getString("SystemUserId", "");
		preferences = getSharedPreferences("IP_PORT_DBNAME", 0);
		newsEditor = preferences.edit();
		// GPS���귢�� ÿ��һ��ʱ��
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// ��Ҫ������:������Ϣ
				String ip = preferences.getString("ip", "");
				String port = preferences.getString("port", "");
				String sb = preferences.getString("sb", "");
				if(sb==null||"".equals(sb)){
					Toast.makeText(getApplicationContext(), "û�п��þ�γ��", Toast.LENGTH_SHORT).show();
					return;
				}
				int loc = sb.indexOf("$");
				String lat = sb.substring(0, loc-1);
				String lon = sb.substring(loc+1, sb.length());
				String parameter = AssembleUpmes.upRequestNews(usId,lon,lat);
				System.out.println("parameter:"+parameter);
				SocketGetData getData = new SocketGetData(); 
				String json = getData.getData(getApplicationContext(),Integer.parseInt(port), ip,operName, parameter);
				System.out.println("ServiceJson:"+json);
				
				if(!"���粻�ȶ������ݻ�ȡʧ��!".equals(json)&&!"������δ��Ӧ".equals(json)&&!
						"����Ϊ�գ���˶���������!".equals(json)&&!"��ȡ����ʧ��".equals(json)) {
					if("yes".equals(json)){
						//..............������������ʾ...................
						Intent CBJintent = new Intent();
						CBJintent.setAction("CBJActivityBroadcastReceiver");
						CBJintent.putExtra("json", json);
						sendBroadcast(CBJintent);
						
						//..................����������������ʾ.............
						Intent intent = new Intent();
						intent.setAction("ActualMissionNEWBroadcastReceiver");
						intent.putExtra("json", json);
						sendBroadcast(intent);
					}
				}
			}
		};
		timer.schedule(task, 5000, 40000);
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out
				.println("++++++++++++++++++++++++�̷߳����ѹر�+++++++++++++++++++++++++");
	}
	/*					ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	List<RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
	RunningTaskInfo cinfo = runningTasks.get(0);
	ComponentName component = cinfo.topActivity;					
	String actName = component.getClassName();
	Log.e("current activity is ", actName);
*/
}
