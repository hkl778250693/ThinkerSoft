package com.example.administrator.thinker_soft.android_cbjactivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.util.AssembleUpmes;
import com.example.administrator.thinker_soft.myfirstpro.util.Gadget;
import com.example.administrator.thinker_soft.myfirstpro.util.MyDialog;
import com.example.administrator.thinker_soft.myfirstpro.util.UniqueID;

import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.util.Regularexpression;
import com.example.administrator.thinker_soft.myfirstpro.util.SocketGetData;

public class Verificationactivity extends Activity implements OnClickListener{
	private TextView vertification_UUID;
	private EditText vertification_Code;
	private TextView vertification_Code_sure;
	private TextView vertification_switch;
	private LinearLayout vertification_open;
	private EditText vertification_ip;
	private EditText vertification_port;
	private Button vertification_cancel;
	private Button vertification_sure;
	private FrameLayout vertification_Frame;
	private String ip;
	private String port;
	private XiaZaiActivity.MyHandler dataHandler;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private Dialog dialog;
	private String md5 = "www.thinkersoft.cc";
	private String Code = "";
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
/*				//�洢��SDCARD�ļ�
				String filepath = Environment.getExternalStorageDirectory().getPath()+"/THK";
				File file = new File(filepath,"configuration_file.txt");
				try {
					if(!file.exists()){
							file.createNewFile();
					}else{
						FileOutputStream fileOutputStream = new FileOutputStream(file);
						byte[] buffer = Code.getBytes();
						fileOutputStream.write(buffer);
						fileOutputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}*/
				//�洢�������ļ�
				editor.putString("code", Code);
				editor.commit();
				//��ת
				Intent intent = new Intent(Verificationactivity.this,CBJActivity.class);
				startActivity(intent);
				dialog.dismiss();
				finish();
				break;
			case 1:
				Toast.makeText(Verificationactivity.this, "ע������Ч", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		guiwadget();
		inti();
	}
	private void inti() {
		UniqueID uniqueID = new UniqueID(getApplicationContext());
		vertification_UUID.setText(uniqueID.getUniqueID());
		sharedPreferences = getSharedPreferences("IP_PORT_DBNAME", 0);
		editor = sharedPreferences.edit();
		ip = sharedPreferences.getString("ip", "");
		port = sharedPreferences.getString("port", "");
		if(!"".equals(ip)&&!"".equals(port)){
			vertification_ip.setText(ip);
			vertification_port.setText(port);
		}
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 70);
		vertification_Code_sure.setText("ע��");
		vertification_Code_sure.setLayoutParams(params);
		vertification_Code.setLayoutParams(params);
	}
	private void guiwadget() {
		vertification_UUID = (TextView)findViewById(R.id.vertification_UUID);
		vertification_Frame = (FrameLayout) findViewById(R.id.vertification_Frame);
		vertification_Code = (EditText) findViewById(R.id.vertification_Code);
		vertification_Code_sure = (TextView) findViewById(R.id.vertification_Code_sure);
		vertification_switch = (TextView) findViewById(R.id.vertification_switch);
		vertification_open = (LinearLayout) findViewById(R.id.vertification_open);
		vertification_ip = (EditText) findViewById(R.id.vertification_ip);
		vertification_port = (EditText) findViewById(R.id.vertification_port);
		vertification_cancel = (Button) findViewById(R.id.vertification_cancel);
		vertification_sure = (Button) findViewById(R.id.vertification_sure);
		vertification_Code_sure.setOnClickListener(this);
		vertification_switch.setOnClickListener(this);
		vertification_cancel.setOnClickListener(this);
		vertification_sure.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vertification_Code_sure:
			if("ȷ��".equals(vertification_Code_sure.getText().toString())){
				if("".equals(ip)||"".equals(port)){
					Toast.makeText(Verificationactivity.this, "������ip��˿ں�", Toast.LENGTH_SHORT).show();
					break;
				}
				if("".equals(vertification_Code.getText().toString())){
					Toast.makeText(Verificationactivity.this, "������ע����", Toast.LENGTH_SHORT).show();
					break;					
				}else{
					FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 70);
					
					vertification_Code_sure.setText("ע��");
					params.setMargins(0, 0, 0, 0);
					vertification_Code_sure.setBackgroundResource(R.drawable.vertify_btn_shape_yes);
					vertification_Code_sure.setLayoutParams(params);
					dialog = MyDialog.createLoadingDialog(Verificationactivity.this, "��֤�У����Ժ�");
					dialog.setCanceledOnTouchOutside(false);
					dialog.setCancelable(true);
					dialog.setOnDismissListener(new OnDismissListener() {
						
						@Override
						public void onDismiss(DialogInterface dialog) {
							
						}
					});
					dialog.show();
					new Thread(){
						public void run() {
							
							try {
								Code = Gadget.EncoderByMd5(vertification_Code.getText().toString()+md5);
							} catch (Exception e) {
								e.printStackTrace();
							}
							String UUID = vertification_UUID.getText().toString();
							String parameter = AssembleUpmes.verifyRigisterCodeParameter(UUID, Code);
							SocketGetData getData = new SocketGetData();
							String result = getData.getData(getApplicationContext(), Integer.parseInt(port), ip, "NANBU", parameter);
							if(!"��ȡ����ʧ��".equals(result)){
								handler.sendEmptyMessage(0);
							}else{
								handler.sendEmptyMessage(1);
							}
						};
					}.start();
				}
			}else if("ע��".equals(vertification_Code_sure.getText().toString())){
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 70);
				params.setMargins(0, 80, 0, 0);
				vertification_Code_sure.setText("ȷ��");
				vertification_Code_sure.setLayoutParams(params);
				vertification_Code_sure.setBackgroundResource(R.drawable.vertify_btn_shape_no);
			}
			break;
		case R.id.vertification_switch:
			if(View.GONE==vertification_open.getVisibility()){
				vertification_open.setVisibility(View.VISIBLE);
			}else{
				vertification_open.setVisibility(View.GONE);
			}
			break;
		case R.id.vertification_cancel:
			vertification_ip.setText("");
			vertification_port.setText("");
			break;
		case R.id.vertification_sure:
			if("".equals(vertification_ip.getText().toString())){
				Toast.makeText(Verificationactivity.this, "����Ip", Toast.LENGTH_SHORT).show();
				break;
			}else if("".equals(vertification_port.getText().toString())){
				Toast.makeText(Verificationactivity.this, "����˿ں�", Toast.LENGTH_SHORT).show();
				break;				
			}else{
				ip = vertification_ip.getText().toString();
				port = vertification_port.getText().toString();
				if(!Regularexpression.checkPort(port)){//||!Regularexpression.checkIp(ip)
					Toast.makeText(Verificationactivity.this, "����ȷ��д��Ϣ", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!Regularexpression.checkIp(ip)){
					Toast.makeText(Verificationactivity.this, "����ȷ��д��Ϣ", Toast.LENGTH_SHORT).show();
					return;					
				}
				editor.putString("ip", ip);
				editor.putString("port", port);
				editor.commit();
				vertification_open.setVisibility(View.GONE);
				Toast.makeText(Verificationactivity.this, "���", Toast.LENGTH_SHORT).show();
			}
			break;			
		default:
			break;
		}
	}
}
