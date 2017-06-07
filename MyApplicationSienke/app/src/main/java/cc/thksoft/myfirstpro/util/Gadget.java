package cc.thksoft.myfirstpro.util;

import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.security.MessageDigest;

/**
 * �˳���ǰActivity������
 * @author qinwei
 *
 */
public class Gadget 
{
	/**
	 * ���÷����˳���ǰActivity
	 * @param KeyCode
	 */
	public static void simulateKey(final int KeyCode) 
	{
		   new Thread() {
		         public void run() {
		             try 
		             {
		                  Instrumentation inst = new Instrumentation(); 
		                  inst.sendKeyDownUpSync(KeyCode);    
		             } catch (Exception e) {
		            }   
		        }  
		  }.start(); 
	}
	/**
	 * ��������Ƿ����
	 * @param context
	 * @return 1��������wifi.2��������gprs.3����û������
	 */
	public static int isConnect(Context context)
	{
			ConnectivityManager connectivity=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			if(connectivity!=null)
			{
				NetworkInfo activeNetInfo=connectivity.getActiveNetworkInfo();
				
				if(activeNetInfo!=null&&activeNetInfo.isConnected())
				{
					if(activeNetInfo.getState()==NetworkInfo.State.CONNECTED);
					{	
						if(activeNetInfo.getType()==ConnectivityManager.TYPE_WIFI)
						{  
							return 1;
						}
						else if(activeNetInfo.getType()==ConnectivityManager.TYPE_MOBILE)
						{  
							return 2;
						}
					}
				}
			}
			return 3;
	}
	/**
	 * MD5����
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String EncoderByMd5(String str) throws Exception 
	{
        MessageDigest md5=MessageDigest.getInstance("md5");//����ʵ��ָ��ժҪ�㷨�� MessageDigest ����
        md5.update(str.getBytes());//�Ƚ��ַ���ת����byte���飬����byte �������ժҪ
        byte[] nStr = md5.digest();//��ϣ���㣬������
        return bytes2Hex(nStr);//���ܵĽ����byte���飬��byte����ת�����ַ���
    }
	/**
	 * Byte����ת�����ַ���
	 * @param bts
	 * @return
	 */
	private static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    } 
	/**
	 * ����IP�˿ں�
	 * @param context
	 * @param ip
	 * @param port
	 * @return
	 */
	public static boolean saveSettingsIP(Context context,String ip,int port)
	{
		SharedPreferences sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putString("ip", ip);
		editor.putInt("port", port);
		boolean result=editor.commit();
		return result;
	}
	/**
	 * �����¼�û�ID
	 * @param context
	 * @param id
	 */
	public static void  saveSystemUserId(Context context,String id)
	{
		SharedPreferences sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putString("id", id);
		editor.commit();
	}
	/**
	 * SDCard�Ƿ����
	 * @return
	 */
	public static boolean isSDCardExist(){
		if(android.os.Environment.getExternalStorageState().equals(  
			    android.os.Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * �������
	 * 
	 * @param mEditText�����
	 * @param mContext������
	 */
	public static void openKeybord(EditText mEditText, Context mContext)
	{
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/**
	 * �ر������
	 * 
	 * @param mEditText�����
	 * @param mContext������
	 */
	public static void closeKeybord(EditText mEditText, Context mContext)
	{
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
	}
}
