package com.example.administrator.thinker_soft.android_cbjactivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.util.Gadget;
import com.example.administrator.thinker_soft.myfirstpro.util.Mytoast;
import com.example.administrator.thinker_soft.myfirstpro.util.Regularexpression;

public class ChooseCountActivity extends Activity {
	private EditText editText;
	private Button button;
	private LinearLayout back;
	private int number;
	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choosecount);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		editText = (EditText) findViewById(R.id.et_set_count);
		button = (Button) findViewById(R.id.btn_set_count_done);
		back = (LinearLayout) findViewById(R.id.set_choose_count_back_Btn);
		sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
		if(sharedPreferences.contains("number")){
			number = sharedPreferences.getInt("number", 0);
			System.out.println(number);
			editText.setText(String.valueOf(number));
			editText.setSelection(String.valueOf(number).length());
		}
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String count = editText.getText().toString();
				if(!Regularexpression.checkCount(count)){
					Toast.makeText(ChooseCountActivity.this, "����ȷ��д��Ϣ", Toast.LENGTH_SHORT).show();
					return;
				}
				if("��������".equals(count)){
					Toast.makeText(ChooseCountActivity.this, "��д��Ϣ", Toast.LENGTH_SHORT).show();
				}else{
					sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putInt("number", Integer.parseInt(count));
					editor.commit();
					Toast.makeText(ChooseCountActivity.this, "���óɹ�", Toast.LENGTH_SHORT).show();
				}		
			}
		});
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Gadget.closeKeybord(editText, ChooseCountActivity.this);
				finish();
			}
		});
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(null!=s.toString()&&!"".equals(s.toString())){
					if(Integer.parseInt(s.toString())>100){
						editText.setText("100");
						editText.setSelection(3);
						Mytoast.showToast(getApplicationContext(), "ÿҳ��ʾ�������ܳ���100", Mytoast.LENGTH_SHORT);
					}
				}
			}
		});
	}
}
