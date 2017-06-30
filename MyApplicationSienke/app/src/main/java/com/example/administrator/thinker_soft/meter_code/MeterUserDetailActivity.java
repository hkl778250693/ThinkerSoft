package com.example.administrator.thinker_soft.meter_code;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;

public class MeterUserDetailActivity extends Activity {
	private ImageView back;
	private TextView save;
	private SQLiteDatabase db;  //数据库
	private SharedPreferences sharedPreferences_login;
	private EditText thisMonthEndDegree;   //本月止度
	private String userID;  //当前抄表用户的ID

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meter_user_detail_info);

		bindView();
		defaultSetting();
		setViewClickListener();
	}

	//绑定控件
	private void bindView() {
		back = (ImageView) findViewById(R.id.back);
		thisMonthEndDegree = (EditText) findViewById(R.id.this_month_end_degree);
		save = (TextView) findViewById(R.id.save);
	}

	//初始化设置
	private void defaultSetting() {
		MySqliteHelper helper = new MySqliteHelper(MeterUserDetailActivity.this, 1);
		db = helper.getWritableDatabase();
		sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
		Intent intent = getIntent();
		if(intent != null){
			userID = intent.getStringExtra("user_id");
		}
	}

	//点击事件
	public void setViewClickListener() {
		back.setOnClickListener(clickListener);
		save.setOnClickListener(clickListener);
	}

	View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.back:
					MeterUserDetailActivity.this.finish();
					break;
				case R.id.save:
					updateMeterUserInfo();
					Intent intent = new Intent();
					intent.putExtra("this_month_en_degree",thisMonthEndDegree.getText().toString().trim());
					setResult(RESULT_OK,intent);
					finish();
					break;
			}
		}
	};

	/**
	 * 保存抄表信息到本地数据库
	 */
	private void updateMeterUserInfo() {
		ContentValues values = new ContentValues();
		values.put("this_month_end_degree", thisMonthEndDegree.getText().toString().trim());
		values.put("meterState", "true");
		db.update("MeterUser", values, "login_user_id=? and user_id=?", new String[]{sharedPreferences_login.getString("userId", ""),userID});
	}
}
