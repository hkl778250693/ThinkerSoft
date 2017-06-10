package com.example.administrator.thinker_soft.myfirstpro.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Iterator;

public class GpsService extends Service {
	private static final String TAG="GpsService----";
	private MyBinder myBinder = new MyBinder();
	private LocationManager lm;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private Context context;
	private Handler handler;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return myBinder;
	}
	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("�����Ѿ�����");
		handler = new Handler();
		sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
		editor = sharedPreferences.edit();
		lm=(LocationManager)getSystemService(getApplicationContext().LOCATION_SERVICE);
		//�ж�GPS�Ƿ���������
		if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(getApplicationContext(), "�뿪��GPS����...", Toast.LENGTH_SHORT).show();
            System.out.println("GPSδ��");
            //���ؿ���GPS�������ý���
/*            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);   
            startActivityForResult(intent,0); */
            return;
        }
        
        //Ϊ��ȡ����λ����Ϣʱ���ò�ѯ����
        String bestProvider = lm.getBestProvider(getCriteria(), true);
        //�󶨼�������4������    
        //����1���豸����GPS_PROVIDER��NETWORK_PROVIDER����
        //����2��λ����Ϣ�������ڣ���λ����    
        //����3��λ�ñ仯��С���룺��λ�þ���仯������ֵʱ��������λ����Ϣ    
        //����4������    
        //��ע������2��3���������3��Ϊ0�����Բ���3Ϊ׼������3Ϊ0����ͨ��ʱ������ʱ���£�����Ϊ0������ʱˢ��   
        
        // 1�����һ�Σ�����Сλ�Ʊ仯����1�׸���һ�Σ�
        //ע�⣺�˴�����׼ȷ�ȷǳ��ͣ��Ƽ���service��������һ��Thread����run��sleep(10000);Ȼ��ִ��handler.sendMessage(),����λ��
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);	      
        //��ȡλ����Ϣ
        //��������ò�ѯҪ��getLastKnownLocation�������˵Ĳ���ΪLocationManager.GPS_PROVIDER
        Location location= lm.getLastKnownLocation(bestProvider);    
        while(location==null){
        	location= lm.getLastKnownLocation(bestProvider); 
        }
        updateView(location);
        //����״̬
        lm.addGpsStatusListener(listener);

	}
	@Override
	public void onDestroy() {
		lm.removeUpdates(locationListener);
		super.onDestroy();
	}
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}
	
	public class MyBinder extends Binder{
         public GpsService getService()
         {
             return GpsService.this;
         }
	}
	//λ�ü���
    private LocationListener locationListener=new LocationListener() {
        
        /**
         * λ����Ϣ�仯ʱ����
         */
        public void onLocationChanged(Location location) {
            updateView(location);
          //Log.i(TAG, "ʱ�䣺"+location.getTime()); 
            
            String sb = new String("");
            sb = sb + (location.getLatitude());
            sb = sb +("$");
            sb = sb +(location.getLongitude());
            editor.putString("sb", sb);
            editor.commit();
            System.out.println("SB�ģ�"+sb);
        }
        
        /**
         * GPS״̬�仯ʱ����
         */
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
            //GPS״̬Ϊ�ɼ�ʱ
            case LocationProvider.AVAILABLE:
                System.out.println("��ǰGPS״̬Ϊ�ɼ�״̬");
                break;
            //GPS״̬Ϊ��������ʱ
            case LocationProvider.OUT_OF_SERVICE:
            	System.out.println("��ǰGPS״̬Ϊ��������״̬");
                break;
            //GPS״̬Ϊ��ͣ����ʱ
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
            	System.out.println("��ǰGPS״̬Ϊ��ͣ����״̬");
                break;
            }
        }
    
        /**
         * GPS����ʱ����
         */
        public void onProviderEnabled(String provider) {
            Location location=lm.getLastKnownLocation(provider);
            updateView(location);
        }
    
        /**
         * GPS����ʱ����
         */
        public void onProviderDisabled(String provider) {
            updateView(null);
        }

    
    };
    
    //״̬����
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
            //��һ�ζ�λ
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                Log.i(TAG, "��һ�ζ�λ");
                break;
            //����״̬�ı�
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                //Log.i(TAG, "����״̬�ı�");
                //��ȡ��ǰ״̬
                GpsStatus gpsStatus=lm.getGpsStatus(null);
                //��ȡ���ǿ�����Ĭ�����ֵ
                int maxSatellites = gpsStatus.getMaxSatellites();
                //����һ�������������������� 
                Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                int count = 0;     
                while (iters.hasNext() && count <= maxSatellites) {     
                    GpsSatellite s = iters.next();     
                    count++;     
                }   
                System.out.println("��������"+count+"������");
                break;
            //��λ����
            case GpsStatus.GPS_EVENT_STARTED:
            	System.out.println("��λ����");
                break;
            //��λ����
            case GpsStatus.GPS_EVENT_STOPPED:
            	System.out.println("��λ����");
                break;
            }
        };
    };
    
    /**
     * ʵʱ�����ı�����
     * 
     * @param location
     */
    private void updateView(Location location){
        if(location!=null){
        	System.out.println("��һ�ζ�λ");
            String sb = new String("");
            sb = sb + (location.getLatitude());
            sb = sb +("$");
            sb = sb +(location.getLongitude());
            editor.putString("sb", sb);
            editor.commit();
        }else{
            //���EditText����
            //editText.getEditableText().clear();
        	System.out.println("�쳣");
        }
    }
    
    /**
     * ���ز�ѯ����
     * @return
*/
    private Criteria getCriteria(){
        Criteria criteria=new Criteria();
        //���ö�λ��ȷ�� Criteria.ACCURACY_COARSE�Ƚϴ��ԣ�Criteria.ACCURACY_FINE��ȽϾ�ϸ 
        criteria.setAccuracy(Criteria.ACCURACY_FINE);    
        //�����Ƿ�Ҫ���ٶ�
        criteria.setSpeedRequired(false);
        // �����Ƿ�������Ӫ���շ�  
        criteria.setCostAllowed(false);
        //�����Ƿ���Ҫ��λ��Ϣ
        criteria.setBearingRequired(false);
        //�����Ƿ���Ҫ������Ϣ
        criteria.setAltitudeRequired(false);
        // ���öԵ�Դ������  
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

}
