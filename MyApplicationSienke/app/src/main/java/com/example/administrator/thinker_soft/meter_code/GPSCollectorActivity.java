package com.example.administrator.thinker_soft.meter_code;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.appcation.MyApplication;

import java.util.ArrayList;
import java.util.List;

import com.example.administrator.thinker_soft.myfirstpro.fragment.GPSMeterFragment;
import com.example.administrator.thinker_soft.myfirstpro.fragment.GPSValveFragment;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;

public class GPSCollectorActivity extends FragmentActivity {
	private LinearLayout gpscollector_back_Btn;
	private ViewPager viewPager;
	private PagerTabStrip tab;
	private FragmentTransaction transaction;
	private GPSMeterFragment gpsFragment;
	private GPSValveFragment valveFragment;
	private List<Fragment> fragmentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		setContentView(R.layout.avtivity_gpscollector);
		guiwadget();
		init();
	}
	private void init() {
		fragmentList = new ArrayList<Fragment>();
		gpsFragment = GPSMeterFragment.getInstance();
		valveFragment = GPSValveFragment.getInstance();
		fragmentList.add(gpsFragment);
		//fragmentList.add(valveFragment);
		viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
	}
	private void guiwadget() {
		gpscollector_back_Btn = (LinearLayout) findViewById(R.id.gpscollector_back_Btn);
		viewPager = (ViewPager) findViewById(R.id.gps_viewpager);
		tab = (PagerTabStrip) findViewById(R.id.gps_pagerTabStrip);
		tab.setTabIndicatorColor(getResources().getColor(R.color.btn_color_xz));
		tab.setBackgroundColor(getResources().getColor(R.color.dialog_bg));
		gpscollector_back_Btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GPSCollectorActivity.this.finish();
				MyApplication.gpsmeterHandler = null;
				MyApplication.gpsvalveHandler = null;
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
			MyApplication.gpsmeterHandler = null;
			MyApplication.gpsvalveHandler = null;
		}
		return super.onKeyDown(keyCode, event);
	}
	class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;
		private String[] titlearray;
		private FragmentManager fm;
		
		public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			//titlearray = new String[]{"ˮ��ɼ�","�����ɼ�"};
			titlearray = new String[]{"ˮ��ɼ�"};
			this.fragments = fragments;
			this.fm = fm;
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			return this.fragments.get(position);
		}
		
		@Override
		public int getCount() {
			return this.titlearray.length;
		}
		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return POSITION_NONE;
		}
		@Override
		public CharSequence getPageTitle(int position) {
			return this.titlearray[position];
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Fragment fragment = (Fragment) super.instantiateItem(container, position);
			
			return fragment;
		}
	}
}
