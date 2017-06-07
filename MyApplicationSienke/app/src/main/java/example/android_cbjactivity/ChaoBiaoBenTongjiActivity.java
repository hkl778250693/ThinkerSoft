package example.android_cbjactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

import java.util.List;

import cc.thksoft.myfirstpro.myactivitymanager.MyActivityManager;
import cc.thksoft.myfirstpro.service.DBService;

public class ChaoBiaoBenTongjiActivity extends Activity {
	
	private LinearLayout mImageView;
	private TextView allid,yichao,weichao,caobiaoliang,gailu;
	private Button tonhjisuoyou,chaobiaotongji;
	private String filepath;
	private String DBName;
	private String temp;
	private DBService service;
	private List<String> usList;
	private List<String> bookName;
	private int totaluser,meterdone,meterno,meterCount;
	private double completrate;
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chaobiaotongji);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		handler = new Handler();
		mImageView=(LinearLayout) findViewById(R.id.tongjiButton);
		allid = (TextView) findViewById(R.id.allid);
		yichao = (TextView) findViewById(R.id.yichao);
		weichao = (TextView) findViewById(R.id.weichao);
		caobiaoliang = (TextView) findViewById(R.id.caobiaoliang);
		gailu = (TextView) findViewById(R.id.gailu);
		tonhjisuoyou = (Button) findViewById(R.id.tonhjisuoyou);
		chaobiaotongji = (Button) findViewById(R.id.chaobiaotongji);
		
		
		SharedPreferences sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
		DBName = sharedPreferences.getString("dbName", "");
		if(DBName!=null&&!"".equals(DBName)){
			System.out.println("ѡ������ݿ⣺"+DBName);
			chaobiaotongji.setEnabled(true);
			tonhjisuoyou.setEnabled(true);
			filepath = Environment.getDataDirectory().getPath() + "/data/"+"com.example.android_cbjactivity"+"/databases/";
		}else{
			chaobiaotongji.setEnabled(false);
			tonhjisuoyou.setEnabled(false);
			Toast.makeText(getApplicationContext(), "�������ҳ�ļ�ѡ��", Toast.LENGTH_SHORT).show();
		}		
	}
	public void onAction(View v) {
		int id=v.getId();
		switch (id) {
		case R.id.tongjiButton:
			finish();
			break;
		case R.id.tonhjisuoyou:
			System.out.println("����");
			new Thread(){
				@Override
				public void run() {
					super.run();
					System.out.println("�����߳�");
					service = new DBService(filepath+DBName);
					usList = service.querystatisticsinfos();
					System.out.println("���ؽ��������"+usList.get(0));
					System.out.println("���ؽ��������"+usList.get(1));
					System.out.println("���ؽ��������"+usList.get(2));
					System.out.println("���صĲ�ѯ���������"+usList.size());
					service.connclose();
					System.out.println(usList.get(0));
					if("0".equals(usList.get(0))&&"0".equals(usList.get(1))&&"0".equals(usList.get(2))){					
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(ChaoBiaoBenTongjiActivity.this, "û�п�������", Toast.LENGTH_LONG).show();
							}
						});
					}
					else{
						Adddata();
					}
				}
			}.start();
			break;
		case R.id.chaobiaotongji:
			SharedPreferences sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putInt("signal", 10);
			editor.commit();
			Intent intent = new Intent(ChaoBiaoBenTongjiActivity.this,ChaoBiaoXuanZeActivity.class);
			intent.putExtra("DBName", DBName);
			int requestCode = 1;
			startActivityForResult(intent, requestCode);
			/*Thread thread = new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					System.out.println("���볭��ͳ���߳�");
					service = new DBService(filepath+DBName);
					bookName = service.queryBookofUser();	
					service.connclose();
				}
			};
			thread.start();
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String[] array = (String[])bookName.toArray(new String[bookName.size()]);
			if(array.length<=0){
				array = new String[]{"û���ļ�"};
			}
			if(array!=null){
			new AlertDialog.Builder(ChaoBiaoBenTongjiActivity.this)
			.setTitle("�����ļ�ѡ��")
			.setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					final String name = bookName.get(which);
					Thread thread = new Thread(){
						@Override
						public void run() {
							super.run();
							service = new DBService(filepath+DBName);
							usList = service.querystatisticsinfosbybook(name);
							service.connclose();
							if("0".equals(usList.get(0))&&"0".equals(usList.get(1))&&"0".equals(usList.get(2))){					
								handler.post(new Runnable() {
									
									@Override
									public void run() {
										Toast.makeText(ChaoBiaoBenTongjiActivity.this, "û�п�������", Toast.LENGTH_LONG).show();
									}
								});							
							}else{
								Adddata();
							}
						}
					};
					thread.start();
					try {
						thread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					dialog.dismiss();					
				}
			})
			.setNegativeButton("ȡ��", null) 
			.show();
			} */
			break;			
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==resultCode){
			String tempname = data.getAction();
			service = new DBService(filepath+DBName);
			usList = service.querystatisticsinfosbybook(tempname);
			if("0".equals(usList.get(0))&&"0".equals(usList.get(1))&&"0".equals(usList.get(2))){					
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(ChaoBiaoBenTongjiActivity.this, "û�п�������", Toast.LENGTH_LONG).show();
					}
				});							
			}else{
				Adddata();
			}
		}
	}

	public void Adddata(){
		System.out.println("�������");
		totaluser = Integer.parseInt(usList.get(0));
		meterdone = Integer.parseInt(usList.get(1));
		if(usList.get(2)==null||"".equals(usList.get(2))){
			meterCount = Integer.parseInt("0");
		}else
		meterCount = Integer.parseInt(usList.get(2));
		
		meterno = totaluser - meterdone;
		if(totaluser!=0){
			completrate = (double)meterdone/(double)totaluser;
			completrate = Double.parseDouble(String.format("%.4f", completrate));
			System.out.println("completrate:"+completrate);
			temp = String.valueOf(completrate*100);
			int loc = temp.indexOf(".");
			if(temp.length()>loc+3){
				temp = temp.substring(0, loc+3);
			}else if(temp.length() ==loc+2){
				temp = temp.substring(0, loc+2);
				temp = temp +"0";
			}else if(temp.length() ==loc+1){
				temp = temp.substring(0, loc+1);
				temp = temp +"00";
			}else if(temp.length() ==loc){
				temp = temp.substring(0, loc);
				temp = temp +".00";
			}
		}
		handler.post(new Runnable() {
			@Override
			public void run() {
				allid.setText(String.valueOf(totaluser));
				yichao.setText(String.valueOf(meterdone));
				weichao.setText(String.valueOf(meterno));
				caobiaoliang.setText(String.valueOf(meterCount));
				gailu.setText(temp+"%");	
			}
		});

	}
}
