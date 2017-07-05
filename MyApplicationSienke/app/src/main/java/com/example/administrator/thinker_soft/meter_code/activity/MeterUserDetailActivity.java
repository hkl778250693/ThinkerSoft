package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.content.ContentValues;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MeterUserDetailActivity extends Activity {
    private ImageView back;
    private TextView save;
    private SQLiteDatabase db;  //数据库
    private Cursor cursor;
    private SharedPreferences sharedPreferences_login;
    private SimpleDateFormat dateFormat;
    private EditText thisMonthEndDegree;   //本月止度
    private String userID;  //当前抄表用户的ID
    private String longitude, latitude, thisMonthDosage, lastMonthDegree, lastMonthDosage, meterDate,
            meterFlag, meterUserName, meterUserID, meterUserOldID, meterNumber, meterBelongArea, meterUserAddress,
            meterUserPhone, meterUserBalance, meterOweAcount, meterOweMonths, meterUserProperty, meterBook,
            meterSerialNumb, meterModel, meterReader, meterChangeDosage, meterStartdosage, meterRemission, meterRubbish;
    private TextView longitudeTv,latitudeTv,thisMonthDosageTv,lastMonthDegreeTv,lastMonthDosageTv,meterDateTv,
            meterFlagTv,meterUserNameTv,meterUserIDTv,meterUserOldIDTv,meterNumberTv,meterBelongAreaTv,meterUserAddressTv,
            meterUserPhoneTv,meterUserBalanceTv,meterOweAcountTv,meterOweMonthsTv,meterUserPropertyTv,meterBookTv,
            meterSeriaNumbTv,meterModelTv,meterReaderTv,meterChangeDosageTv,meterStartdosageTv,meterRemissionTv,meterRubbishTv;

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
        longitudeTv = (TextView) findViewById(R.id.longitude);
        latitudeTv = (TextView) findViewById(R.id.latitude);
        thisMonthDosageTv = (TextView) findViewById(R.id.this_month_dosage);
        lastMonthDegreeTv = (TextView) findViewById(R.id.last_month_degree);
        lastMonthDosageTv = (TextView) findViewById(R.id.last_month_dosage);
        meterDateTv = (TextView) findViewById(R.id.meter_date);
        meterFlagTv = (TextView) findViewById(R.id.meter_flag);
        meterUserNameTv = (TextView) findViewById(R.id.user_name);
        meterUserIDTv = (TextView) findViewById(R.id.user_id);
        meterUserOldIDTv = (TextView) findViewById(R.id.user_old_id);
        meterNumberTv = (TextView) findViewById(R.id.meter_number);
        meterBelongAreaTv = (TextView) findViewById(R.id.area_name);
        meterUserAddressTv = (TextView) findViewById(R.id.user_addres);
        meterUserPhoneTv = (TextView) findViewById(R.id.user_phone);
        meterUserBalanceTv = (TextView) findViewById(R.id.user_balance);
        meterOweAcountTv = (TextView) findViewById(R.id.owe_acount);
        meterOweMonthsTv = (TextView) findViewById(R.id.owe_months);
        meterUserPropertyTv = (TextView) findViewById(R.id.user_property);
        meterBookTv = (TextView) findViewById(R.id.meter_book);
        meterSeriaNumbTv = (TextView) findViewById(R.id.meter_serial_numb);
        meterModelTv = (TextView) findViewById(R.id.meter_model);
        meterReaderTv = (TextView) findViewById(R.id.meter_reader);
        meterChangeDosageTv = (TextView) findViewById(R.id.change_dosage);
        meterStartdosageTv = (TextView) findViewById(R.id.start_dosage);
        meterRemissionTv = (TextView) findViewById(R.id.remission);
        meterRubbishTv = (TextView) findViewById(R.id.rubbish_cost);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(MeterUserDetailActivity.this, 1);
        db = helper.getWritableDatabase();
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra("user_id");
            Log.i("MeterUserDetailActivity","用户ID为："+userID);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    getMeterUserDetailInfo();
                }
            }.start();
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
            switch (v.getId()) {
                case R.id.back:
                    MeterUserDetailActivity.this.finish();
                    break;
                case R.id.save:
                    if (!"".equals(thisMonthEndDegree.getText().toString())) {
                        meterDate = dateFormat.format(new Date());
                        meterDateTv.setText(meterDate);
                        updateMeterUserInfo();
                        Intent intent = new Intent();
                        intent.putExtra("this_month_dosage", thisMonthDosageTv.getText().toString().trim());
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(MeterUserDetailActivity.this, "请您输入本月读数！", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    /**
     * 根据用户ID查询本地数据库抄表详情
     */
    private void getMeterUserDetailInfo() {
        cursor = db.rawQuery("select * from MeterUser where login_user_id=? and user_id=?", new String[]{sharedPreferences_login.getString("userId", ""),userID});//查询并获得游标
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            thisMonthDosage = cursor.getString(cursor.getColumnIndex("this_month_dosage"));
            if("".equals(cursor.getString(cursor.getColumnIndex("meter_date")))){
                meterDate = "暂无";
            }else {
                meterDate = cursor.getString(cursor.getColumnIndex("meter_date"));
            }
            lastMonthDegree = cursor.getString(cursor.getColumnIndex("meter_degrees"));
            lastMonthDosage = cursor.getString(cursor.getColumnIndex("last_month_dosage"));
            if (cursor.getString(cursor.getColumnIndex("meterState")).equals("false")) {
                meterFlag = "未抄";
            } else {
                meterFlag = "已抄";
            }
            meterUserName = cursor.getString(cursor.getColumnIndex("user_name"));
            meterUserID = cursor.getString(cursor.getColumnIndex("user_id"));
            meterUserOldID = cursor.getString(cursor.getColumnIndex("old_user_id"));
            if(!cursor.getString(cursor.getColumnIndex("meter_number")).equals("null")){
                meterNumber = cursor.getString(cursor.getColumnIndex("meter_number"));
            }else {
                meterNumber = "暂无";
            }
            meterBelongArea = cursor.getString(cursor.getColumnIndex("area_name"));
            meterUserAddress = cursor.getString(cursor.getColumnIndex("user_address"));
            if(!cursor.getString(cursor.getColumnIndex("user_phone")).equals("null")){
                meterUserPhone = cursor.getString(cursor.getColumnIndex("user_phone"));
            }else {
                meterUserPhone = "暂无";
            }
            meterUserBalance = cursor.getString(cursor.getColumnIndex("user_amount"));
            meterOweAcount = cursor.getString(cursor.getColumnIndex("arrearage_amount"));
            meterOweMonths = cursor.getString(cursor.getColumnIndex("arrearage_months"));
            meterUserProperty = cursor.getString(cursor.getColumnIndex("property_name"));
            meterBook = cursor.getString(cursor.getColumnIndex("book_name"));
            meterSerialNumb = cursor.getString(cursor.getColumnIndex("meter_order_number"));
            meterModel = cursor.getString(cursor.getColumnIndex("meter_model"));
            meterReader = cursor.getString(cursor.getColumnIndex("meter_reader_name"));
            meterChangeDosage = cursor.getString(cursor.getColumnIndex("dosage_change"));
            meterStartdosage = cursor.getString(cursor.getColumnIndex("start_dosage"));
            meterRemission = cursor.getString(cursor.getColumnIndex("remission"));
            meterRubbish = cursor.getString(cursor.getColumnIndex("rubbish_cost"));
        }
        handler.sendEmptyMessage(0);
    }

    /**
     * 保存抄表信息到本地数据库
     */
    private void updateMeterUserInfo() {
        ContentValues values = new ContentValues();
        values.put("this_month_end_degree", thisMonthEndDegree.getText().toString().trim());
        int monthDosage = Integer.parseInt(thisMonthEndDegree.getText().toString()) - Integer.parseInt(lastMonthDegree);
        values.put("this_month_dosage", "" + monthDosage);
        values.put("meter_date", meterDate);
        values.put("meterState", "true");
        db.update("MeterUser", values, "login_user_id=? and user_id=?", new String[]{sharedPreferences_login.getString("userId", ""), userID});
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    thisMonthDosageTv.setText(thisMonthDosage);
                    lastMonthDegreeTv.setText(lastMonthDegree);
                    lastMonthDosageTv.setText(lastMonthDosage);
                    meterDateTv.setText(meterDate);
                    meterFlagTv.setText(meterFlag);
                    meterUserNameTv.setText(meterUserName);
                    meterUserIDTv.setText(meterUserID);
                    meterUserOldIDTv.setText(meterUserOldID);
                    meterNumberTv.setText(meterNumber);
                    meterBelongAreaTv.setText(meterBelongArea);
                    meterUserAddressTv.setText(meterUserAddress);
                    meterUserPhoneTv.setText(meterUserPhone);
                    meterUserBalanceTv.setText(meterUserBalance);
                    meterOweAcountTv.setText(meterOweAcount);
                    meterOweMonthsTv.setText(meterOweMonths);
                    meterUserPropertyTv.setText(meterUserProperty);
                    meterBookTv.setText(meterBook);
                    meterSeriaNumbTv.setText(meterSerialNumb);
                    meterModelTv.setText(meterModel);
                    meterReaderTv.setText(meterReader);
                    meterChangeDosageTv.setText(meterChangeDosage);
                    meterStartdosageTv.setText(meterStartdosage);
                    meterRemissionTv.setText(meterRemission);
                    meterRubbishTv.setText(meterRubbish);
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }
}
