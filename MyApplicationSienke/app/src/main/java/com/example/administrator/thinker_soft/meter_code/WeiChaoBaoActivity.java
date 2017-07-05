package com.example.administrator.thinker_soft.meter_code;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.activity.MeterSelectActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterUserListviewActivity;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;

public class WeiChaoBaoActivity extends Activity {

	private TextView mTextViewfanhui;
	private TextView choose_meter;
	private TextView choose_all;
	private int  meter_sg = 0;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weichaobiao);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
		mTextViewfanhui=(TextView) findViewById(R.id.tv_weichaobiao_back);
		choose_meter = (TextView) findViewById(R.id.tv_choose_met);
		choose_all = (TextView) findViewById(R.id.tv_choose_all);
		choose_meter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				meter_sg = 1;
				intent.putExtra("meter_sg", meter_sg);
				intent.setClass(WeiChaoBaoActivity.this, MeterSelectActivity.class);
				startActivity(intent);
				
				editor = sharedPreferences.edit();
				editor.putInt("meter_sg", meter_sg);
				editor.apply();
				WeiChaoBaoActivity.this.finish();
			}
		});
		choose_all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				meter_sg = 2; 
				intent.putExtra("meter_sg", meter_sg);
				intent.setClass(WeiChaoBaoActivity.this, MeterUserListviewActivity.class);
				startActivity(intent);
				editor = sharedPreferences.edit();
				editor.putInt("meter_sg", meter_sg);
				editor.apply();
				WeiChaoBaoActivity.this.finish();
			}
		});
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		int resultCode = 1;
		setResult(resultCode);
	}
	public void onAction(View v) {
		
		int id=v.getId();
		
		switch (id) {
		case R.id.tv_weichaobiao_back:
			finish();
			break;

		default:
			break;
		}

	}
	
}
