package com.example.administrator.thinker_soft.android_cbjactivity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;

/**
 * ��ʾMapView�Ļ����÷�
 */
public class BaseMapDemo extends Activity {
	@SuppressWarnings("unused")
	private static final String LTAG = BaseMapDemo.class.getSimpleName();
	private MapView mMapView;
	private BaiduMap mBaiduMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Intent intent = getIntent();
		if (intent.hasExtra("x") && intent.hasExtra("y")) {
			// ����intent����ʱ���������ĵ�Ϊָ����
			Bundle b = intent.getExtras();
			LatLng p = new LatLng(b.getDouble("y"), b.getDouble("x"));
			mMapView = new MapView(this,
					new BaiduMapOptions().mapStatus(new MapStatus.Builder()
							.target(p).build()));
		} else {
			mMapView = new MapView(this, new BaiduMapOptions());
		}
		setContentView(mMapView);
		mBaiduMap = mMapView.getMap();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// activity ��ͣʱͬʱ��ͣ��ͼ�ؼ�
		mMapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// activity �ָ�ʱͬʱ�ָ���ͼ�ؼ�
		mMapView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity ����ʱͬʱ���ٵ�ͼ�ؼ�
		mMapView.onDestroy();
	}

}
