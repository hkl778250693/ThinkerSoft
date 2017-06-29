package com.example.administrator.thinker_soft.meter_code;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.util.Gadget;
import com.example.administrator.thinker_soft.myfirstpro.util.Regularexpression;

public class Ip_Port_activity extends Activity {
	private EditText editTextIP;
	private EditText editTextPort;
	private Button   btn_set_done;
	private LinearLayout back;
	private String ip;
	private String port;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ip_port);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		editTextIP = (EditText) findViewById(R.id.et_set_ip);
		editTextPort = (EditText) findViewById(R.id.et_set_port);
		SharedPreferences sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
		if(sharedPreferences.contains("ip")){
			ip = sharedPreferences.getString("ip", "");
			System.out.println(ip);
			editTextIP.setText(ip);
			editTextIP.setSelection(ip.length());
		}
		if(sharedPreferences.contains("port")){
			port = sharedPreferences.getString("port", "");
			System.out.println(port);
			editTextPort.setText(port);
			editTextPort.setSelection(port.length());
		}
		btn_set_done = (Button) findViewById(R.id.btn_set_ip_port_done);
		back = (LinearLayout) findViewById(R.id.set_ip_port_back_Btn);
		btn_set_done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String ip = editTextIP.getText().toString();
				String port = editTextPort.getText().toString();
				if(!Regularexpression.checkPort(port)){//||!Regularexpression.checkIp(ip)
					Toast.makeText(Ip_Port_activity.this, "����ȷ��д��Ϣ", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!Regularexpression.checkIp(ip)){
					Toast.makeText(Ip_Port_activity.this, "����ȷ��д��Ϣ", Toast.LENGTH_SHORT).show();
					return;					
				}
				if("����IP".equals(ip)||"����port".equals(port)){
					Toast.makeText(Ip_Port_activity.this, "��д��Ϣ", Toast.LENGTH_SHORT).show();
				}else{
					SharedPreferences sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("ip", ip);
					editor.putString("port", port);
					editor.commit();
/*					Intent intent = new Intent("MyBroadcast");
					intent.putExtra("ip", ip);
					intent.putExtra("port", port);
					sendBroadcast(intent);*/
					System.out.println("ip:"+ip+"port:"+port);
					Toast.makeText(Ip_Port_activity.this, "���óɹ�", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Gadget.closeKeybord(editTextIP, Ip_Port_activity.this);
				Gadget.closeKeybord(editTextPort, Ip_Port_activity.this);
				finish();
			}
		});
	}
}
