package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/6/9.
 */
public class BusinessMessageActivity extends Activity {

    private ImageView back;
    private EditText content;
    private TextView leixing, time;
    private Calendar c; //日历
    private RelativeLayout send;
    private PopupWindow window;
    private TextView huiyi, tongzhi;
    private SQLiteDatabase db;  //数据库
    private SharedPreferences sharedPreferences_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_fbgg);//发布公告

        bindView();//绑定控件
        defaultSetting();//初始化设置
        setViewClickListener();//点击事件

    }

    private void defaultSetting() {
        c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        time.setText(dateFormat.format(new Date()));
        MySqliteHelper helper = new MySqliteHelper(BusinessMessageActivity.this, 1);
        db = helper.getWritableDatabase();
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
    }

    public void bindView() {
        back = (ImageView) findViewById(R.id.back);
        leixing = (TextView) findViewById(R.id.leixing);
        time = (TextView) findViewById(R.id.time);
        send = (RelativeLayout) findViewById(R.id.send);
        content = (EditText) findViewById(R.id.content);
    }

    public void setViewClickListener() {
        back.setOnClickListener(clickListener);
        time.setOnClickListener(clickListener);
        leixing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = BusinessMessageActivity.this.getLayoutInflater().inflate(R.layout.popupwindow_business_fbgg, null);
                window = new PopupWindow(popupView, 600, 400);
                window.setAnimationStyle(R.style.Popupwindow);
                window.setFocusable(true);
                backgroundAlpha(0.6F);   //背景变暗
                window.setOutsideTouchable(true);
                window.update();
                window.showAsDropDown(leixing, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1.0F);
                        leixing.setClickable(true);
                    }
                });

                huiyi = (TextView) popupView.findViewById(R.id.huiyi);
                tongzhi = (TextView) popupView.findViewById(R.id.tongzhi);
                huiyi.setOnClickListener(clickListener);
                tongzhi.setOnClickListener(clickListener);
                send.setOnClickListener(clickListener);
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.time:
                    time.setClickable(false);
                    //开始时间选择器
                    DatePickerDialog startDateDialog = new DatePickerDialog(BusinessMessageActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            time.setText(new StringBuilder().append(year).append("-").append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "")
                                    .append("-")
                                    .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth + ""));
                            time.setClickable(true);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    startDateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            time.setClickable(true);
                        }
                    });
                    startDateDialog.show();
                    break;
                case R.id.huiyi:
                    window.dismiss();
                    leixing.setText("会议纪要");
                    break;
                case R.id.tongzhi:
                    window.dismiss();
                    leixing.setText("通知公告");
                    break;
                case R.id.send:
                    insertOaAnnounce();
                    Toast.makeText(BusinessMessageActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    content.setText("");
                    break;

            }
        }
    };

    private void insertOaAnnounce() {
        ContentValues values = new ContentValues();
        values.put("userId", sharedPreferences_login.getString("userId", ""));
        values.put("userName", sharedPreferences_login.getString("user_name", ""));
        values.put("type", leixing.getText().toString().trim());
        values.put("time", time.getText().toString().trim());
        values.put("content", content.getText().toString().trim());
        db.insert("OaAnnounce", null, values);
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = BusinessMessageActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            BusinessMessageActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            BusinessMessageActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        BusinessMessageActivity.this.getWindow().setAttributes(lp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
