package com.example.administrator.thinker_soft.myfirstpro.util;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.List;

public class JaugeInternetState {
	
public static boolean isNetworkAvailable(Context context) {   
    ConnectivityManager cm = (ConnectivityManager) context   
            .getSystemService(Context.CONNECTIVITY_SERVICE);   
    if (cm == null) {   
    } else {
/*������������������������ж���������
������������  �����ʹ��
*/        	//cm.getActiveNetworkInfo().isAvailable();  
        NetworkInfo[] info = cm.getAllNetworkInfo();   
        if (info != null) {   
            for (int i = 0; i < info.length; i++) {   
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {   
                    return true;   
                }   
            }   
        }   
    }   
    return false;   
} 
/**
 * �ж�GPS�Ƿ��
 * @param context
 * @return
 */
 public static boolean isGpsEnabled(Context context) {   
        LocationManager lm = ((LocationManager) context   
                .getSystemService(Context.LOCATION_SERVICE));   
        List<String> accessibleProviders = lm.getProviders(true);   
        return accessibleProviders != null && accessibleProviders.size() > 0;   
    } 
/**
 * ��GPS
 * @param context
 */
 public static boolean openGPSSettings(Context context) {      
	 boolean sign = false;
     //��ȡGPS���ڵ�״̬���򿪻��ǹر�״̬��
   boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled( context.getContentResolver(), LocationManager.GPS_PROVIDER );

   if(gpsEnabled)
   {
	   sign = true;
	   System.out.println("��GPS");
	   //��GPS  www.2cto.com
	    Settings.Secure.setLocationProviderEnabled( context.getContentResolver(), LocationManager.GPS_PROVIDER, true);
   }
   return sign;
  }
   /**
    * �ر�GPS
    * @param context
    */
 public static boolean closeGPSSettings(Context context) { 
	 	boolean sign = false;
	     //��ȡGPS���ڵ�״̬���򿪻��ǹر�״̬��
	   boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled( context.getContentResolver(), LocationManager.GPS_PROVIDER );

	   if(!gpsEnabled)
	   {
		   sign = true;
		System.out.println("�ر�GPS");
	   //�ر�GPS
	    Settings.Secure.setLocationProviderEnabled( context.getContentResolver(), LocationManager.GPS_PROVIDER, false );
	   }
	   return sign;
   }
 /**
  * �ж�WIFI�Ƿ��
  * @param context
  * @return
  */
 public static boolean isWifiEnabled(Context context) {   
     ConnectivityManager mgrConn = (ConnectivityManager) context   
             .getSystemService(Context.CONNECTIVITY_SERVICE);   
     TelephonyManager mgrTel = (TelephonyManager) context   
             .getSystemService(Context.TELEPHONY_SERVICE);   
     return ((mgrConn.getActiveNetworkInfo() != null && mgrConn   
             .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel   
             .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);   
 } 
/**
 * �ж�gprs�Ƿ��
 * @param context
 * @return
 */
	public static boolean isGprs(Context context)
	{
			ConnectivityManager connectivity=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			if(connectivity!=null)
			{
				NetworkInfo activeNetInfo=connectivity.getActiveNetworkInfo();
				if(activeNetInfo.getState()==NetworkInfo.State.CONNECTED);
				{	
					if(activeNetInfo.getType()==ConnectivityManager.TYPE_MOBILE)
					{  
						return true;
					}
				}
			}
			return false;
	}
/**
 * �ж��Ƿ���3G����
 * @param context
 * @return
 */
 public static boolean is3rd(Context context) {   
     ConnectivityManager cm = (ConnectivityManager) context   
             .getSystemService(Context.CONNECTIVITY_SERVICE);   
     NetworkInfo networkINfo = cm.getActiveNetworkInfo();   
     if (networkINfo != null   
             && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {   
         return true;   
     }   
     return false;   
 }  
 /**
  * �ж���wifi����3g����,�û����������������ˣ�wifi�Ϳ��Խ������ػ������߲��š�
  * @param context
  * @return
  */
 public static boolean isWifi(Context context) {   
     ConnectivityManager cm = (ConnectivityManager) context   
             .getSystemService(Context.CONNECTIVITY_SERVICE);   
     NetworkInfo networkINfo = cm.getActiveNetworkInfo();   
     if (networkINfo != null   
             && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {   
         return true;   
     }   
     return false;   
 }
 
 
}
