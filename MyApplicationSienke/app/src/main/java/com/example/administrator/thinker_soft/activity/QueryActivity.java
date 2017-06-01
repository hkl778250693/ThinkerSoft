package com.example.administrator.thinker_soft.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017/2/24 0024.
 */
public class QueryActivity extends Activity {
    private ImageView more,back;
    private Button queryBtn;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private EditText editText1;
    private EditText editText2;
    private LinearLayout rootLinearlayout;
    private ImageView frameAnimation;
    private AnimationDrawable animationDrawable;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private View view;
    private long exitTime = 0;//退出程序
    public int responseCode = 0;
    private String result; //网络请求结果
    private String ip;  //接口ip地址
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        bindView();//绑定控件
        defaultSetting();//初始化设置
        setViewClickListener();//设置点击事件
    }

    //绑定控件ID
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        more = (ImageView) findViewById(R.id.more);
        queryBtn = (Button) findViewById(R.id.query_btn);
        radioButton1 = (RadioButton) findViewById(R.id.radio_btn1);
        radioButton2 = (RadioButton) findViewById(R.id.radio_btn2);
        editText1 = (EditText) findViewById(R.id.edit_text1);
        editText2 = (EditText) findViewById(R.id.edit_text2);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
    }

    //设置点击事件
    private void setViewClickListener() {
        back.setOnClickListener(clickListener);
        more.setOnClickListener(clickListener);
        queryBtn.setOnClickListener(clickListener);
        radioButton1.setOnClickListener(clickListener);
        radioButton2.setOnClickListener(clickListener);

        radioButton1.setChecked(true);
        editText1.setEnabled(true);
        editText1.setFocusable(true);
        editText1.setFocusableInTouchMode(true);
        editText2.setEnabled(false);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.radio_btn1:
                    editText1.setEnabled(true);
                    editText2.setEnabled(false);
                    editText1.setFocusable(true);
                    editText1.setFocusableInTouchMode(true);
                    editText2.setFocusable(false);
                    editText2.setFocusableInTouchMode(false);
                    editText1.setText("");
                    editText2.setText("");
                    break;
                case R.id.radio_btn2:
                    editText1.setEnabled(false);
                    editText2.setEnabled(true);
                    editText2.setFocusable(true);
                    editText2.setFocusableInTouchMode(true);
                    editText1.setFocusable(false);
                    editText1.setFocusableInTouchMode(false);
                    editText1.setText("");
                    editText2.setText("");
                    break;
                case R.id.query_btn:
                    queryInformation();
                    break;
                case R.id.more:
                    //开始动画
                    //more.startAnimation(AnimationUtils.loadAnimation(QueryActivity.this,R.anim.more_rotate));
                    startAnimation();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(QueryActivity.this, MoreSettingsActivity.class);
                    startActivityForResult(intent, 200);
                    break;
            }
        }
    };

    //初始化设置
    private void defaultSetting() {
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //开始旋转动画
    public void startAnimation() {
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(more, "alpha", 1.0F, 0.0F, 1.0F);
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(more, "rotation", 0F, 90F);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playTogether(alphaAnimation, rotateAnimation);
        animatorSet.start();
    }

    //返回旋转动画
    public void backAnimation() {
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(more, "alpha", 1.0F, 0.0F, 1.0F);
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(more, "rotation", 90F, 0F);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playTogether(alphaAnimation, rotateAnimation);
        animatorSet.start();
    }

    //开始帧动画
    public void startFrameAnimation() {
        frameAnimation.setBackgroundResource(R.drawable.frame_animation_list);
        animationDrawable = (AnimationDrawable) frameAnimation.getDrawable();
        animationDrawable.start();
    }

    //show弹出框
    public void showPopupwindow() {
        layoutInflater = LayoutInflater.from(QueryActivity.this);
        view = layoutInflater.inflate(R.layout.popupwindow_query_loading, null);
        popupWindow = new PopupWindow(view, 250, 250);
        frameAnimation = (ImageView) view.findViewById(R.id.frame_animation);
        //popupWindow.setFocusable(true);
        //popupWindow.setOutsideTouchable(true);
        //popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E6E6E6")));
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.loading_shape));
        popupWindow.setAnimationStyle(R.style.dialog);
        //popupWindow.update();
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.8F);   //背景变暗
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
        //开始加载动画
        startFrameAnimation();
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    //查询用户信息
    public void queryInformation() {
        if (editText1.getText().toString().equals("") && radioButton1.isChecked()) {
            Toast.makeText(QueryActivity.this, "请输入用户编号", Toast.LENGTH_SHORT).show();
        } else if (editText2.getText().toString().equals("") && radioButton2.isChecked()) {
            Toast.makeText(QueryActivity.this, "请输入表编号", Toast.LENGTH_SHORT).show();
        }
        if (!editText1.getText().toString().equals("")) {
            new Thread() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(2);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    requireMyWorks("getUser.do", "userid=" + editText1.getText().toString());
                }
            }.start();
        } else if (!editText2.getText().toString().equals("")) {
            new Thread() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(3);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    requireMyWorks("getUser.do", "meterNumber=" + editText2.getText().toString());
                }
            }.start();
        }
    }

    //请求网络数据
    private void requireMyWorks(final String method, final String keyAndValue) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url;
                    HttpURLConnection httpURLConnection;
                    Log.i("sharedPreferences====>", sharedPreferences.getString("IP", ""));
                    if (!sharedPreferences.getString("IP", "").equals("")) {
                        ip = sharedPreferences.getString("IP", "");
                        //Log.i("sharedPreferences=ip=>",ip);
                    } else {
                        ip = "192.168.2.201:8080";
                    }
                    String httpUrl = "http://" + ip + "/SMDemo/" + method;
                    //有参数传递
                    if (!keyAndValue.equals("")) {
                        url = new URL(httpUrl + "?" + keyAndValue);
                        //没有参数传递
                    } else {
                        url = new URL(httpUrl);
                    }
                    Log.i("url=============>", url + "");
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(6000);
                    httpURLConnection.setReadTimeout(6000);
                    httpURLConnection.connect();
                    //传回的数据解析成String
                    Log.i("responseCode====>", httpURLConnection.getResponseCode() + "");
                    if ((responseCode = httpURLConnection.getResponseCode()) == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String str;
                        while ((str = bufferedReader.readLine()) != null) {
                            stringBuilder.append(str);
                        }
                        result = stringBuilder.toString();
                        Log.i("result_query==========>", result);
                        JSONObject jsonObject = new JSONObject(result);
                        if (!jsonObject.optString("total", "").equals("0")) {
                            handler.sendEmptyMessage(0);
                        } else {
                            try {
                                Thread.sleep(3000);
                                handler.sendEmptyMessage(4);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            Thread.sleep(1000);
                            handler.sendEmptyMessage(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("IOException==========>", "网络请求异常!");
                    handler.sendEmptyMessage(4);
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {   //验证成功，进行网络请求，弹出框消失
                Intent intent = new Intent(QueryActivity.this, CostListviewActivity.class);
                if (radioButton1.isChecked()) {
                    intent.putExtra("userid", editText1.getText().toString());
                    Log.i("userid_QueryActivity==>", editText1.getText().toString());
                } else if (radioButton2.isChecked()) {
                    intent.putExtra("meterNumber", editText2.getText().toString());
                    Log.i("meterNumberQueryActivit", editText2.getText().toString());
                }
                startActivityForResult(intent, 100);
                popupWindow.dismiss();
                editText1.setEnabled(false);
                editText2.setEnabled(false);
            } else if (msg.what == 1) {   //返回码不是200,账号错误，弹出框消失
                popupWindow.dismiss();
                Toast.makeText(QueryActivity.this, "编号不正确，请重新输入！", Toast.LENGTH_SHORT).show();
                if (radioButton1.isChecked()) {
                    editText1.setEnabled(true);
                    editText2.setEnabled(false);
                } else {
                    editText1.setEnabled(false);
                    editText2.setEnabled(true);
                }
            } else if (msg.what == 2) {   //show弹出框
                editText1.setEnabled(false);
                editText2.setEnabled(false);
                showPopupwindow();
            } else if (msg.what == 3) {   //show弹出框
                editText1.setEnabled(false);
                editText2.setEnabled(false);
                showPopupwindow();
            } else if (msg.what == 4) {   //返回码是200,但是数据为空
                popupWindow.dismiss();
                Toast.makeText(QueryActivity.this, "网络请求超时！", Toast.LENGTH_SHORT).show();
                if (radioButton1.isChecked()) {
                    editText1.setEnabled(true);
                    editText2.setEnabled(false);
                } else {
                    editText1.setEnabled(false);
                    editText2.setEnabled(true);
                }
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    //返回当前页面的回调方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            editText1.setText("");
            editText2.setText("");
            if (radioButton1.isChecked()) {
                editText1.setFocusable(true);
                editText1.setFocusableInTouchMode(true);
                editText1.setCursorVisible(true);
                editText1.setEnabled(true);
                editText2.setEnabled(false);
            } else if (radioButton2.isChecked()) {
                editText2.setFocusable(true);
                editText2.setFocusableInTouchMode(true);
                editText2.setCursorVisible(true);
                editText1.setEnabled(false);
                editText2.setEnabled(true);
            }
        }
        if (resultCode == 200) {
            //more.startAnimation(AnimationUtils.loadAnimation(QueryActivity.this,R.anim.more_rotate_back));
            backAnimation();
            editText1.setText("");
            editText2.setText("");
            if (radioButton1.isChecked()) {
                editText1.setFocusable(true);
                editText1.setFocusableInTouchMode(true);
                editText1.setCursorVisible(true);
                editText1.setEnabled(true);
                editText2.setEnabled(false);
            } else if (radioButton2.isChecked()) {
                editText2.setFocusable(true);
                editText2.setFocusableInTouchMode(true);
                editText2.setCursorVisible(true);
                editText1.setEnabled(false);
                editText2.setEnabled(true);
            }
        }
    }

    /**
     * 捕捉返回事件按钮
     * 因为此 Activity继承 TabActivity,用 onKeyDown无响应，
     * 所以改用 dispatchKeyEvent
     * <p/>
     * 一般的 Activity 用 onKeyDown就可以了
     *//*
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    *//**
     * 退出程序
     *//*
    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Log.i("exitTime==========>", System.currentTimeMillis() - exitTime + "");
            //-------------Activity.this的context 返回当前activity的上下文，属于activity，activity 摧毁他就摧毁
            //-------------getApplicationContext() 返回应用的上下文，生命周期是整个应用，应用摧毁它才摧毁
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }*/
}
