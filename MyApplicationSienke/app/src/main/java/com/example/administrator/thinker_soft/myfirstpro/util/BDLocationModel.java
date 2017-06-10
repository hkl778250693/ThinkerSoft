package com.example.administrator.thinker_soft.myfirstpro.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class BDLocationModel {
	private LocationClient mLocationClient;
	private BDLocationListener myListener;
	private Context context;
	private Activity activity;
	private Handler handler;

	/*
	 * public BDLocationModel(Context context){ this.context = context;
	 * System.out.println("�����Ѿ�������"); mLocationClient = new
	 * LocationClient(context); //����LocationClient�� myListener = new
	 * MyLocationListener(); LocationClientOption option = new
	 * LocationClientOption();
	 * option.setLocationMode(LocationMode.Hight_Accuracy);//���ö�λģʽ
	 * option.setOpenGps(true);
	 * option.setCoorType("bd09ll");//���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
	 * option.setScanSpan(5000);//���÷���λ����ļ��ʱ��Ϊ5000ms
	 * option.setIsNeedAddress(true);//���صĶ�λ���������ַ��Ϣ
	 * option.setNeedDeviceDirect(true);//���صĶ�λ��������ֻ���ͷ�ķ���
	 * mLocationClient.setLocOption(option);
	 * mLocationClient.registerLocationListener( myListener ); //ע��������� }
	 */

	public BDLocationModel(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
		System.out.println("�����Ѿ�������");
		mLocationClient = new LocationClient(context); // ����LocationClient��
		myListener = new MyLocationListener();
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ���ö�λģʽ
		option.setOpenGps(true);
		option.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
		option.setScanSpan(5000);// ���÷���λ����ļ��ʱ��Ϊ5000ms
		option.setIsNeedAddress(true);// ���صĶ�λ���������ַ��Ϣ
		option.setNeedDeviceDirect(true);// ���صĶ�λ��������ֻ���ͷ�ķ���
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(myListener); // ע���������
	}

	public void startLoaction() {
		mLocationClient.start();
	}

	public boolean isstartLoaction() {
		return mLocationClient.isStarted();
	}

	public void stopLocation() {
		mLocationClient.stop();
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"IP_PORT_DBNAME", 0);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			if (location == null) {
				return;
			}

			StringBuffer sb = new StringBuffer(100);
			if (location.getLatitude() != 4.9E-324
					&& location.getLongitude() != 4.9E-324) {
				sb.append(location.getLatitude());
				sb.append("$");
				sb.append(location.getLongitude());
				if ("".equals(sharedPreferences.getString("sb", ""))) {
					editor.putString("sb", sb.toString());
				}
				if (!"".equals(sharedPreferences.getString("sb", ""))
						&& !sb.toString().equals(
								sharedPreferences.getString("sb", ""))) {
					editor.putString("sb", sb.toString());
				}
				editor.putString("City", String.valueOf(location.getCity()));
				editor.putString("Addr", String.valueOf(location.getAddrStr()));
				editor.commit();

				if (handler != null) {
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putString("key", "0");
					data.putDouble("Latitude", location.getLatitude());
					data.putDouble("Longitude", location.getLongitude());
					data.putFloat("Direction", location.getDirection());
					data.putFloat("Radius", location.getRadius());
					data.putString("Address", location.getAddrStr());
					msg.setData(data);
					handler.sendMessage(msg);
				}
				
			} else {
				System.out.println("��λʧ��");
			}
		}
	}

}
