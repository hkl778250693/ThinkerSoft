package example.android_cbjactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

import java.io.Serializable;
import java.util.List;

import cc.thksoft.myfirstpro.entity.UsersInfo;
import cc.thksoft.myfirstpro.lvadapter.BookNumberAdapter;
import cc.thksoft.myfirstpro.myactivitymanager.MyActivityManager;
import cc.thksoft.myfirstpro.service.DBService;
import cc.thksoft.myfirstpro.util.Gadget;

public class CbbCXAction extends Activity {
	private ListView lv;
	private TextView tv;
	private EditText et;
	private Button   done;
	private Button   cancel;
	private List<String> meterList;
	private UsersInfo usInfo;
	private BookNumberAdapter bookAdapter;
	private DBService service;
	private String filepath;
	private String DBName;
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cbbcx);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		handler = new Handler();
		lv = (ListView) findViewById(R.id.lv_show_men);
		tv = (TextView) findViewById(R.id.tv_show_met);
		et = (EditText) findViewById(R.id.meter_num);
		cancel = (Button) findViewById(R.id.btn_cancel);
		done = (Button) findViewById(R.id.btn_done);
		SharedPreferences sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
		DBName = sharedPreferences.getString("dbName", "");
		if(DBName!=null&&!"".equals(DBName)){
			tv.setEnabled(true);
			done.setEnabled(true);
			System.out.println("����");
			filepath = Environment.getDataDirectory().getPath() + "/data/"+"com.example.android_cbjactivity"+"/databases/";
			lv.setOnItemClickListener(new OnItemClickListener() {
	
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position,
						long arg3) {
					String book = meterList.get(position);
					tv.setText(book);
				}
			});
		}else{
			tv.setEnabled(false);
			done.setEnabled(false);
			Toast.makeText(getApplicationContext(), "�������ҳ�ļ�ѡ��", Toast.LENGTH_SHORT).show();
		}
	}
	public void onAction(View view){
		int id = view.getId();
		switch(id){
		case R.id.tv_show_met:
			Thread thread = new Thread(){
				public void run() {
					service = new DBService(filepath+DBName);
					meterList = service.queryBookofUser();	
				}
			};
			thread.start();	
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(String str:meterList)
				System.out.println(str);
			if(meterList.isEmpty()){
			Toast.makeText(getApplicationContext(), "����Ϊ��", Toast.LENGTH_LONG).show();
			}else{
			bookAdapter = new BookNumberAdapter(getApplicationContext(), meterList);
			lv.setAdapter(bookAdapter);	
			lv.setVisibility(lv.VISIBLE);
			}
			break;
		case R.id.btn_cancel:
			if(et!=null&&et.getText()!=null){
				et.setText("");
			}
			break;
		case R.id.cbbcx_back_btn:
			Gadget.closeKeybord(et, CbbCXAction.this);
			finish();
			break;
		case R.id.btn_done:
			if("����ѡ��".equals(tv.getText().toString())||"".equals(et.getText().toString())){
				Toast.makeText(getApplicationContext(), "����д��ѯ��Ϣ", Toast.LENGTH_LONG).show();
			}else{
				new Thread(){
					@Override
					public void run() {
						super.run();
						String book = tv.getText().toString();
						//book = book.substring(0, 1);
						String DOMETERID = et.getText().toString();
						service = new DBService(filepath+DBName);
						usInfo = service.queryUserInfobybook_meterid(book, DOMETERID);
						System.out.println("usInfo:"+usInfo.getBOOK());
						if(usInfo!=null&&null==usInfo.getUSID()){
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									System.out.println("������û ��");
									Toast.makeText(CbbCXAction.this, "��˶Բ�ѯ��Ϣ", Toast.LENGTH_SHORT).show();
								}
							});
						}else{
							Intent intent = new Intent();
							intent.setClass(CbbCXAction.this, UserDetailActivity.class);
							intent.putExtra("action", "CbbCXAction");
							intent.putExtra("usInfo", (Serializable)usInfo);
							startActivity(intent);
							
						}
					}
				}.start();
			}
			break;
			default:
				break;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
