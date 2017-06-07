package example.android_cbjactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cc.thksoft.myfirstpro.entity.AreaInfo;
import cc.thksoft.myfirstpro.entity.BookInfo;
import cc.thksoft.myfirstpro.entity.UsersInfo;
import cc.thksoft.myfirstpro.myactivitymanager.MyActivityManager;
import cc.thksoft.myfirstpro.service.DBService;
import cc.thksoft.myfirstpro.threadsocket.SocketInteraction;
import cc.thksoft.myfirstpro.util.AssembleUpmes;
import cc.thksoft.myfirstpro.util.JaugeInternetState;
import cc.thksoft.myfirstpro.util.JsonAnalyze;
import cc.thksoft.myfirstpro.util.MyDialog;
import cc.thksoft.myfirstpro.util.Mytoast;

public class SJCSActivity extends Activity {
	private List<BookInfo>  bookList;
	private List<AreaInfo> areaList;
	private Dialog upDialog ;
	private Dialog downDialog;
	private String tt="p";
	private Button data_down,data_up;
	private SharedPreferences sharedPreferences;
	//.............................
	//����ΪSoecket��������
	private String ip;
	private String defaultPort;
	private String operName = "NANBU";
	
	private Boolean signal = false;
	
	//�ϴ�
	private DBService dbService;
	private String dbName;
	private final String filepath = Environment.getDataDirectory().getPath() + "/data/"+"com.example.android_cbjactivity"+"/databases/";
	private int upCount;
	private int minusCount;
	private Map<Integer,Boolean> dialogControl;
	private int clickCount;
	private static Handler dataHandler;
	private MyActivityManager mam;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sjcs);
		mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		data_down = (Button) findViewById(R.id.data_down);
		data_up = (Button) findViewById(R.id.data_up);
		sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
		dialogControl = new HashMap<Integer, Boolean>();	
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.v("onSaveInstanceState:", "onSaveInstanceState()");
	}
	@Override
	protected void onStart() {
		super.onStart();

	}
	@Override
	protected void onPause() {
		super.onStop();
		data_down.setEnabled(false);
		data_up.setEnabled(false);		
	}
	@Override
	protected void onResume() {
		super.onResume();
		data_down.setEnabled(true);
		data_up.setEnabled(true);
		ip = sharedPreferences.getString("ip", "");
		defaultPort = sharedPreferences.getString("port", "");
		if("".equals(ip)||ip==null){
			Toast.makeText(SJCSActivity.this, "������IP", Toast.LENGTH_LONG).show();		
		}else if("".equals(defaultPort)||defaultPort==null){
			Toast.makeText(SJCSActivity.this, "�����ö˿�", Toast.LENGTH_LONG).show();			
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	public void onAction(View v) {
		int id=v.getId();
		Intent intent=new Intent();
		switch (id) {
		//����
		case R.id.data_down:
			clickCount ++;
			dialogControl.put(clickCount, true);
			downDialog = MyDialog.createLoadingDialog(SJCSActivity.this, "���ݳ�ʼ����...");
			downDialog.setCancelable(true);
			downDialog.setCanceledOnTouchOutside(false);
			downDialog.setOnDismissListener(new OnDismissListener() {			
				@Override
				public void onDismiss(DialogInterface dialog) {
					dialogControl.put(clickCount, false);
				}
			});	
			if(!JaugeInternetState.isWifiEnabled(getApplicationContext())){
				Toast.makeText(this, "�����������WIFI", Toast.LENGTH_SHORT).show();
				break;
			}
			ip = sharedPreferences.getString("ip", "");
			defaultPort = sharedPreferences.getString("port", "");
			if("".equals(ip)||"".equals(defaultPort)){
				Mytoast.showToast(SJCSActivity.this, "��⵽��δ����IP��PORT", Toast.LENGTH_SHORT);
				break;
			}
			downDialog.show();
			
			//��ȡ�����ͷ�������
			DownLoadData();
			//��ת
/*			intent.putExtra("areaList", (Serializable)areaList);
			intent.putExtra("bookList", (Serializable)bookList);
			intent.putExtra("floatRange", floatRange);
			intent.putExtra("month", month);
			intent.setClass(SJCSActivity.this, XiaZaiActivity.class);
			startActivity(intent);*/
			break;
		//�ϴ�
		case R.id.data_up:
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putInt("signal", 11);
			editor.commit();
			intent = intent.setClass(SJCSActivity.this,ChaoBiaoXuanZeActivity.class);
			int requestCode = 1;
			startActivityForResult(intent, requestCode);
			break;
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);	
		if(data!=null)
			dbName = data.getAction();
		final Handler dataHandler = new Handler();
		if(requestCode==resultCode&&!"".equals(dbName)){
			if(!"û���ļ�".equals(dbName)&&!"".equals(dbName)){
				if(!JaugeInternetState.isWifiEnabled(getApplicationContext())){
					Toast.makeText(this, "�����������WIFI", Toast.LENGTH_SHORT).show();
					return;
				}
				upDialog = MyDialog.createLoadingDialog(SJCSActivity.this, "�����ϴ�...");
				upDialog.setCancelable(false);
				upDialog.setCanceledOnTouchOutside(false);
				upDialog.show();
				new Thread(){
					public void run() {
						dbService = new DBService(filepath+dbName);
						List<UsersInfo> usersInfos = dbService.queryAllUsersInfo();
						final int totalcount = usersInfos.size();
						upCount = 0;
						minusCount = 0;
					   
						if(totalcount<=0){
							dataHandler.post(new Runnable() {
								
								@SuppressLint("NewApi") @Override
								public void run() {
									Toast.makeText(SJCSActivity.this, "�Բ��� û�п��ϴ�����", Toast.LENGTH_LONG).show();
									upDialog.dismiss();
								}
							});
							return;
						}
						int length = 50;
						int totalnum = totalcount/50;
						if(totalcount%50!=0){
							totalnum =totalnum +1;
						}
						List<UsersInfo> infos = null;
						for (int i = 0; i <totalnum; i++) {
							if(i!=totalnum-1){
								infos = usersInfos.subList(length*i, length*(i+1));
							}else{
								infos = usersInfos.subList(length*i, totalcount);	
							}
							//��һ�� �޸�����Ҫ�ϴ������ݱ�־Ϊ"1" ���ϴ�
							dbService = new DBService(filepath+dbName);
							dbService.modifytheStateofUsersyes(infos);	
							String parameterBag = AssembleUpmes.upLoadParameter(infos);
							SocketInteraction interaction = new SocketInteraction(getApplicationContext(), Integer.parseInt(defaultPort),ip,operName,parameterBag,dataHandler);
							String result = interaction.DataUpLoadConn();
							interaction.closeConn();
							System.out.println("result:"+result);
							if(!"���粻�ȶ������ݻ�ȡʧ��!".equals(result)
									&&!"������δ��Ӧ".equals(result)
									&&!"����Ϊ�գ���˶���������!".equals(result)
									&&!"���Ĺؼ�������".equals(result)&&!"".equals(result)){
								if(!"�ɹ�".equals(result)){
									result = result.substring(1, result.length()-1);
									String[] usIDS = result.split(",");
									//�ڶ��� ������δ��ȫ���ϴ��ɹ����޸�δ�ܳɹ��ϴ����û���ע Ϊδ�ϴ�
									dbService = new DBService(filepath+dbName);
									dbService.modifytheStateArrayofUserno(usIDS);
									upCount = upCount +(infos.size()-usIDS.length);
									System.out.println("+++++++++++++++++++++=���result��"+result);
									for (int j = 0; j < usIDS.length; j++) {	
										System.out.println("+++++++++++++++++++++=���usIDS["+j+"]��"+usIDS[j]);
									}
									tt="p";
								}else{
									//�ڶ��� ��ȫ���ϴ��ɹ����޸������û���ע Ϊ���ϴ�
									upCount = upCount +infos.size();
									tt="p";
/*									dbService = new DBService(filepath+dbName);
									dbService.modifytheStateofUsersyes(infos);*/
								}
							}else{
								//ȫ��δ�ɹ� ���עȫ������Ϊδ�ϴ�
								dbService = new DBService(filepath+dbName);
								dbService.modifytheStateofUsersno(infos);
								tt="s";
							}
/*							for (int j = 0; j < infos.size(); j++) {							
								int dosage =  Integer.parseInt(infos.get(j).getTHISMONTH_DOSAGE());
								if(dosage<0){	
									minusCount++;
								}
							}*/
						}
						dataHandler.post(new Runnable() {
							@SuppressLint("NewApi") @Override
							public void run() {
								AlertDialog.Builder builder = new AlertDialog.Builder(SJCSActivity.this);
								if(tt.equals("s")){
									
									builder.setMessage("�����쳣���������ϴ���");
								}else{
								//"���γɹ��ϴ� "+(upCount)+" ��\nʧ���ϴ� "+(totalcount-upCount)+" ��\n��ˮ����(�����û�) "+minusCount+" ��"
								builder.setMessage("���γɹ��ϴ� "+(upCount)+" ��\nʧ���ϴ� "+(totalcount-upCount)+" ��");
								}
								builder.show();
								upDialog.dismiss();
								
							}
						});
					};
				}.start();
			}
		}
	}
	private void DownLoadData() {
		final MyHandler dataHandler  = new MyHandler();
		new Thread(){
			@Override
			public void run() {
				super.run();
				int loc = clickCount;
				boolean singal = false;
				String parameter = AssembleUpmes.initialParameter();
				SocketInteraction areaSocket= new SocketInteraction(getApplicationContext(),Integer.parseInt(defaultPort),ip,operName,parameter,dataHandler);
				singal = areaSocket.DataDownLoadConn();
				areaSocket.closeConn();
				if(dialogControl.size()>0){
					Log.v("ZHOUTAO","��ǰ�߳���ţ�"+loc);
					if(dialogControl.get(loc)==true){
						if(singal==true){
							dataHandler.post(new Runnable() {
								
								@Override
								public void run() {
									Toast.makeText(SJCSActivity.this, "��ʼ���ݳɹ�", Toast.LENGTH_LONG).show();
									downDialog.dismiss();
								}
							});
						}else{
							dataHandler.post(new Runnable() {
								
								@Override
								public void run() {
									Toast.makeText(SJCSActivity.this, "��ʼ����ʧ��", Toast.LENGTH_LONG).show();
									downDialog.dismiss();	
								}
							});
						}
					}
				}
			}
		}.start();	
	}
	

    class MyHandler extends Handler {
    	/**
    	 * ��ȡsocket�߳�����
    	 */
    	@SuppressLint("NewApi") @Override
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		int loc = clickCount;//��ǰ�̵߳����
    		Bundle bundle = msg.getData();
    		String data = bundle.getString("data");
    		String temp[] = data.split("��");
			if(dialogControl.size()>0){
				Log.v("ZHOUTAO","��ǰ�߳���ţ�"+loc);
				if(dialogControl.get(loc)==true){
		    		for(int i=0;i<temp.length;i++){
		    			if(!"û������".equals(data)){
							if(temp[i].contains("AreaData")){
								areaList = JsonAnalyze.analyszeArea(temp[i]);
							}else if(temp[i].contains("BookData")){
								bookList = JsonAnalyze.analyszeBook(temp[i]);
							}											
						}
		    		}
					if(areaList!=null&&bookList!=null){
						downDialog.dismiss();
						Intent intent=new Intent();
						intent.putExtra("areaList", (Serializable)areaList);
						intent.putExtra("bookList", (Serializable)bookList);
						intent.setClass(SJCSActivity.this, XiaZaiActivity.class);
						startActivity(intent);
					}else if(areaList==null&&bookList==null){
						AlertDialog.Builder builder = null;
						builder = new AlertDialog.Builder(SJCSActivity.this);
						builder.setMessage("���ݳ�ʼʧ��");
						builder.show();
						downDialog.dismiss();
						return;
					}
				}
			}
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
