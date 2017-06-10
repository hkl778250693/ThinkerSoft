package com.example.administrator.thinker_soft.android_cbjactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.util.Mytoast;

import java.util.ArrayList;
import java.util.List;

import com.example.administrator.thinker_soft.myfirstpro.util.JaugeInternetState;

public class MapXiazhaiActivity extends Activity implements OnClickListener,
		MKOfflineMapListener {
	private String TAG = "MapXiazhaiActivity";
	private TextView bendi_city;// ���س�������
	private TextView xiazai_map;// ���ر��س��е�ͼ
	private LinearLayout set_back_Btn_map;
	private ListView localmaplistview;// �����س���
	private MKOfflineMap mOffline;
	private ArrayList<MKOLUpdateElement> localMapList = null;
	private List<String> bendiNameList = new ArrayList<String>();
	public SharedPreferences preferences;
	private LocalMapAdapter lAdapter = null;
	private String city;
	public Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lixianditu);
		mOffline = new MKOfflineMap();
		mOffline.init(this);
		init();
	}

	private void init() {
		preferences = getSharedPreferences("IP_PORT_DBNAME", 0);
		editor = preferences.edit();
		bendi_city = (TextView) findViewById(R.id.bendi_city);
		xiazai_map = (TextView) findViewById(R.id.xiazai_map);
		set_back_Btn_map = (LinearLayout) findViewById(R.id.set_back_Btn_map);
		xiazai_map.setOnClickListener(this);
		set_back_Btn_map.setOnClickListener(this);
		city = preferences.getString("City", "");
		bendi_city.setText("��ǰλ�ã�" + city);
		localmaplistview = (ListView) findViewById(R.id.localmaplistview);

		// ��ȡ���¹������ߵ�ͼ��Ϣ
		localMapList = mOffline.getAllUpdateInfo();
		if (localMapList == null) {
			localMapList = new ArrayList<MKOLUpdateElement>();
		}
		lAdapter = new LocalMapAdapter();
		localmaplistview.setAdapter(lAdapter);
		if (localMapList.size() != 0) {
			for (int i = 0; i < localMapList.size(); i++) {
				bendiNameList.add(localMapList.get(i).cityName);
			}

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.xiazai_map:// ��Ϊ���ߵ�ͼ�Ƚϴ���ʾ��Ҫ��WIFI�²�������
			localMapList = mOffline.getAllUpdateInfo();
			if (localMapList != null && localMapList.size() != 0) {
				for (int i = 0; i < localMapList.size(); i++) {
					bendiNameList.add(localMapList.get(i).cityName);
				}

			}
			ArrayList<MKOLSearchRecord> records = mOffline.searchCity(city);
			if (records == null || records.size() != 1)
				return;
			int cityID = Integer// �õ����س��е�ID�����ص�ͼ
					.parseInt(String.valueOf(records.get(0).cityID));
	
			Log.e(TAG, "���ݰ���С=" + records.get(0).size);
			boolean isXZ = true;// �Ƿ���Ҫ����
			Log.i(TAG, "bendiNameList��С=" + bendiNameList.size());
			if (bendiNameList != null && bendiNameList.size() != 0) {
				for (int i = 0; i < bendiNameList.size(); i++) {
					Log.e(TAG, "city=" + city);
					Log.e(TAG, "(bendiNameList.get(i)=" + i + "  "
							+ (bendiNameList.get(i)));
					if (bendiNameList.get(i).toString().trim().endsWith(city)) {
						Mytoast.showToast(this, city + "��ͼ������", 1000);
						isXZ = false;
						return;
					}
				}
			}
			Log.i(TAG, "isXZ=" + isXZ);
			if (isXZ == true) {
				if (JaugeInternetState.isWifi(getApplicationContext()) == false) {
					Mytoast.showToast(this, "�������ݽϴ�����WIFI���������ص�ͼ", 1000);
				} else {
					Log.i(TAG, "��ʼ����");
					mOffline.start(cityID);
					updateView();
				}
			}

			break;
		case R.id.set_back_Btn_map:
			finish();
			break;

		default:
			break;
		}

	}

	/**
	 * ���ߵ�ͼ�����б�������
	 */
	public class LocalMapAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return localMapList.size();
		}

		@Override
		public Object getItem(int index) {
			return localMapList.get(index);
		}

		@Override
		public long getItemId(int index) {
			return index;
		}

		@Override
		public View getView(int index, View view, ViewGroup arg2) {
			MKOLUpdateElement e = (MKOLUpdateElement) getItem(index);
			view = View.inflate(MapXiazhaiActivity.this,
					R.layout.lixianditu_item, null);
			initViewItem(view, e);
			return view;
		}

		void initViewItem(View view, final MKOLUpdateElement e) {
			Button display = (Button) view.findViewById(R.id.display);
			Button remove = (Button) view.findViewById(R.id.remove);
			TextView title = (TextView) view.findViewById(R.id.title);
			TextView update = (TextView) view.findViewById(R.id.update);
			TextView ratio = (TextView) view.findViewById(R.id.ratio);
			ratio.setText(e.ratio + "%");
			title.setText(e.cityName);
			if (e.update) {
				update.setText("�ɸ���");// ���ɸ��µ�ʱ�򣬵���е���¼�,ɾ������������
				update.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mOffline.remove(e.cityID);
						mOffline.start(e.cityID);
						updateView();
					}
				});
			} else {
				update.setText("����");
			}
			if (e.ratio != 100) {
				display.setEnabled(false);
			} else {
				display.setEnabled(true);
				editor.putString("MAP", e.cityName + "  ��ͼ������");
				editor.commit();
			}
			remove.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mOffline.remove(e.cityID);
					updateView();
					editor.putString("MAP", "���ص�ͼ����");
					editor.commit();
				}
			});
			display.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra("x", e.geoPt.longitude);
					intent.putExtra("y", e.geoPt.latitude);
					intent.setClass(MapXiazhaiActivity.this, BaseMapDemo.class);
					startActivity(intent);
				}
			});
		}
	}

	/**
	 * ����״̬��ʾ
	 */
	public void updateView() {
		localMapList = mOffline.getAllUpdateInfo();
		if (localMapList == null) {
			localMapList = new ArrayList<MKOLUpdateElement>();
		}
		lAdapter.notifyDataSetChanged();
	}

	@Override
	public void onGetOfflineMapState(int type, int state) {
		switch (type) {
		case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
			MKOLUpdateElement update = mOffline.getUpdateInfo(state);
			// �������ؽ��ȸ�����ʾ
			if (update != null) {
				updateView();
			}
		}
			break;
		case MKOfflineMap.TYPE_NEW_OFFLINE:
			// �������ߵ�ͼ��װ
			Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
			break;
		case MKOfflineMap.TYPE_VER_UPDATE:
			// �汾������ʾ
			// MKOLUpdateElement e = mOffline.getUpdateInfo(state);

			break;
		}

	}
}
