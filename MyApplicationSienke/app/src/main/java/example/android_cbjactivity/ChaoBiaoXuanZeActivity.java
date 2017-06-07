package example.android_cbjactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cc.thksoft.myfirstpro.entity.UsersInfo;
import cc.thksoft.myfirstpro.lvadapter.MeterDBAdapter;
import cc.thksoft.myfirstpro.myactivitymanager.MyActivityManager;
import cc.thksoft.myfirstpro.service.DBService;
import gitonway.niftydialogeffects.widget.niftydialogeffects.Effectstype;
import gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;

@SuppressLint("NewApi")
public class ChaoBiaoXuanZeActivity extends Activity {
	private ListView lv;
	private TextView chooseHead_tv;
	private List<String> list;
	private List<String> lists;
	private int signal;
	private String DBName;
	private List<UsersInfo> usersInfos;
	private DBService service;
	private MeterDBAdapter meterDBAdapter;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private String tempName;
	private NiftyDialogBuilder dialogBuilder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chaobiaoxuanze);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		chooseHead_tv = (TextView) findViewById(R.id.chooseHead_tv);
		final String filepath = Environment.getDataDirectory().getPath() + "/data/"+"com.example.android_cbjactivity"+"/databases/";
		lv = (ListView) findViewById(R.id.meter_db_lv);
		list = new ArrayList<String>();
		sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
		signal = sharedPreferences.getInt("signal", 0);
		if(signal==6){//�ļ�ѡ��
			File file = new File(filepath);
			String[] filename = file.list();
			if(filename!=null)
			for(String name:filename){
				System.out.println(name);
				if(name.contains("journal")){	
					System.out.println("����");
					File file1 = new File(filepath+name);
					file1.delete();
				}else{
					list.add(name);
				}
			}
			if(list==null||list.size()<=0){
				System.out.println("Ϊ��");
				list.add("û���ļ�");
			}
			meterDBAdapter = new MeterDBAdapter(this,list);
		}else if(signal==2){//��������
			DBName = getIntent().getStringExtra("DBName");
			service = new DBService(filepath+DBName);
			list = service.queryBookofUser();
			service.connclose();
			
			meterDBAdapter = new MeterDBAdapter(this,list);
		}else if(signal==4){//δ������
			DBName = getIntent().getStringExtra("DBName");
			service = new DBService(filepath+DBName);
			list = service.queryBookofUser();
			
			service.connclose();
			meterDBAdapter = new MeterDBAdapter(this,list);
		}else if(signal==10){//����ͳ��
			DBName = getIntent().getStringExtra("DBName");
			service = new DBService(filepath+DBName);
			list = service.queryBookofUser();
			service.connclose();
		
			meterDBAdapter = new MeterDBAdapter(this,list);
		}else if(signal==11){//�����ϴ��ļ�ѡ��
			File file = new File(filepath);
			String[] filename = file.list();
			if(filename!=null)
			for(String name:filename){
				System.out.println("fileName:"+name);
				if(name.contains("journal")){	
					System.out.println("����");
					File file1 = new File(filepath+name);
					file1.delete();
				}else{
					list.add(name);
				}
			}
			if(list==null||list.size()<=0){
				System.out.println("Ϊ��");
				list.add("û���ļ�");
			}
			meterDBAdapter = new MeterDBAdapter(this,list);
		}
		lv.setAdapter(meterDBAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, final int arg2,
					long arg3) {
				      
					   tempName = list.get(arg2);
					if(!"û���ļ�".equals(tempName)){
                    if(signal==6){
							if(!"û���ļ�".equals(tempName)){
								SharedPreferences.Editor editor = sharedPreferences.edit();
								editor.putString("dbName",  tempName);
								editor.commit();
								editor.remove("con_signal");
								editor.commit();
								editor.remove("con_position");
								editor.commit();	
								ChaoBiaoXuanZeActivity.this.finish();
							}
						}else if(signal==2){
								System.out.println("����ǰ��"+tempName);
								Intent intent = new Intent(ChaoBiaoXuanZeActivity.this,ShowListviewActivity.class);
								startActivity(intent);		
								
								editor = sharedPreferences.edit();
								editor.putString("bookName", tempName);
								editor.commit();
								ChaoBiaoXuanZeActivity.this.finish();
						}else if(signal==4){
								Intent intent = getIntent();
								intent.setClass(ChaoBiaoXuanZeActivity.this,ShowListviewActivity.class);
								//intent.putExtra("bookName", bookName);
								startActivity(intent);
								
								editor = sharedPreferences.edit();
								editor.putString("bookName", tempName);
								editor.commit();	
								ChaoBiaoXuanZeActivity.this.finish();
						}else if(signal==10){
								int resultCode = 1;
								setResult(resultCode, new Intent().setAction(tempName));
								editor = sharedPreferences.edit();
								editor.putString("bookName", tempName);
								editor.commit();
								ChaoBiaoXuanZeActivity.this.finish();
						}else if(signal==11){
							dialogBuilder = new NiftyDialogBuilder(ChaoBiaoXuanZeActivity.this,R.style.dialog_untran);
	        		        dialogBuilder
	                        .withTitle("�ϴ���")                                  
	                        .withTitleColor("#000000")                                  
	                        .withDividerColor("#999999")                             
	                        .withMessage(list.get(arg2))                     
	                        .withMessageColor("#000000")                               
	                        .isCancelableOnTouchOutside(true)                           
	                        .withDuration(700)                                          
	                        .withEffect(Effectstype.Flipv)
	                        .withButton1Text("ȷ��")                                      
	                        .withButton2Text("ȡ��")                                  
	                        .setButton1Click(new View.OnClickListener() {
	                            @Override
	                            public void onClick(View v) {
									int resultCode = 1;
									setResult(resultCode, new Intent().setAction(tempName));
			                    	dialogBuilder.dismiss();
			                    	ChaoBiaoXuanZeActivity.this.finish();
			                    }
			                })
			                .setButton2Click(new View.OnClickListener() {
			                    @Override
			                    public void onClick(View v) {
			                    	dialogBuilder.dismiss();
			                    }
			                })
			                .show();
					}
				}
			}
		});
	}
	
	public void onAction(View v) {
		
		int id=v.getId();
		
		switch (id) {
		case R.id.tv_xuanze_fanhui:
			ChaoBiaoXuanZeActivity.this.finish();
			break;
		default:
			break;
		}

	}
	
}
