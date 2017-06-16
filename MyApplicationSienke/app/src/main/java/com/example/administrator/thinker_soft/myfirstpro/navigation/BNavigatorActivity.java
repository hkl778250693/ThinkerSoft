package com.example.administrator.thinker_soft.myfirstpro.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.administrator.thinker_soft.R;

public class BNavigatorActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bnavigation);

	}
}
