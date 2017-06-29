package com.example.administrator.thinker_soft.meter_code;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.thinker_soft.R;

public class MeterUserDetailActivity extends Activity {
	private ImageView back;

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
	}

	//初始化设置
	private void defaultSetting() {

	}

	//点击事件
	public void setViewClickListener() {
		back.setOnClickListener(clickListener);
	}

	View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.back:
					MeterUserDetailActivity.this.finish();
					break;
			}
		}
	};
}
