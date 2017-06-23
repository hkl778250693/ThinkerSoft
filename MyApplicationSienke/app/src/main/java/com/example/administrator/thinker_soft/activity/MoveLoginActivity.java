package com.example.administrator.thinker_soft.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.thinker_soft.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/5/25.
 */
public class MoveLoginActivity extends Activity{
    private Button loginBtn;
    private long exitTime = 0;//退出程序
    private CheckBox remindMe;  //记住账号密码
    private EditText editUser, editPsw;
    private SharedPreferences sharedPreferences_login,public_sharedPreferences;
    private SharedPreferences.Editor editor;
    private String ip, port;  //接口ip地址   端口
    private TextView ipTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_login);

        bindView();//绑定控件
        defaultSetting();
        setViewClickListener();//点击事件
    }

    //绑定控件
    public void bindView(){
        editUser = (EditText) findViewById(R.id.edit_user);
        editPsw = (EditText) findViewById(R.id.edit_psw);
        remindMe = (CheckBox) findViewById(R.id.remind_me);
        ipTextView = (TextView) findViewById(R.id.ip);
        loginBtn = (Button) findViewById(R.id.login_btn);
    }

    //初始化设置
    private void defaultSetting() {
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        public_sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences_login.edit();
        Log.i("MobileSecurityLogin", "默认设置进来了！是否记住账号："+sharedPreferences_login.getBoolean("remind_me",false));
        if(sharedPreferences_login.getBoolean("remind_me",false)){
            Log.i("MobileSecurityLogin", "记住账号默认设置进来了！");
            remindMe.setChecked(true);
            editUser.setText(sharedPreferences_login.getString("login_name",""));//设置EditText控件的内容
            editUser.setSelection(sharedPreferences_login.getString("login_name","").length());//将光标移至文字末尾
            editPsw.setText(sharedPreferences_login.getString("login_psw",""));
            editPsw.setSelection(sharedPreferences_login.getString("login_psw","").length());//将光标移至文字末尾
        }else {
            editUser.setText(sharedPreferences_login.getString("login_name",""));//设置EditText控件的内容
            editUser.setSelection(sharedPreferences_login.getString("login_name","").length());//将光标移至文字末尾
            editUser.requestFocus();
            Log.i("MobileSecurityLogin", "没记住账号默认设置进来了！");
        }
    }

    //点击事件
    public void setViewClickListener(){
        loginBtn.setOnClickListener(onclickListener);
        ipTextView.setOnClickListener(onclickListener);
        remindMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Log.i("MobileSecurityLogin", "记住账号");
                    editor.putBoolean("remind_me",true);
                    editor.apply();
                }else {
                    Log.i("MobileSecurityLogin", "没记住账号");
                    editor.putBoolean("remind_me",false);
                    editor.apply();
                }
            }
        });
    }
    View.OnClickListener onclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ip:
                    Intent intent = new Intent(MoveLoginActivity.this, IpSettingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.login_btn:
                    loginBtn.setClickable(false);
                    if (editUser.getText().toString().equals("") && editPsw.getText().toString().equals("")) {
                        Toast.makeText(MoveLoginActivity.this, "请您输入用户名和密码！", Toast.LENGTH_LONG).show();
                        loginBtn.setClickable(true);
                    } else if (editUser.getText().toString().equals("")) {
                        Toast.makeText(MoveLoginActivity.this, "请您输入用户名！", Toast.LENGTH_LONG).show();
                        loginBtn.setClickable(true);
                    } else if (editPsw.getText().toString().equals("")) {
                        Toast.makeText(MoveLoginActivity.this, "请您输入密码！", Toast.LENGTH_LONG).show();
                        loginBtn.setClickable(true);
                    }
                    if (!editUser.getText().toString().equals("") && !editPsw.getText().toString().equals("")) {
                        //开启子线程
                        new Thread() {
                            public void run() {
                                loginByPost(editUser.getText().toString(), editPsw.getText().toString());
                            }
                        }.start();
                    }
                    break;
            }
        }
    };

    //post请求
    public void loginByPost(final String userName, final String userPass) {
        new Thread() {
            @Override
            public void run() {
                try {
                    //请求的地址
                    if (!public_sharedPreferences.getString("security_ip", "").equals("")) {
                        ip = public_sharedPreferences.getString("security_ip", "");
                    } else {
                        ip = "192.168.2.201:";
                    }
                    if (!public_sharedPreferences.getString("security_port", "").equals("")) {
                        port = public_sharedPreferences.getString("security_port", "");
                    } else {
                        port = "8080";
                    }
                    String httpUrl = "http://" + ip + port + "/SMDemo/login.do";
                    Log.i("httpUrl==========>", "" + httpUrl);
                    // 根据地址创建URL对象
                    URL url = new URL(httpUrl);
                    // 根据URL对象打开链接
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    // 发送POST请求必须设置允许输出
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setUseCaches(false);//不使用缓存
                    // 设置请求的方式
                    urlConnection.setRequestMethod("POST");
                    // 设置请求的超时时间
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setConnectTimeout(5000);
                    // 传递的数据
                    String data = "username=" + URLEncoder.encode(userName, "UTF-8") + "&password=" + URLEncoder.encode(userPass, "UTF-8");
                    Log.i("data==========>","data="+data);
                    // 设置请求的头
                    //urlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                    //urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    //urlConnection.setRequestProperty("Origin", "http://"+ ip + port);
                    Log.i("data==========>", "data=" + data);
                    // 设置请求的头
                    //urlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    //urlConnection.setRequestProperty("Origin", "http://"+ ip + port);
                    urlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                    //urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
                    //获取输出流
                    OutputStream os = urlConnection.getOutputStream();
                    os.write(data.getBytes("UTF-8"));
                    os.flush();
                    os.close();
                    Log.i("getResponseCode====>", "" + urlConnection.getResponseCode());
                    if (urlConnection.getResponseCode() == 200) {
                        InputStream inputStream = urlConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String str;
                        while ((str = bufferedReader.readLine()) != null) {
                            stringBuilder.append(str);
                        }
                        // 释放资源
                        inputStream.close();
                        // 返回字符串
                        String result = stringBuilder.toString();
                        Log.i("login_result=========>", result);
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.optInt("messg", 0) == 1) {
                            editor.putInt("company_id",jsonObject.optInt("companyid",0));
                            editor.putString("user_name",jsonObject.optString("userName",""));
                            editor.putString("userId",jsonObject.optInt("systemuserId",0)+"");
                            editor.apply();
                            Log.i("MobileSecurityLogin", "当前用户公司ID是："+sharedPreferences_login.getInt("company_id",0));
                            Log.i("MobileSecurityLogin", "当前用户是："+sharedPreferences_login.getString("user_name",""));
                            handler.sendEmptyMessage(1);
                        }
                        if (jsonObject.optInt("messg", 0) == 0) {
                            handler.sendEmptyMessage(2);
                        }
                    } else {
                        handler.sendEmptyMessage(3);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("IOException==========>", "网络请求异常!");
                    handler.sendEmptyMessage(3);
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if(editUser.getText().toString().equals(sharedPreferences_login.getString("login_name","")) && editPsw.getText().toString().equals(sharedPreferences_login.getString("login_psw",""))){
                        Log.i("MobileSecurityLogin", "用户未改变");
                        sharedPreferences_login.edit().putBoolean("user_exchanged",false).apply();
                    }else {
                        Log.i("MobileSecurityLogin", "用户改变");
                        sharedPreferences_login.edit().putBoolean("user_exchanged",true).apply();
                    }
                    Log.i("MobileSecurityLogin", "是否记住账号"+sharedPreferences_login.getBoolean("remind_me",false));
                    if(sharedPreferences_login.getBoolean("remind_me",false)){
                        editor.putString("login_name",editUser.getText().toString());
                        editor.putString("login_psw",editPsw.getText().toString());
                        editor.apply();
                    }else {
                        editor.putString("login_name",editUser.getText().toString()).apply();
                        Log.i("MobileSecurityLogin", "未记住密码！账号为"+editUser.getText().toString());
                    }
                    Toast.makeText(MoveLoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MoveLoginActivity.this, MoveHomePageActivity.class);
                    startActivity(intent);
                    loginBtn.setClickable(true);
                    editor.putBoolean("have_logined",true);
                    editor.apply();
                    finish();
                    break;
                case 2:
                    loginBtn.setClickable(true);
                    Toast.makeText(MoveLoginActivity.this, "账号或密码错误!", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    loginBtn.setClickable(true);
                    Toast.makeText(MoveLoginActivity.this, "网络请求异常!", Toast.LENGTH_LONG).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
