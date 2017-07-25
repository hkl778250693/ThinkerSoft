package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.adapter.MeterUserCoordinateManageAdapter;
import com.example.administrator.thinker_soft.meter_code.model.MeterUserCoordinateManageItem;
import com.example.administrator.thinker_soft.mode.MyAnimationUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;

import java.util.ArrayList;

public class MeterUserCoordinateManageActivity extends Activity {
	private ImageView back;
	private SQLiteDatabase db;  //数据库
	private SharedPreferences sharedPreferences_login,sharedPreferences;
	private String bookID,book_name,fileName;
	private TextView bookName,lastPage, nextPage, currentPageTv, totalPageTv;
	private ListView listview;
	private Cursor totalCountCursor, userLimitCursor;
	private ArrayList<MeterUserCoordinateManageItem> userLists = new ArrayList<>();
	private MeterUserCoordinateManageAdapter adapter;
	private int dataStartCount = 0;   //用于分页查询，表示从第几行开始
	private int currentPage = 1;  //当前页数
	private int totalPage;    //总页数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meter_user_coodinate_manage);

		bindView();
		defaultSetting();
		setViewClickListener();
	}

	//绑定控件
	private void bindView() {
		back = (ImageView) findViewById(R.id.back);
		bookName = (TextView) findViewById(R.id.book_name);
		listview = (ListView) findViewById(R.id.listview);
		lastPage = (TextView) findViewById(R.id.last_page);
		nextPage = (TextView) findViewById(R.id.next_page);
		currentPageTv = (TextView) findViewById(R.id.current_page_tv);
		totalPageTv = (TextView) findViewById(R.id.total_page_tv);
	}

	//初始化设置
	private void defaultSetting() {
		MySqliteHelper helper = new MySqliteHelper(MeterUserCoordinateManageActivity.this, 1);
		db = helper.getWritableDatabase();
		sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
		sharedPreferences = MeterUserCoordinateManageActivity.this.getSharedPreferences(sharedPreferences_login.getString("login_name","")+"data", Context.MODE_PRIVATE);
		Intent intent = getIntent();
		if(intent != null){
			fileName = intent.getStringExtra("fileName");
			bookID = intent.getStringExtra("bookID");
			book_name = intent.getStringExtra("bookName");
			bookName.setText("当前："+book_name);
			if(!"".equals(bookID) && !"".equals(fileName)){
				Log.i("meter_user","");
				new Thread() {
					@Override
					public void run() {
						super.run();
						getTotalUserCount();
						getMeterUserData(fileName, bookID, dataStartCount);   //默认读取本地的抄表分区用户数据
						handler.sendEmptyMessage(0);
					}
				}.start();
			}
		}
	}

	//点击事件
	public void setViewClickListener() {
		back.setOnClickListener(clickListener);
		lastPage.setOnClickListener(clickListener);
		nextPage.setOnClickListener(clickListener);
	}

	View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.back:
					MeterUserCoordinateManageActivity.this.finish();
					break;
				case R.id.last_page:
					lastPage.setClickable(false);
					if (currentPageTv.getText().equals("1")) {
						Toast.makeText(MeterUserCoordinateManageActivity.this, "已经是第一页哦！", Toast.LENGTH_SHORT).show();
					} else {
						currentPageTv.setText(String.valueOf(Integer.parseInt(currentPageTv.getText().toString()) - 1));
						dataStartCount -= 50;
						new Thread() {
							@Override
							public void run() {
								super.run();
								getMeterUserData(fileName, bookID, dataStartCount);  //读取抄表本用户数据
								handler.sendEmptyMessage(1);
							}
						}.start();
					}
					break;
				case R.id.next_page:
					nextPage.setClickable(false);
					if (currentPageTv.getText().equals(totalPageTv.getText())) {
						Toast.makeText(MeterUserCoordinateManageActivity.this, "已经是最后一页哦！", Toast.LENGTH_SHORT).show();
					} else {
						currentPageTv.setText(String.valueOf(Integer.parseInt(currentPageTv.getText().toString()) + 1));
						dataStartCount += 50;
						new Thread() {
							@Override
							public void run() {
								super.run();
								getMeterUserData(fileName, bookID, dataStartCount);  //读取抄表本用户数据
								handler.sendEmptyMessage(1);
							}
						}.start();
					}
					Log.i("MeterUserLVActivity", "总页数为：" + totalPageTv.getText());
					Log.i("MeterUserLVActivity", "开始行数是：" + dataStartCount);
					Log.i("MeterUserLVActivity", "当前页数是：" + currentPageTv.getText());
					break;
				default:
					break;
			}
		}
	};

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					adapter = new MeterUserCoordinateManageAdapter(MeterUserCoordinateManageActivity.this, userLists);
					adapter.notifyDataSetChanged();
					listview.setAdapter(adapter);
					MyAnimationUtils.viewGroupOutAlphaAnimation(MeterUserCoordinateManageActivity.this,listview,0.1F);
					currentPageTv.setText(String.valueOf(currentPage));
					if (totalCountCursor.getCount() % 50 != 0) {
						totalPage = totalCountCursor.getCount() / 50 + 1;
					} else {
						if (totalCountCursor.getCount() <= 50) {
							totalPage = 1;
						} else {
							totalPage = totalCountCursor.getCount() / 50;
						}
					}
					totalPageTv.setText(String.valueOf(totalPage));
					break;
				case 1:
					adapter = new MeterUserCoordinateManageAdapter(MeterUserCoordinateManageActivity.this, userLists);
					adapter.notifyDataSetChanged();
					listview.setAdapter(adapter);
					MyAnimationUtils.viewGroupOutAlphaAnimation(MeterUserCoordinateManageActivity.this,listview,0.1F);
					lastPage.setClickable(true);
					nextPage.setClickable(true);
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
	};

	//查询抄表用户总数
	public void getTotalUserCount() {
		totalCountCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=?", new String[]{sharedPreferences_login.getString("userId", ""), fileName,bookID});//查询并获得游标
		//如果游标为空，则显示没有数据图片
		Log.i("MeterUserLVActivity", "总的查询到" + totalCountCursor.getCount() + "条数据！");
		if (totalCountCursor.getCount() == 0) {
			return;
		}
		while (totalCountCursor.moveToNext()) {

		}
		totalCountCursor.close();
	}

	//读取本地的抄表分区用户数据
	public void getMeterUserData(String fileName,String bookID,int dataStartCount) {
		userLists.clear();
		if(!"".equals(sharedPreferences.getString("page_count",""))){
			userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? limit " + dataStartCount + ","+Integer.parseInt(sharedPreferences.getString("page_count","")), new String[]{sharedPreferences_login.getString("userId", ""), fileName,bookID});//查询并获得游标
		}else {
			userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? limit " + dataStartCount + ",50", new String[]{sharedPreferences_login.getString("userId", ""), fileName,bookID});//查询并获得游
		}
		Log.i("MeterUserLVActivity", "分页查询到" + userLimitCursor.getCount() + "条数据！");
		//如果游标为空，则显示没有数据图片
		if (userLimitCursor.getCount() == 0) {
			return;
		}
		while (userLimitCursor.moveToNext()) {
			MeterUserCoordinateManageItem item = new MeterUserCoordinateManageItem();
			item.setMeterID(userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_order_number")));
			item.setUserName(userLimitCursor.getString(userLimitCursor.getColumnIndex("user_name")));
			item.setUserID(userLimitCursor.getString(userLimitCursor.getColumnIndex("user_id")));
			if (!userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_number")).equals("null")) {
				item.setMeterNumber(userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_number")));
			} else {
				item.setMeterNumber("无");
			}
			item.setLongitude(userLimitCursor.getString(userLimitCursor.getColumnIndex("last_month_dosage")));
			item.setLatitude(userLimitCursor.getString(userLimitCursor.getColumnIndex("this_month_dosage")));
			item.setAddress(userLimitCursor.getString(userLimitCursor.getColumnIndex("user_address")));
			userLists.add(item);
		}
	}
}
